package com.example.quanlythuchi_5bonghoa;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThongBaoActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tabTatCa, tabDaDoc, tabChuaDoc;
    private RecyclerView recyclerViewThongBao;
    private ThongBaoAdapter adapter;

    private List<ThongBao> allNotifications = new ArrayList<>();
    private String currentTab = "tatca";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_bao);

        initViews();

        userId = getUserId();
        if (userId <= 0) {
            toast("Chưa xác định người dùng. Vui lòng đăng nhập lại.");
            finish();
            return;
        }

        setupRecyclerView();
        setupListeners();

        selectTab("tatca");
        loadNotificationsFromDb();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }

    private int getUserId() {
        SharedPreferences sp = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sp.getInt("user_id", -1);
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tabTatCa = findViewById(R.id.tabTatCa);
        tabDaDoc = findViewById(R.id.tabDaDoc);
        tabChuaDoc = findViewById(R.id.tabChuaDoc);
        recyclerViewThongBao = findViewById(R.id.recyclerViewThongBao);
    }

    private void setupRecyclerView() {
        recyclerViewThongBao.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ThongBaoAdapter(new ArrayList<>(), thongBao -> {
            // bấm thông báo -> đánh dấu đã đọc (nếu chưa đọc)
            if (!thongBao.isDaDoc()) {
                markAsRead(thongBao.getMaThongBao());
                thongBao.setDaDoc(true);
                filterNotifications(currentTab);
            }
        });
        recyclerViewThongBao.setAdapter(adapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        tabTatCa.setOnClickListener(v -> {
            selectTab("tatca");
            filterNotifications("tatca");
        });

        tabDaDoc.setOnClickListener(v -> {
            selectTab("dadoc");
            filterNotifications("dadoc");
        });

        tabChuaDoc.setOnClickListener(v -> {
            selectTab("chuadoc");
            filterNotifications("chuadoc");
        });
    }

    private void selectTab(String tab) {
        currentTab = tab;

        tabTatCa.setSelected(false);
        tabDaDoc.setSelected(false);
        tabChuaDoc.setSelected(false);

        switch (tab) {
            case "tatca":
                tabTatCa.setSelected(true);
                break;
            case "dadoc":
                tabDaDoc.setSelected(true);
                break;
            case "chuadoc":
                tabChuaDoc.setSelected(true);
                break;
        }
    }

    private void loadNotificationsFromDb() {
        executor.execute(() -> {
            List<ThongBao> list = fetchThongBao(userId);

            runOnUiThread(() -> {
                if (list == null) {
                    toast("Lỗi kết nối hoặc truy vấn ThongBao.");
                    return;
                }
                allNotifications.clear();
                allNotifications.addAll(list);

                filterNotifications(currentTab);
                if (allNotifications.isEmpty()) {
                    toast("Không có thông báo cho tài khoản này.");
                }
            });
        });
    }

    private List<ThongBao> fetchThongBao(int userId) {
        List<ThongBao> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnector.getConnection();
            if (conn == null) return null;

            String sql =
                    "SELECT MaThongBao, NoiDung, DaDoc, NgayTao " +
                            "FROM ThongBao " +
                            "WHERE MaNguoiDung = ? " +
                            "ORDER BY NgayTao DESC";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                int maThongBao = rs.getInt("MaThongBao");
                String noiDung = rs.getString("NoiDung");
                boolean daDoc = rs.getBoolean("DaDoc");
                Date ngayTao = rs.getTimestamp("NgayTao");

                String thoiGian = formatRelativeTime(ngayTao);
                list.add(new ThongBao(maThongBao, noiDung, thoiGian, daDoc));
            }

            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (ps != null) ps.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }

    private void markAsRead(int maThongBao) {
        executor.execute(() -> {
            Connection conn = null;
            PreparedStatement ps = null;
            try {
                conn = DatabaseConnector.getConnection();
                if (conn == null) return;

                String sql = "UPDATE ThongBao SET DaDoc = 1 WHERE MaThongBao = ? AND MaNguoiDung = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, maThongBao);
                ps.setInt(2, userId);
                ps.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try { if (ps != null) ps.close(); } catch (Exception ignored) {}
                try { if (conn != null) conn.close(); } catch (Exception ignored) {}
            }
        });
    }

    private void filterNotifications(String filter) {
        List<ThongBao> filteredList = new ArrayList<>();

        switch (filter) {
            case "tatca":
                filteredList.addAll(allNotifications);
                break;
            case "dadoc":
                for (ThongBao tb : allNotifications) {
                    if (tb.isDaDoc()) filteredList.add(tb);
                }
                break;
            case "chuadoc":
                for (ThongBao tb : allNotifications) {
                    if (!tb.isDaDoc()) filteredList.add(tb);
                }
                break;
        }

        adapter.updateData(filteredList);
    }

    private String formatRelativeTime(Date date) {
        if (date == null) return "";
        long now = System.currentTimeMillis();
        return DateUtils.getRelativeTimeSpanString(
                date.getTime(),
                now,
                DateUtils.MINUTE_IN_MILLIS
        ).toString(); // ví dụ: "6 phút trước", "Hôm qua", ...
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    // Inner class (giữ nguyên để bạn khỏi tạo file mới)
    public static class ThongBao {
        private final int maThongBao;
        private final String noiDung;
        private final String thoiGian;
        private boolean daDoc;

        public ThongBao(int maThongBao, String noiDung, String thoiGian, boolean daDoc) {
            this.maThongBao = maThongBao;
            this.noiDung = noiDung;
            this.thoiGian = thoiGian;
            this.daDoc = daDoc;
        }

        public int getMaThongBao() { return maThongBao; }
        public String getNoiDung() { return noiDung; }
        public String getThoiGian() { return thoiGian; }
        public boolean isDaDoc() { return daDoc; }
        public void setDaDoc(boolean daDoc) { this.daDoc = daDoc; }
    }
}
