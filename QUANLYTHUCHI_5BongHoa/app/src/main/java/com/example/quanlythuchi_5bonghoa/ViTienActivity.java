package com.example.quanlythuchi_5bonghoa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViTienActivity extends AppCompatActivity {

    private TextView tvSoDu, tvThuNhap, tvChiTieu, tvXemThem, tvTenNganHang, tvSoTaiKhoan;
    private LinearProgressIndicator progressBalance;
    private RecyclerView recyclerViewRecentTransactions;
    private FloatingActionButton fabHome, fabAdd;
    private ImageView ivToggleBalance;

    private GiaoDichAdapter giaoDichAdapter;
    private List<GiaoDich> recentTransactionList;

    private int currentUserId;
    private boolean isBalanceVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vi_tien);

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
        tvSoDu = findViewById(R.id.tv_so_du);
        tvThuNhap = findViewById(R.id.tv_thu_nhap);
        tvChiTieu = findViewById(R.id.tv_chi_tieu);
        progressBalance = findViewById(R.id.progress_balance);
        ivToggleBalance = findViewById(R.id.iv_toggle_balance);
        recyclerViewRecentTransactions = findViewById(R.id.recycler_view_recent_transactions);
        fabHome = findViewById(R.id.fab_home);
        fabAdd = findViewById(R.id.fab_add);
        tvXemThem = findViewById(R.id.tv_xem_them);
        tvTenNganHang = findViewById(R.id.tv_ten_ngan_hang);
        tvSoTaiKhoan = findViewById(R.id.tv_so_tai_khoan);
    }



    private void displayData() {
        // This method is called from updateUI, so we don't need to implement it separately
        // The display logic is handled in updateUI method
    }

    private void setupRecyclerView() {
        recyclerViewRecentTransactions.setLayoutManager(new LinearLayoutManager(this));
        recentTransactionList = new ArrayList<>();
        giaoDichAdapter = new GiaoDichAdapter(this, recentTransactionList);
        recyclerViewRecentTransactions.setAdapter(giaoDichAdapter);
    }

    private void setupClickListeners() {
        fabHome.setOnClickListener(v -> {
            Intent intent = new Intent(ViTienActivity.this, TrangChuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        // FAB thêm giao dịch nhanh
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(ViTienActivity.this, ThemGiaoDichActivity.class);
            startActivity(intent);
        });

        ivToggleBalance.setOnClickListener(v -> {
            isBalanceVisible = !isBalanceVisible;
            loadDataFromDatabase(); // Reload to apply visibility change
        });

        tvXemThem.setOnClickListener(v -> {
            // Navigate to the full statistics screen
            Intent intent = new Intent(ViTienActivity.this, ThongKeActivity.class);
            startActivity(intent);
        });
    }

    private void loadDataFromDatabase() {
        new Thread(() -> {
            Connection connection = DatabaseConnector.getConnection();
            if (connection == null) {
                runOnUiThread(() -> Toast.makeText(ViTienActivity.this, "Không thể kết nối database", Toast.LENGTH_SHORT).show());
                return;
            }

            double totalIncome = 0;
            double totalExpense = 0;
            List<GiaoDich> transactions = new ArrayList<>();
            String tenNganHang = "";
            String soTaiKhoan = "";

            try {
                // 1. Calculate total income and expense
                String summaryQuery = "SELECT d.LoaiDanhMuc, SUM(g.SoTien) as TongSoTien " +
                                    "FROM GiaoDich g JOIN DanhMuc d ON g.MaDanhMuc = d.MaDanhMuc " +
                                    "WHERE g.MaNguoiDung = ? " +
                                    "GROUP BY d.LoaiDanhMuc";
                PreparedStatement summaryStmt = connection.prepareStatement(summaryQuery);
                summaryStmt.setInt(1, currentUserId);
                ResultSet summaryRs = summaryStmt.executeQuery();

                while (summaryRs.next()) {
                    String loai = summaryRs.getString("LoaiDanhMuc");
                    double total = summaryRs.getDouble("TongSoTien");
                    if ("Thu nhập".equals(loai)) {
                        totalIncome = total;
                    } else if ("Chi tiêu".equals(loai)) {
                        totalExpense = total;
                    }
                }
                summaryRs.close();
                summaryStmt.close();

                // 2. Fetch recent transactions (top 3)
                String recentQuery = "SELECT TOP 3 g.TenGiaoDich, g.SoTien, g.NgayGiaoDich, d.TenDanhMuc, d.LoaiDanhMuc, d.BieuTuong " +
                                   "FROM GiaoDich g JOIN DanhMuc d ON g.MaDanhMuc = d.MaDanhMuc " +
                                   "WHERE g.MaNguoiDung = ? ORDER BY g.NgayGiaoDich DESC";
                PreparedStatement recentStmt = connection.prepareStatement(recentQuery);
                recentStmt.setInt(1, currentUserId);
                ResultSet recentRs = recentStmt.executeQuery();

                while (recentRs.next()) {
                    String tenGiaoDich = recentRs.getString("TenGiaoDich");
                    double soTien = recentRs.getDouble("SoTien");
                    Date ngayGiaoDich = recentRs.getTimestamp("NgayGiaoDich");
                    String tenDanhMuc = recentRs.getString("TenDanhMuc");
                    String loaiDanhMuc = recentRs.getString("LoaiDanhMuc");
                    String bieuTuong = recentRs.getString("BieuTuong");
                    transactions.add(new GiaoDich(tenGiaoDich, soTien, ngayGiaoDich, tenDanhMuc, loaiDanhMuc, bieuTuong));
                }
                recentRs.close();
                recentStmt.close();

                // 3. Fetch bank account info
                String bankQuery = "SELECT TOP 1 TenNganHang, SoTaiKhoan FROM LienKetNganHang WHERE MaNguoiDung = ?";
                PreparedStatement bankStmt = connection.prepareStatement(bankQuery);
                bankStmt.setInt(1, currentUserId);
                ResultSet bankRs = bankStmt.executeQuery();

                if (bankRs.next()) {
                    tenNganHang = bankRs.getString("TenNganHang");
                    soTaiKhoan = bankRs.getString("SoTaiKhoan");
                }
                bankRs.close();
                bankStmt.close();

            } catch (SQLException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(ViTienActivity.this, "Lỗi khi tải dữ liệu.", Toast.LENGTH_SHORT).show());
            } finally {
                try {
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            double finalTotalIncome = totalIncome;
            double finalTotalExpense = totalExpense;
            String finalTenNganHang = tenNganHang;
            String finalSoTaiKhoan = soTaiKhoan;

            runOnUiThread(() -> {
                updateUI(finalTotalIncome, finalTotalExpense, transactions);
                updateBankInfo(finalTenNganHang, finalSoTaiKhoan);
            });
        }).start();
    }

    private void updateUI(double income, double expense, List<GiaoDich> transactions) {
        double balance = income - expense;
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        if (isBalanceVisible) {
            tvSoDu.setText(formatter.format(balance));
        } else {
            tvSoDu.setText("*** *** *** VND");
        }

        tvThuNhap.setText("+" + formatter.format(income));
        tvChiTieu.setText("-" + formatter.format(expense));

        double totalTransactions = income + expense;
        if (totalTransactions > 0) {
            int incomePercentage = (int) ((income * 100) / totalTransactions);
            progressBalance.setProgress(incomePercentage);
        } else {
            progressBalance.setProgress(0);
        }

        // Update RecyclerView
        recentTransactionList.clear();
        recentTransactionList.addAll(transactions);
        giaoDichAdapter.notifyDataSetChanged();
    }

    private void updateBankInfo(String tenNganHang, String soTaiKhoan) {
        if (tenNganHang != null && !tenNganHang.isEmpty()) {
            tvTenNganHang.setText(tenNganHang);
            // Ẩn 4 số cuối tài khoản
            if (soTaiKhoan != null && soTaiKhoan.length() > 4) {
                String masked = "**** **** " + soTaiKhoan.substring(soTaiKhoan.length() - 4);
                tvSoTaiKhoan.setText(masked);
            } else {
                tvSoTaiKhoan.setText(soTaiKhoan);
            }
        } else {
            tvTenNganHang.setText("Chưa liên kết");
            tvSoTaiKhoan.setText("Nhấn để thêm tài khoản");
        }
    }
}
