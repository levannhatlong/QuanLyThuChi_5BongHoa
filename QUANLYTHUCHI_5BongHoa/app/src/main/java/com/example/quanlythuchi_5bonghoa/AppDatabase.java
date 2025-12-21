package com.example.quanlythuchi_5bonghoa;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.quanlythuchi_5bonghoa.daos.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {NguoiDung.class, DanhMuc.class, GiaoDich.class, LienKetNganHang.class, CanhBaoChiTieu.class, ThongBao.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract NguoiDungDao nguoiDungDao();
    public abstract DanhMucDao danhMucDao();
    public abstract GiaoDichDao giaoDichDao();
    public abstract LienKetNganHangDao lienKetNganHangDao();
    public abstract CanhBaoChiTieuDao canhBaoChiTieuDao();
    public abstract ThongBaoDao thongBaoDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "5BongHoa_QLthuchi")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                NguoiDungDao nguoiDungDao = INSTANCE.nguoiDungDao();
                DanhMucDao danhMucDao = INSTANCE.danhMucDao();
                GiaoDichDao giaoDichDao = INSTANCE.giaoDichDao();
                LienKetNganHangDao lienKetNganHangDao = INSTANCE.lienKetNganHangDao();
                CanhBaoChiTieuDao canhBaoChiTieuDao = INSTANCE.canhBaoChiTieuDao();
                ThongBaoDao thongBaoDao = INSTANCE.thongBaoDao();

                NguoiDung nguoiDung = new NguoiDung();
                nguoiDung.TenDangNhap = "5bonghoa";
                nguoiDung.MatKhau = "pass123";
                nguoiDung.HoTen = "5 Bông Hoa";
                nguoiDung.EmailSoDienThoai = "5bonghoa@gmail.com";
                nguoiDung.NgaySinh = "2001-08-30";
                nguoiDungDao.insert(nguoiDung);

                // Insert initial data here based on your SQL script

            });
        }
    };
}
