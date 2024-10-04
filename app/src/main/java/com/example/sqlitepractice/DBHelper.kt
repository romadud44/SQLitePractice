package com.example.sqlitepractice

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "PRODUCTS_DATABASE"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "product_table"
        const val KEY_WEIGHT = "weight"
        const val KEY_NAME = "name"
        const val KEY_PRICE = "price"
        const val KEY_ID = "id"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val PRODUCT_TABLE = ("CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " TEXT, " +
                KEY_WEIGHT + " TEXT, " +
                KEY_PRICE + " TEXT" + ")")
        db?.execSQL(PRODUCT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addProduct(product: Product) {
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, product.productId)
        contentValues.put(KEY_NAME, product.productName)
        contentValues.put(KEY_WEIGHT, product.productWeight)
        contentValues.put(KEY_PRICE, product.productPrice)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    fun getInfo(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun removeAll() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
    }

    @SuppressLint("Range", "Recycle")
    fun readProduct(): MutableList<Product> {
        val productList: MutableList<Product> = mutableListOf()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return productList
        }
        var productId: Int
        var productName: String
        var productWeight: Int
        var productPrice: Int
        if (cursor.moveToFirst()) {
            do {
                productId = cursor.getInt(cursor.getColumnIndex("id"))
                productName = cursor.getString(cursor.getColumnIndex("name"))
                productWeight = cursor.getInt(cursor.getColumnIndex("weight"))
                productPrice = cursor.getInt(cursor.getColumnIndex("price"))
                val product = Product(
                    productId = productId,
                    productName = productName,
                    productWeight = productWeight,
                    productPrice = productPrice
                )
                productList.add(product)
            } while (cursor.moveToNext())
        }
        return productList
    }

    fun updateProduct(product: Product) {
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, product.productId)
        contentValues.put(KEY_NAME, product.productName)
        contentValues.put(KEY_WEIGHT, product.productWeight)
        contentValues.put(KEY_PRICE, product.productPrice)
        val db = this.writableDatabase
        db.update(TABLE_NAME, contentValues, "id=" + product.productId, null)
        db.close()
    }

    fun deleteProduct(product: Product) {
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, product.productId)
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "id=" + product.productId, null)
        db.close()
    }
}