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
    private LinearLayout emptyView;
    private TextView tvEmptyMessage;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghi_chu);

        // Lấy user ID
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        initViews();
        loadNotes();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewNotes);
        emptyView = findViewById(R.id.emptyView);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GhiChuAdapter(ghiChuList, this);
        recyclerView.setAdapter(adapter);

        // Nút Back
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Nút thêm ghi chú
        findViewById(R.id.fabAddNote).setOnClickListener(v -> {
            Intent intent = new Intent(this, ThemGhiChuActivity.class);
            startActivity(intent);
        });
    }

    private void loadNotes() {
        new Thread(() -> {
            List<GhiChu> list = GhiChuRepository.getActiveNotes(userId);

            runOnUiThread(() -> {
                ghiChuList.clear();
                ghiChuList.addAll(list);
                adapter.updateData(ghiChuList);

                if (ghiChuList.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    tvEmptyMessage.setText("Chưa có ghi chú nào");
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
        // Hiển thị chi tiết ghi chú
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
                .setMessage("Bạn có muốn xóa ghi chú \"" + ghiChu.getTieuDe() + "\"?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    new Thread(() -> {
                        boolean success = GhiChuRepository.deleteNote(ghiChu.getMaGhiChu());
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
}
