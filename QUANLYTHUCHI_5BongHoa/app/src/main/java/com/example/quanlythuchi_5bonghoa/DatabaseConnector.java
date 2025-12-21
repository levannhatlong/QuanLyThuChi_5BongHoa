package com.example.quanlythuchi_5bonghoa;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String IP = "your_server_ip"; // Thay thế bằng IP của máy chủ SQL Server
    private static final String PORT = "1433";
    private static final String DATABASE = "5BongHoa_QLthuchi";
    private static final String USERNAME = "your_username"; // Thay thế bằng tên đăng nhập SQL Server
    private static final String PASSWORD = "your_password"; // Thay thế bằng mật khẩu SQL Server

    private static final String TAG = "DatabaseConnector";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Bắt buộc đối với các phiên bản Android mới hơn để cho phép các hoạt động mạng trên luồng chính.
            // Đây là một thực hành không tốt, bạn nên sử dụng AsyncTask hoặc một luồng nền khác.
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // Tải trình điều khiển JDBC
            Class.forName("net.sourceforge.jtds.jdbc.Driver");

            // Tạo chuỗi kết nối
            String connectionUrl = "jdbc:jtds:sqlserver://" + IP + ":" + PORT + "/" + DATABASE;

            // Kết nối đến cơ sở dữ liệu
            connection = DriverManager.getConnection(connectionUrl, USERNAME, PASSWORD);
            Log.d(TAG, "Kết nối đến SQL Server thành công!");

        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Lỗi: Không tìm thấy trình điều khiển JDBC.", e);
        } catch (SQLException e) {
            Log.e(TAG, "Lỗi kết nối đến SQL Server.", e);
        }
        return connection;
    }
}
