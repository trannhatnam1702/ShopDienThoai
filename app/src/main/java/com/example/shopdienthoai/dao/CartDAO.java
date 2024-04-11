package com.example.shopdienthoai.dao;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.shopdienthoai.database.DbHelper;
import com.example.shopdienthoai.model.Cart;

import java.util.ArrayList;

public class CartDAO {

    private final DbHelper dbHelper;
    public CartDAO(Context context){
        dbHelper = new DbHelper(context);
    }

    public ArrayList<Cart> getCartItems() {
        ArrayList<Cart> cartItems = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        database.beginTransaction();
        try {
            Cursor cursor = database.rawQuery("SELECT * FROM cart", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    cartItems.add(new Cart(cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getDouble(4),
                            cursor.getInt(5)));
                } while (cursor.moveToNext());
                database.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.e(TAG, "getCartItems" + e);
        } finally {
            database.endTransaction();
        }
        return cartItems;
    }

    // Hàm thêm sản phẩm vào giỏ hàng
    public boolean addToCart(Cart cartItem) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("productId", cartItem.getProductId());
        values.put("name", cartItem.getName());
        values.put("slug", cartItem.getSlug());
        values.put("price", cartItem.getPrice());
        values.put("quantity", cartItem.getQuantity());
        long check = database.insert("cart", null, values);
        return check != -1;
    }

    // Hàm xóa sản phẩm từ giỏ hàng
    public boolean removeFromCart(int cartItemId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int affectedRows = db.delete("cart", "id=?", new String[]{String.valueOf(cartItemId)});
        return affectedRows > 0;
    }

    public void removeAllFromCart() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("cart", null, null);
        db.close();
    }

    // Hàm cập nhật số lượng sản phẩm trong giỏ hàng
    public boolean updateCartItemQuantity(int cartItemId, int newQuantity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("quantity", newQuantity);

        int rowsAffected = db.update("cart", cv, "id=?", new String[]{String.valueOf(cartItemId)});
        db.close();

        return rowsAffected > 0;
    }
    public boolean updateCartItemPrice(int cartItemId, float newPrice) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("price", newPrice);

        int rowsAffected = db.update("cart", cv, "id=?", new String[]{String.valueOf(cartItemId)});
        db.close();

        return rowsAffected > 0;
    }
}
