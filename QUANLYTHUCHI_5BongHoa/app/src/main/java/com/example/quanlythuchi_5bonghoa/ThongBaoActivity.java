package com.example.quanlythuchi_5bonghoa;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
}
