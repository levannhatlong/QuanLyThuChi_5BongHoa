package com.example.quanlythuchi_5bonghoa;

import android.content.Intent;
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

public class GhiChuActivity extends AppCompatActivity implements GhiChuAdapter.OnNoteActionListener {

    private RecyclerView recyclerView;
    private GhiChuAdapter adapter;
    private List<GhiChu> ghiChuList = new ArrayList<>();
    private TextView tabActive, tabHistory;
    private LinearLayout emptyView;
    private TextView tvEmptyMessage;
    private int userId;
    private boolean isHistoryMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghi_chu);

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        initViews();
        setupTabs();
        loadNotes();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewNotes);
        tabActive = findViewById(R.id.tabActive);
        tabHistory = findViewById(R.id.tabHistory);
        emptyView = findViewById(R.id.emptyView);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GhiChuAdapter(ghiChuList, isHistoryMode, this);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        findViewById(R.id.fabAddNote).setOnClickListener(v -> {
            Intent intent = new Intent(this, ThemGhiChuActivity.class);
            startActivity(intent);
        });
    }

    private void setupTabs() {
        tabActive.setOnClickListener(v -> {
            if (isHistoryMode) {
                isHistoryMode = false;
                updateTabUI();
                loadNotes();
            }
        });

        tabHistory.setOnClickListener(v -> {
            if (!isHistoryMode) {
                isHistoryMode = true;
                updateTabUI();
                loadNotes();
            }
        });
    }

    private void updateTabUI() {
        if (isHistoryMode) {
            tabActive.setBackgroundResource(R.drawable.bg_tab_unselected);
            tabActive.setTextColor(getResources().getColor(R.color.mau_chu_dao));
            tabHistory.setBackgroundResource(R.drawable.bg_tab_selected);
            tabHistory.setTextColor(getResources().getColor(android.R.color.white));
            findViewById(R.id.fabAddNote).setVisibility(View.GONE);
        } else {
            tabActive.setBackgroundResource(R.drawable.bg_tab_selected);
            tabActive.setTextColor(getResources().getColor(android.R.color.white));
            tabHistory.setBackgroundResource(R.drawable.bg_tab_unselected);
            tabHistory.setTextColor(getResources().getColor(R.color.mau_chu_dao));
            findViewById(R.id.fabAddNote).setVisibility(View.VISIBLE);
        }
    }

    private void loadNotes() {
        new Thread(() -> {
            List<GhiChu> list;
            if (isHistoryMode) {
                list = GhiChuRepository.getDeletedNotes(userId);
            } else {
                list = GhiChuRepository.getActiveNotes(userId);
            }

            runOnUiThread(() -> {
                ghiChuList.clear();
                ghiChuList.addAll(list);
                adapter.updateData(ghiChuList, isHistoryMode);

                if (ghiChuList.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    tvEmptyMessage.setText(isHistoryMode ? "Không có ghi chú đã xóa" : "Chưa có ghi chú nào");
                } else {
                    emptyView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    @Override
    public void onClick(GhiChu ghiChu) {
        new AlertDialog.Builder(this)
                .setTitle(ghiChu.getTieuDe())
                .setMessage(ghiChu.getNoiDung() + "\n\nNgày tạo: " + ghiChu.getNgayTao())
                .setPositiveButton("Đóng", null)
                .show();
    }

    @Override
    public void onEdit(GhiChu ghiChu) {
        Intent intent = new Intent(this, ThemGhiChuActivity.class);
        intent.putExtra("ma_ghi_chu", ghiChu.getMaGhiChu());
        intent.putExtra("tieu_de", ghiChu.getTieuDe());
        intent.putExtra("noi_dung", ghiChu.getNoiDung());
        startActivity(intent);
    }

    @Override
    public void onDelete(GhiChu ghiChu) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có muốn xóa ghi chú \"" + ghiChu.getTieuDe() + "\"?\nGhi chú sẽ được chuyển vào mục đã xóa.")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    new Thread(() -> {
                        boolean success = GhiChuRepository.softDeleteNote(ghiChu.getMaGhiChu());
                        runOnUiThread(() -> {
                            if (success) {
                                Toast.makeText(this, "Đã xóa ghi chú", Toast.LENGTH_SHORT).show();
                                loadNotes();
                            } else {
                                Toast.makeText(this, "Lỗi khi xóa", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }).start();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onRestore(GhiChu ghiChu) {
        new AlertDialog.Builder(this)
                .setTitle("Khôi phục ghi chú")
                .setMessage("Bạn có muốn khôi phục ghi chú \"" + ghiChu.getTieuDe() + "\"?")
                .setPositiveButton("Khôi phục", (dialog, which) -> {
                    new Thread(() -> {
                        boolean success = GhiChuRepository.restoreNote(ghiChu.getMaGhiChu());
                        runOnUiThread(() -> {
                            if (success) {
                                Toast.makeText(this, "Đã khôi phục ghi chú", Toast.LENGTH_SHORT).show();
                                loadNotes();
                            } else {
                                Toast.makeText(this, "Lỗi khi khôi phục", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }).start();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onPermanentDelete(GhiChu ghiChu) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa vĩnh viễn")
                .setMessage("Bạn có chắc muốn xóa vĩnh viễn ghi chú \"" + ghiChu.getTieuDe() + "\"?\nHành động này không thể hoàn tác!")
                .setPositiveButton("Xóa vĩnh viễn", (dialog, which) -> {
                    new Thread(() -> {
                        boolean success = GhiChuRepository.permanentDeleteNote(ghiChu.getMaGhiChu());
                        runOnUiThread(() -> {
                            if (success) {
                                Toast.makeText(this, "Đã xóa vĩnh viễn", Toast.LENGTH_SHORT).show();
                                loadNotes();
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
