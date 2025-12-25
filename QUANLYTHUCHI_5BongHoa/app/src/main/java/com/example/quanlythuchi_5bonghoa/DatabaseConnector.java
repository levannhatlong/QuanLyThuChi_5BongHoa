package com.example.quanlythuchi_5bonghoa;

import android.os.StrictMode;
import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnector {
    private static final String IP = "192.168.0.116";
    private static final String PORT = "1433";
    private static final String DATABASE = "5BongHoa_QLthuchi";

    private static final String USER = "sa";

    private static final String PASS = "Nl123@";


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