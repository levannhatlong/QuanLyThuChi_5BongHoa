package com.example.quanlythuchi_5bonghoa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "QuanLyThuChi.db";
    private static final int DATABASE_VERSION = 1;
    
    // Bảng danh mục
    private static final String TABLE_CATEGORIES = "categories";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_ICON_RES_ID = "icon_res_id";
    private static final String COLUMN_COLOR = "color";
    private static final String COLUMN_IS_EXPENSE = "is_expense";
    private static final String COLUMN_CREATED_AT = "created_at";
    private static final String COLUMN_UPDATED_AT = "updated_at";
    
    // SQL tạo bảng
    private static final String CREATE_TABLE_CATEGORIES = 
        "CREATE TABLE " + TABLE_CATEGORIES + " (" +
        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_NAME + " TEXT NOT NULL, " +
        COLUMN_DESCRIPTION + " TEXT, " +
        COLUMN_ICON_RES_ID + " INTEGER NOT NULL, " +
        COLUMN_COLOR + " TEXT NOT NULL, " +
        COLUMN_IS_EXPENSE + " INTEGER NOT NULL, " +
        COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
        COLUMN_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
        ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORIES);
        
        // Thêm dữ liệu mẫu
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        // Danh mục chi tiêu mẫu
        insertCategory(db, "Ăn uống", "Chi phí ăn uống hàng ngày", R.drawable.ic_food_modern, "#E53935", true);
        insertCategory(db, "Di chuyển", "Chi phí đi lại, xăng xe", R.drawable.ic_transport_modern, "#FB8C00", true);
        insertCategory(db, "Mua sắm", "Chi phí mua sắm", R.drawable.ic_shopping_modern, "#8E24AA", true);
        insertCategory(db, "Giải trí", "Chi phí giải trí", R.drawable.ic_entertainment_modern, "#0EA5E9", true);
        insertCategory(db, "Hóa đơn", "Điện, nước, internet", R.drawable.ic_bills_modern, "#43A047", true);
        insertCategory(db, "Y tế", "Chi phí khám chữa bệnh", R.drawable.ic_health_modern, "#EC407A", true);
        insertCategory(db, "Giáo dục", "Học phí, sách vở", R.drawable.ic_education_modern, "#1976D2", true);
        
        // Danh mục thu nhập mẫu
        insertCategory(db, "Tiền lương", "Lương hàng tháng", R.drawable.ic_salary_modern, "#43A047", false);
        insertCategory(db, "Tiền thưởng", "Thưởng, bonus", R.drawable.ic_salary_modern, "#0EA5E9", false);
        insertCategory(db, "Đầu tư", "Lợi nhuận đầu tư", R.drawable.ic_investment_modern, "#FB8C00", false);
    }

    private void insertCategory(SQLiteDatabase db, String name, String description, int iconResId, String color, boolean isExpense) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_ICON_RES_ID, iconResId);
        values.put(COLUMN_COLOR, color);
        values.put(COLUMN_IS_EXPENSE, isExpense ? 1 : 0);
        db.insert(TABLE_CATEGORIES, null, values);
    }

    // Thêm danh mục mới
    public long addCategory(String name, String description, int iconResId, String color, boolean isExpense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_ICON_RES_ID, iconResId);
        values.put(COLUMN_COLOR, color);
        values.put(COLUMN_IS_EXPENSE, isExpense ? 1 : 0);
        values.put(COLUMN_UPDATED_AT, "datetime('now')");
        
        long id = db.insert(TABLE_CATEGORIES, null, values);
        db.close();
        return id;
    }

    // Cập nhật danh mục
    public int updateCategory(int id, String name, String description, int iconResId, String color, boolean isExpense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_ICON_RES_ID, iconResId);
        values.put(COLUMN_COLOR, color);
        values.put(COLUMN_IS_EXPENSE, isExpense ? 1 : 0);
        values.put(COLUMN_UPDATED_AT, "datetime('now')");
        
        int rowsAffected = db.update(TABLE_CATEGORIES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }

    // Xóa danh mục
    public int deleteCategory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_CATEGORIES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }

    // Lấy tất cả danh mục
    public List<QuanLyDanhMucActivity.DanhMuc> getAllCategories() {
        List<QuanLyDanhMucActivity.DanhMuc> categories = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES + " ORDER BY " + COLUMN_NAME;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                QuanLyDanhMucActivity.DanhMuc category = new QuanLyDanhMucActivity.DanhMuc(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ICON_RES_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COLOR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_EXPENSE)) == 1
                );
                categories.add(category);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return categories;
    }

    // Lấy danh mục theo loại (chi tiêu/thu nhập)
    public List<QuanLyDanhMucActivity.DanhMuc> getCategoriesByType(boolean isExpense) {
        List<QuanLyDanhMucActivity.DanhMuc> categories = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES + 
                           " WHERE " + COLUMN_IS_EXPENSE + " = ? ORDER BY " + COLUMN_NAME;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{isExpense ? "1" : "0"});
        
        if (cursor.moveToFirst()) {
            do {
                QuanLyDanhMucActivity.DanhMuc category = new QuanLyDanhMucActivity.DanhMuc(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ICON_RES_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COLOR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_EXPENSE)) == 1
                );
                categories.add(category);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return categories;
    }

    // Tìm kiếm danh mục theo tên
    public List<QuanLyDanhMucActivity.DanhMuc> searchCategories(String keyword, boolean isExpense) {
        List<QuanLyDanhMucActivity.DanhMuc> categories = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES + 
                           " WHERE " + COLUMN_NAME + " LIKE ? AND " + COLUMN_IS_EXPENSE + " = ? ORDER BY " + COLUMN_NAME;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + keyword + "%", isExpense ? "1" : "0"});
        
        if (cursor.moveToFirst()) {
            do {
                QuanLyDanhMucActivity.DanhMuc category = new QuanLyDanhMucActivity.DanhMuc(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ICON_RES_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COLOR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_EXPENSE)) == 1
                );
                categories.add(category);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return categories;
    }

    // Lấy số lượng danh mục theo loại
    public int getCategoryCount(boolean isExpense) {
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_CATEGORIES + 
                          " WHERE " + COLUMN_IS_EXPENSE + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, new String[]{isExpense ? "1" : "0"});
        
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    // Kiểm tra tên danh mục đã tồn tại chưa
    public boolean isCategoryNameExists(String name, boolean isExpense, int excludeId) {
        String query = "SELECT COUNT(*) FROM " + TABLE_CATEGORIES + 
                      " WHERE " + COLUMN_NAME + " = ? AND " + COLUMN_IS_EXPENSE + " = ?";
        String[] args;
        
        if (excludeId > 0) {
            query += " AND " + COLUMN_ID + " != ?";
            args = new String[]{name, isExpense ? "1" : "0", String.valueOf(excludeId)};
        } else {
            args = new String[]{name, isExpense ? "1" : "0"};
        }
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, args);
        
        boolean exists = false;
        if (cursor.moveToFirst()) {
            exists = cursor.getInt(0) > 0;
        }
        cursor.close();
        db.close();
        return exists;
    }
}