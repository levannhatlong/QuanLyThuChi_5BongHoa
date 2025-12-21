package com.example.quanlythuchi_5bonghoa;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ThongKeActivity extends AppCompatActivity {

    private PieChart pieChart;
    private RecyclerView recyclerView;
    private GiaoDichAdapter giaoDichAdapter;
    private ImageView ivBack;
    private MaterialButtonToggleGroup toggleButtonGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        pieChart = findViewById(R.id.piechart);
        recyclerView = findViewById(R.id.recycler_view_giao_dich);
        ivBack = findViewById(R.id.iv_back);
        toggleButtonGroup = findViewById(R.id.toggle_button_group);

        setupRecyclerView();
        ivBack.setOnClickListener(v -> finish());

        // Bắt đầu quá trình lấy dữ liệu từ SQL Server
        new FetchGiaoDichTask().execute();

        toggleButtonGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btn_ngay) {
                    updateDataFor("ngay");
                } else if (checkedId == R.id.btn_thang) {
                    updateDataFor("thang");
                } else if (checkedId == R.id.btn_nam) {
                    updateDataFor("nam");
                }
            }
        });

        toggleButtonGroup.check(R.id.btn_thang);
    }

    private void setupPieChart(List<GiaoDich> giaoDichs) {
        float tongThu = 0;
        float tongChi = 0;

        for (GiaoDich gd : giaoDichs) {
            // Cần logic để xác định giao dịch là thu hay chi, tạm thời giả định
            if (gd.getTenGiaoDich().toLowerCase().contains("lương")) { // Ví dụ: nếu tên có chữ "lương" thì là thu
                gd.TienVao = true;
            }

            if (gd.isTienVao()) {
                tongThu += gd.getSoTien();
            } else {
                tongChi += gd.getSoTien();
            }
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(tongChi, "Tổng chi"));
        entries.add(new PieEntry(tongThu, "Tổng thu"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(new int[]{Color.RED, Color.GREEN});
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.invalidate(); // refresh
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        giaoDichAdapter = new GiaoDichAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(giaoDichAdapter);
    }

    private void updateDataFor(String filter) {
        Toast.makeText(this, "Lọc theo: " + filter, Toast.LENGTH_SHORT).show();
    }

    // Lớp nội để thực hiện tác vụ mạng trên luồng nền
    private class FetchGiaoDichTask extends AsyncTask<Void, Void, List<GiaoDich>> {
        @Override
        protected List<GiaoDich> doInBackground(Void... voids) {
            List<GiaoDich> giaoDichs = new ArrayList<>();
            Connection connection = DatabaseConnector.getConnection();

            if (connection == null) {
                return null; // Trả về null nếu kết nối thất bại
            }

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT TenGiaoDich, SoTien FROM GiaoDich")) {

                while (resultSet.next()) {
                    String tenGiaoDich = resultSet.getString("TenGiaoDich");
                    double soTien = resultSet.getDouble("SoTien");
                    giaoDichs.add(new GiaoDich(tenGiaoDich, soTien, false)); // Tạm thời mặc định là tiền chi (false)
                }
                return giaoDichs;
            } catch (Exception e) {
                Log.e("FetchGiaoDichTask", "Lỗi khi lấy dữ liệu", e);
                return null;
            } finally {
                try {
                    connection.close();
                } catch (Exception e) {
                    Log.e("FetchGiaoDichTask", "Lỗi khi đóng kết nối", e);
                }
            }
        }

        @Override
        protected void onPostExecute(List<GiaoDich> result) {
            if (result != null) {
                Log.d("FetchGiaoDichTask", "Lấy thành công " + result.size() + " giao dịch.");
                giaoDichAdapter.setGiaoDichs(result);
                setupPieChart(result);
            } else {
                Log.e("FetchGiaoDichTask", "Lấy dữ liệu thất bại hoặc không có dữ liệu.");
                Toast.makeText(ThongKeActivity.this, "Lỗi kết nối cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
