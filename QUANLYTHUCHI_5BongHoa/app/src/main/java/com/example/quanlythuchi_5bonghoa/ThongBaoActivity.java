package com.example.quanlythuchi_5bonghoa;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ThongBaoActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tabTatCa, tabDaDoc, tabChuaDoc;
    private RecyclerView recyclerViewThongBao;
    private ThongBaoAdapter adapter;

    private List<ThongBao> allNotifications;
    private String currentTab = "tatca";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_bao);

        initViews();
        setupRecyclerView();
        setupListeners();
        loadNotifications();
        selectTab("tatca");
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
        adapter = new ThongBaoAdapter(new ArrayList<>());
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

    private void loadNotifications() {
        allNotifications = new ArrayList<>();

        allNotifications.add(new ThongBao(
                "Cảnh báo: Chi tiêu vượt mức giới hạn. 🔥",
                "6 phút trước",
                false
        ));

        allNotifications.add(new ThongBao(
                "Nhắc nhở: Nạp tiền vào ví cho thánh toán sử dụng ngân hàng Vietcombank.",
                "1 ngày trước",
                false
        ));

        allNotifications.add(new ThongBao(
                "Nhắc nhở: Phải nộp chi tiền 5,000,000 VNĐ.",
                "1 ngày trước",
                true
        ));

        allNotifications.add(new ThongBao(
                "Nhắc nhở: Đã thanh toán tiền 400,000 VNĐ.",
                "2 ngày trước",
                true
        ));

        allNotifications.add(new ThongBao(
                "Nhắc nhở: Đã chi tiền 300,000 VNĐ cho ăn uống.",
                "2 ngày trước",
                true
        ));

        filterNotifications(currentTab);
    }

    private void filterNotifications(String filter) {
        List<ThongBao> filteredList = new ArrayList<>();

        switch (filter) {
            case "tatca":
                filteredList = allNotifications;
                break;
            case "dadoc":
                for (ThongBao tb : allNotifications) {
                    if (tb.isDaDoc()) {
                        filteredList.add(tb);
                    }
                }
                break;
            case "chuadoc":
                for (ThongBao tb : allNotifications) {
                    if (!tb.isDaDoc()) {
                        filteredList.add(tb);
                    }
                }
                break;
        }

        adapter.updateData(filteredList);
    }

    // Inner class for Notification data
    public static class ThongBao {
        private String noiDung;
        private String thoiGian;
        private boolean daDoc;

        public ThongBao(String noiDung, String thoiGian, boolean daDoc) {
            this.noiDung = noiDung;
            this.thoiGian = thoiGian;
            this.daDoc = daDoc;
        }

        public String getNoiDung() {
            return noiDung;
        }

        public String getThoiGian() {
            return thoiGian;
        }

        public boolean isDaDoc() {
            return daDoc;
        }

        public void setDaDoc(boolean daDoc) {
            this.daDoc = daDoc;
        }
    }
}