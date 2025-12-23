package com.example.quanlythuchi_5bonghoa;

import android.os.StrictMode;
import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnector {
    // 10.0.2.2 là IP mặc định để máy ảo Android hiểu đó là "localhost" của máy tính bạn
    private static final String IP = "10.0.2.2";
    private static final String PORT = "1433";
    private static final String DATABASE = "5BongHoa_QLthuchi";

    // Lưu ý: Android bắt buộc phải có User/Pass của SQL Server để xác thực từ xa
    private static final String USER = "sa";
<<<<<<< HEAD
    private static final String PASS = "123";
=======
<<<<<<< HEAD
    private static final String PASS = "Nl123@";
=======
    private static final String PASS = "kin2112005";
>>>>>>> HoThiMyHa
>>>>>>> 1ee33c8ca1ac369a9ddd4b55a3b94b5f81ef69a4

    public static Connection getConnection() {
        Connection conn = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String url = "jdbc:jtds:sqlserver://" + IP + ":" + PORT + "/" + DATABASE;

            conn = DriverManager.getConnection(url, USER, PASS);
            Log.d("SQL", "Kết nối thành công!");
        } catch (Exception e) {
            Log.e("SQL", "Lỗi kết nối: " + e.getMessage());
        }
        return conn;
    }
}