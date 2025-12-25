package com.example.quanlythuchi_5bonghoa;

<<<<<<< HEAD
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateUtils;
=======
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
>>>>>>> 21f642b447fe83afa3f79fcbc14a938f6b06beaa
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
<<<<<<< HEAD

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
=======
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
>>>>>>> 21f642b447fe83afa3f79fcbc14a938f6b06beaa
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThongBaoActivity extends AppCompatActivity implements ThongBaoAdapter.OnNotificationActionListener {

    private RecyclerView recyclerView;
    private ThongBaoAdapter adapter;
<<<<<<< HEAD

    private List<ThongBao> allNotifications = new ArrayList<>();
=======
    private List<ThongBao> thongBaoList = new ArrayList<>();
    private TextView tabTatCa, tabChuaDoc, tabDaDoc;
    private LinearLayout emptyView;
    private TextView tvEmptyMessage;
    private int userId;
>>>>>>> 21f642b447fe83afa3f79fcbc14a938f6b06beaa
    private String currentTab = "tatca";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_bao);

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        initViews();
<<<<<<< HEAD

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
=======
        setupTabs();
        loadNotifications();
>>>>>>> 21f642b447fe83afa3f79fcbc14a938f6b06beaa
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewThongBao);
        tabTatCa = findViewById(R.id.tabTatCa);
        tabChuaDoc = findViewById(R.id.tabChuaDoc);
        tabDaDoc = findViewById(R.id.tabDaDoc);
        emptyView = findViewById(R.id.emptyView);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ThongBaoAdapter(thongBaoList, this);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Đánh dấu tất cả đã đọc
        findViewById(R.id.btnMarkAllRead).setOnClickListener(v -> markAllAsRead());
    }

<<<<<<< HEAD
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

=======
    private void setupTabs() {
>>>>>>> 21f642b447fe83afa3f79fcbc14a938f6b06beaa
        tabTatCa.setOnClickListener(v -> {
            currentTab = "tatca";
            updateTabUI();
            loadNotifications();
        });

        tabChuaDoc.setOnClickListener(v -> {
            currentTab = "chuadoc";
            updateTabUI();
            loadNotifications();
        });

        tabDaDoc.setOnClickListener(v -> {
            currentTab = "dadoc";
            updateTabUI();
            loadNotifications();
        });
    }

    private void updateTabUI() {
        // Reset tất cả tab
        tabTatCa.setBackgroundResource(R.drawable.bg_tab_unselected);
        tabTatCa.setTextColor(getResources().getColor(R.color.mau_chu_dao));
        tabChuaDoc.setBackgroundResource(R.drawable.bg_tab_unselected);
        tabChuaDoc.setTextColor(getResources().getColor(R.color.mau_chu_dao));
        tabDaDoc.setBackgroundResource(R.drawable.bg_tab_unselected);
        tabDaDoc.setTextColor(getResources().getColor(R.color.mau_chu_dao));

        // Highlight tab được chọn
        switch (currentTab) {
            case "tatca":
                tabTatCa.setBackgroundResource(R.drawable.bg_tab_selected);
                tabTatCa.setTextColor(getResources().getColor(android.R.color.white));
                break;
            case "chuadoc":
                tabChuaDoc.setBackgroundResource(R.drawable.bg_tab_selected);
                tabChuaDoc.setTextColor(getResources().getColor(android.R.color.white));
                break;
            case "dadoc":
                tabDaDoc.setBackgroundResource(R.drawable.bg_tab_selected);
                tabDaDoc.setTextColor(getResources().getColor(android.R.color.white));
                break;
        }
    }

