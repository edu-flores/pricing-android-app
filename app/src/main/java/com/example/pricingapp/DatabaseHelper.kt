package com.example.pricingapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "QuoteDatabase.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY = "CREATE TABLE quotes (id INTEGER PRIMARY KEY AUTOINCREMENT, fromPlace TEXT, toPlace TEXT, price INTEGER, transit TEXT)"
        db?.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS quotes")
        onCreate(db)
    }

    fun addQuote(fromPlace: String, toPlace: String, price: Int, transit: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("toPlace", fromPlace)
        contentValues.put("fromPlace", toPlace)
        contentValues.put("price", price)
        contentValues.put("transit", transit)

        val result = db.insert("quotes", null, contentValues)
        db.close()

        return result != -1L
    }

    fun getAllQuotes(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM quotes", null)
    }

    fun updateQuote(id: Int, fromPlace: String, toPlace: String, price: Int, transit: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("toPlace", fromPlace)
        contentValues.put("fromPlace", toPlace)
        contentValues.put("price", price)
        contentValues.put("transit", transit)

        val result = db.update("quotes", contentValues, "id = ?", arrayOf(id.toString()))
        db.close()

        return result > 0
    }

    fun deleteQuote(id: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete("quotes", "id = ?", arrayOf(id.toString()))
        db.close()

        return result > 0
    }
}