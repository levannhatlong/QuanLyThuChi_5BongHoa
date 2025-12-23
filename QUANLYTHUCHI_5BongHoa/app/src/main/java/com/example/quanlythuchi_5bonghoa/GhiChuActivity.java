package com.example.quanlythuchi_5bonghoa; // ← sửa thành package của bạn

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;

public class GhiChuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghi_chu);

        ListView listView = findViewById(R.id.listViewNotes);

        // === DỮ LIỆU GIẢ (10 ghi chú đẹp, thêm 5 cái mới) ===
        String[] titles = {
                "Họp team cuối tuần",
                "Mua quà sinh nhật mẹ",
                "Đi khám răng",
                "Gọi mẹ tối nay",
                "Hoàn thành app trước Chủ nhật",
                "Trả nợ bạn Lan 2 triệu",
                "Mua vé máy bay đi Đà Nẵng",
                "Kiểm tra hóa đơn điện tháng 11",
                "Lập kế hoạch tiết kiệm Tết 2026",
                "Ghi chú mua sắm Black Friday"
        };

        String[] contents = {
                "15h30 thứ 7, mang laptop + tài liệu dự án theo nhé",
                "Mẹ thích hoa với bánh kem, dưới 800k là được",
                "9h sáng thứ 6, đã đặt lịch bác sĩ Minh",
                "Mẹ hỏi thăm hoài mà quên gọi lại, tối nay phải gọi",
                "Còn thiếu trang ghi chú và thông báo, cố lên!",
                "Nhắc nợ 2 triệu, trả trước 25/12/2025",
                "Vé khứ hồi Vietjet 3,5 triệu, ngày 15-20/1/2026",
                "Hóa đơn 450k, đã thanh toán qua Momo",
                "Mục tiêu tiết kiệm 15 triệu cho Tết, bắt đầu tháng 12",
                "Danh sách deal tốt: iPhone, tai nghe, quần áo giảm 50%"
        };

        String[] dates = {
                "20/11/2025, 14:25",
                "19/11/2025, 10:15",
                "18/11/2025, 08:30",
                "17/11/2025, 21:40",
                "16/11/2025, 23:59",
                "15/11/2025, 17:10",
                "14/11/2025, 09:45",
                "13/11/2025, 22:30",
                "12/11/2025, 11:20",
                "11/11/2025, 20:15"
        };

        ArrayList<HashMap<String, String>> data = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("title", titles[i]);
            map.put("content", contents[i]);
            map.put("date", dates[i]);
            data.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data,
                R.layout.item_ghi_chu,
                new String[]{"title", "content", "date"},
                new int[]{R.id.tvTitle, R.id.tvContent, R.id.tvDate}
        );

        listView.setAdapter(adapter);

        // Nhấn vào 1 ghi chú
        listView.setOnItemClickListener((parent, view, position, id) ->
                Toast.makeText(this, "Đã chọn: " + titles[position], Toast.LENGTH_SHORT).show()
        );

        // Nút Back
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Nút +
        findViewById(R.id.fabAddNote).setOnClickListener(v ->
                startActivity(new Intent(this, ThemGhiChuActivity.class))
        );
    }
}