<<<<<<< HEAD
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
=======
    private void loadNotifications() {
        new Thread(() -> {
            List<ThongBao> list;
            switch (currentTab) {
                case "chuadoc":
                    list = ThongBaoRepository.getUnreadNotifications(userId);
                    break;
                case "dadoc":
                    list = ThongBaoRepository.getReadNotifications(userId);
                    break;
                default:
                    list = ThongBaoRepository.getAllNotifications(userId);
            }

            runOnUiThread(() -> {
                thongBaoList.clear();
                thongBaoList.addAll(list);
                adapter.updateData(thongBaoList);

                if (thongBaoList.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    switch (currentTab) {
                        case "chuadoc":
                            tvEmptyMessage.setText("Không có thông báo chưa đọc");
                            break;
                        case "dadoc":
                            tvEmptyMessage.setText("Không có thông báo đã đọc");
                            break;
                        default:
                            tvEmptyMessage.setText("Không có thông báo");
                    }
                } else {
                    emptyView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            });
        }).start();
    }

    private void markAllAsRead() {
        new Thread(() -> {
            boolean success = ThongBaoRepository.markAllAsRead(userId);
            runOnUiThread(() -> {
                if (success) {
                    Toast.makeText(this, "Đã đánh dấu tất cả đã đọc", Toast.LENGTH_SHORT).show();
                    loadNotifications();
                }
            });
        }).start();
    }

    @Override
    public void onClick(ThongBao thongBao) {
        // Đánh dấu đã đọc khi click
        if (!thongBao.isDaDoc()) {
            new Thread(() -> {
                ThongBaoRepository.markAsRead(thongBao.getMaThongBao());
                runOnUiThread(() -> loadNotifications());
            }).start();
        }

        // Hiển thị dialog chi tiết
        showDetailDialog(thongBao);
    }

    private void showDetailDialog(ThongBao thongBao) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_chi_tiet_thong_bao);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        // Ánh xạ views
        ImageView ivIconDialog = dialog.findViewById(R.id.ivIconDialog);
        ImageView btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
        TextView tvLoaiThongBao = dialog.findViewById(R.id.tvLoaiThongBao);
        TextView tvThoiGianDialog = dialog.findViewById(R.id.tvThoiGianDialog);
        TextView tvTieuDeDialog = dialog.findViewById(R.id.tvTieuDeDialog);
        TextView tvNoiDungDialog = dialog.findViewById(R.id.tvNoiDungDialog);
        TextView tvTrangThai = dialog.findViewById(R.id.tvTrangThai);
        View viewTrangThai = dialog.findViewById(R.id.viewTrangThai);
        TextView btnXoaThongBao = dialog.findViewById(R.id.btnXoaThongBao);
        TextView btnDongDialog = dialog.findViewById(R.id.btnDongDialog);

        // Set dữ liệu
        tvTieuDeDialog.setText(thongBao.getTieuDe() != null ? thongBao.getTieuDe() : "Thông báo");
        tvNoiDungDialog.setText(thongBao.getNoiDung() != null ? thongBao.getNoiDung() : "");
        tvThoiGianDialog.setText(thongBao.getNgayTao() != null ? thongBao.getNgayTao() : "");

        // Loại thông báo
        String loai = thongBao.getLoaiThongBao();
        if (loai != null) {
            switch (loai) {
                case "canh_bao":
                    tvLoaiThongBao.setText("⚠️ Cảnh báo");
                    ivIconDialog.setImageResource(R.drawable.ic_alert);
                    break;
                case "nhac_nho":
                    tvLoaiThongBao.setText("📅 Nhắc nhở");
                    ivIconDialog.setImageResource(R.drawable.ic_calendar);
                    break;
                default:
                    tvLoaiThongBao.setText("🔔 Thông báo hệ thống");
                    ivIconDialog.setImageResource(R.drawable.ic_notification);
            }
        } else {
            tvLoaiThongBao.setText("🔔 Thông báo hệ thống");
        }

        // Trạng thái đọc
        if (thongBao.isDaDoc()) {
            tvTrangThai.setText("Đã đọc");
            viewTrangThai.setBackgroundResource(R.drawable.bg_status_active);
        } else {
            tvTrangThai.setText("Chưa đọc");
            viewTrangThai.setBackgroundResource(R.drawable.bg_unread_dot);
        }

        // Sự kiện
        btnCloseDialog.setOnClickListener(v -> dialog.dismiss());
        btnDongDialog.setOnClickListener(v -> dialog.dismiss());

        btnXoaThongBao.setOnClickListener(v -> {
            dialog.dismiss();
            new AlertDialog.Builder(this)
                    .setTitle("Xóa thông báo")
                    .setMessage("Bạn có muốn xóa thông báo này?")
                    .setPositiveButton("Xóa", (d, which) -> {
                        new Thread(() -> {
                            boolean success = ThongBaoRepository.deleteNotification(thongBao.getMaThongBao());
                            runOnUiThread(() -> {
                                if (success) {
                                    Toast.makeText(this, "Đã xóa thông báo", Toast.LENGTH_SHORT).show();
                                    loadNotifications();
                                } else {
                                    Toast.makeText(this, "Lỗi khi xóa", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }).start();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        dialog.show();
    }

    @Override
    public void onDelete(ThongBao thongBao) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa thông báo")
                .setMessage("Bạn có muốn xóa thông báo này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    new Thread(() -> {
                        boolean success = ThongBaoRepository.deleteNotification(thongBao.getMaThongBao());
                        runOnUiThread(() -> {
                            if (success) {
                                Toast.makeText(this, "Đã xóa thông báo", Toast.LENGTH_SHORT).show();
                                loadNotifications();
                            } else {
                                Toast.makeText(this, "Lỗi khi xóa", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }).start();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
>>>>>>> 21f642b447fe83afa3f79fcbc14a938f6b06beaa
}
