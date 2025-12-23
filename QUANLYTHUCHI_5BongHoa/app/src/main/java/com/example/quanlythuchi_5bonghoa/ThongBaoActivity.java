package com.example.quanlythuchi_5bonghoa;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ThongBaoActivity extends AppCompatActivity implements ThongBaoAdapter.OnNotificationActionListener {

    private RecyclerView recyclerView;
    private ThongBaoAdapter adapter;
    private List<ThongBao> thongBaoList = new ArrayList<>();
    private TextView tabTatCa, tabChuaDoc, tabDaDoc;
    private LinearLayout emptyView;
    private TextView tvEmptyMessage;
    private int userId;
    private String currentTab = "tatca";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_bao);

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        initViews();
        setupTabs();
        loadNotifications();
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

    private void setupTabs() {
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

        // Hiển thị chi tiết
        new AlertDialog.Builder(this)
                .setTitle(thongBao.getTieuDe())
                .setMessage(thongBao.getNoiDung())
                .setPositiveButton("Đóng", null)
                .show();
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
}
