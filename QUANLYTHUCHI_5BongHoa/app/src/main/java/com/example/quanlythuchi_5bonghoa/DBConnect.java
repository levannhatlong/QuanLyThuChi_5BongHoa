package com.example.quanlythuchi_5bonghoa;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    private static final String IP = "your_server_ip";
    private static final String PORT = "1433";
    private static final String DATABASE = "5BongHoa_QLthuchi";
    private static final String USERNAME = "sq";
    private static final String PASSWORD = "123";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // It's important to run network operations off the main thread.
            // For simplicity in this example, we are allowing it on the main thread,
            // but in a real application, you should use AsyncTask, Coroutines, or another background thread mechanism.
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connectionUrl = "jdbc:jtds:sqlserver://" + IP + ":" + PORT + "/" + DATABASE + ";user=" + USERNAME + ";password=" + PASSWORD + ";";
            connection = DriverManager.getConnection(connectionUrl);
        } catch (ClassNotFoundException | SQLException e) {
            Log.e("DBConnect", "Error connecting to database: " + e.getMessage());
        }
        return connection;
    }
}
