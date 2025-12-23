package com.example.quanlythuchi_5bonghoa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TrangChuActivity extends AppCompatActivity {

    private TextView tvGreeting, tvTienChi, tvTienThu, tvSoDu, tvChiTiet;
    private LineChart lineChart;
    private LinearLayout btnViTien, btnThemGiaoDich, btnThongKe;
    private RecyclerView recyclerViewGiaoDich;
    private ImageView ivNotification, ivSettings, ivGhichu;

    private GiaoDichAdapter giaoDichAdapter;
    private List<GiaoDich> danhSachGiaoDich;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("user_id", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Lỗi: không tìm thấy người dùng.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        initViews();
        setupRecyclerView();
        setupClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromDatabase();
    }

    private void initViews() {
        tvGreeting = findViewById(R.id.tv_greeting);
        tvTienChi = findViewById(R.id.tv_tien_chi);
        tvTienThu = findViewById(R.id.tv_tien_thu);
        tvSoDu = findViewById(R.id.tv_so_du);
        tvChiTiet = findViewById(R.id.tv_chi_tiet);
        lineChart = findViewById(R.id.line_chart);
        btnViTien = findViewById(R.id.btn_vi_tien);
        btnThemGiaoDich = findViewById(R.id.btn_them_giao_dich);
        btnThongKe = findViewById(R.id.btn_thong_ke);
        recyclerViewGiaoDich = findViewById(R.id.recycler_view_giao_dich);
        ivNotification = findViewById(R.id.iv_notification);
        ivSettings = findViewById(R.id.iv_settings);
        ivGhichu = findViewById(R.id.iv_ghichu);
    }

    private void setupRecyclerView() {
        recyclerViewGiaoDich.setLayoutManager(new LinearLayoutManager(this));
        danhSachGiaoDich = new ArrayList<>();
        giaoDichAdapter = new GiaoDichAdapter(this, danhSachGiaoDich);
        recyclerViewGiaoDich.setAdapter(giaoDichAdapter);
    }

    private void setupClickListeners() {
        btnViTien.setOnClickListener(v -> startActivity(new Intent(this, ViTienActivity.class)));
        btnThemGiaoDich.setOnClickListener(v -> startActivity(new Intent(this, ThemGiaoDichActivity.class)));
        btnThongKe.setOnClickListener(v -> startActivity(new Intent(this, ThongKeActivity.class)));
        ivNotification.setOnClickListener(v -> startActivity(new Intent(this, ThongBaoActivity.class)));
        ivGhichu.setOnClickListener(v -> startActivity(new Intent(this, GhiChuActivity.class)));
        ivSettings.setOnClickListener(v -> startActivity(new Intent(this, CaiDatActivity.class)));
        
        // Click "Xem tất cả" để xem danh sách giao dịch đầy đủ
        tvChiTiet.setOnClickListener(v -> startActivity(new Intent(this, ThongKeActivity.class)));
    }

    private void loadDataFromDatabase() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        
        // Lấy thông tin người dùng
        dbHelper.getUserInfo(currentUserId, new DatabaseHelper.DataCallback<NguoiDung>() {
            @Override
            public void onSuccess(NguoiDung user) {
                // Lấy giao dịch sau khi có thông tin người dùng
                loadTransactions(dbHelper, user.getHoTen());
            }

            @Override
            public void onError(String error) {
                // Fallback với tên mặc định
                loadTransactions(dbHelper, "Người dùng");
            }
        });
    }

    private void loadTransactions(DatabaseHelper dbHelper, String userName) {
        dbHelper.getGiaoDich(currentUserId, "", new DatabaseHelper.DataCallback<List<GiaoDich>>() {
            @Override
            public void onSuccess(List<GiaoDich> transactions) {
                runOnUiThread(() -> {
                    double totalIncome = 0;
                    double totalExpense = 0;
                    List<GiaoDich> recentTransactions = new ArrayList<>();
                    ArrayList<Entry> incomeEntries = new ArrayList<>();
                    ArrayList<Entry> expenseEntries = new ArrayList<>();
                    
                    int incomeIndex = 0;
                    int expenseIndex = 0;
                    
                    for (int i = 0; i < transactions.size(); i++) {
                        GiaoDich gd = transactions.get(i);
                        
                        if (i < 5) { // Lấy 5 giao dịch gần nhất
                            recentTransactions.add(gd);
                        }
                        
                        if ("Thu nhập".equals(gd.getLoaiDanhMuc())) {
                            totalIncome += gd.getSoTien();
                            incomeEntries.add(new Entry(incomeIndex++, (float) gd.getSoTien()));
                        } else {
                            totalExpense += gd.getSoTien();
                            expenseEntries.add(new Entry(expenseIndex++, (float) gd.getSoTien()));
                        }
                    }
                    
                    updateUI(userName, totalIncome, totalExpense, recentTransactions);
                    setupLineChart(incomeEntries, expenseEntries);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(TrangChuActivity.this, error, Toast.LENGTH_SHORT).show();
                    updateUI(userName, 0, 0, new ArrayList<>());
                    setupLineChart(new ArrayList<>(), new ArrayList<>());
                });
            }
        });
    }

    private void updateUI(String userName, double income, double expense, List<GiaoDich> transactions) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        tvGreeting.setText(userName != null && !userName.isEmpty() ? userName : "Người dùng");
        tvTienThu.setText(formatter.format(income));
        tvTienChi.setText(formatter.format(expense));
        tvSoDu.setText(formatter.format(income - expense));

        danhSachGiaoDich.clear();
        danhSachGiaoDich.addAll(transactions);
        giaoDichAdapter.notifyDataSetChanged();
    }

    private void setupLineChart(ArrayList<Entry> incomeEntries, ArrayList<Entry> expenseEntries) {
        LineDataSet incomeDataSet = new LineDataSet(incomeEntries, "Thu");
        incomeDataSet.setColor(Color.parseColor("#43A047"));
        incomeDataSet.setLineWidth(2f);
        incomeDataSet.setCircleColor(Color.parseColor("#43A047"));
        incomeDataSet.setDrawValues(false);

        LineDataSet expenseDataSet = new LineDataSet(expenseEntries, "Chi");
        expenseDataSet.setColor(Color.parseColor("#E53935"));
        expenseDataSet.setLineWidth(2f);
        expenseDataSet.setCircleColor(Color.parseColor("#E53935"));
        expenseDataSet.setDrawValues(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(incomeDataSet);
        dataSets.add(expenseDataSet);

        LineData lineData = new LineData(dataSets);

        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(true);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setEnabled(false);

        lineChart.invalidate(); // refresh
    }
}
