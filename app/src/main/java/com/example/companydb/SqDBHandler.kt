package com.example.companydb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import java.util.*

class SqDBHandler(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        val DATABASE_NAME = "KIRA_DB"
        val DATABASE_VERSION = 1
        val TABLE_NAME = "entry"
        val COLUMN_NAME = "name"
        val COLUMN_ADDRESS = "address"
        val COLUMN_POSITION = "position"
        val COLUMN_ID = "id"
    }

    private val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${COLUMN_NAME} TEXT," +
                "${COLUMN_ADDRESS} TEXT," +
                "${COLUMN_POSITION} TEXT," +
                "${COLUMN_ID} INT"

    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    val db: SqDBHandler? = null

    fun AddEmployee(employee: Employee): Boolean {
        val dbHelper = db?.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, employee.Name)
            put(COLUMN_ADDRESS, employee.Address)
            put(COLUMN_POSITION, employee.position)
            put(COLUMN_ID, employee.id)
        }
        dbHelper?.insert(TABLE_NAME, null, values)
        dbHelper?.close()
        return true
    }

    fun GetAllEmployees(): ArrayList<String> {
        val listEmployees = ArrayList<String>()
        val dbHelper = db?.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = dbHelper?.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    var name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                    var address = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS))
                    var position = cursor.getString(cursor.getColumnIndex(COLUMN_POSITION))
                    var id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))).toDouble()
                    var result = "$name  \n $address \n $position \n $id"
                    listEmployees.add(result)
                }
            }
        }
        dbHelper?.close()
        return listEmployees
    }

    fun searchEmployee(id: Int): ArrayList<String> {
        val listEmployees = ArrayList<String>()
        val dbHelper = db?.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $id "
        val cursor = dbHelper?.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                    val address = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS))
                    val position = cursor.getString(cursor.getColumnIndex(COLUMN_POSITION))
                    val id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))).toDouble()
                    var result = "$name  \n $address \n $position \n $id"
                    listEmployees.add(result)
                }
            }
        }
        dbHelper?.close()
        return listEmployees
    }

    fun deleteAllEmployees(): Boolean {
        val dbHelper = db?.writableDatabase
        val result = dbHelper?.delete(TABLE_NAME, null, null)?.toLong()
        dbHelper?.close()
        return (Integer.parseInt("$result")) != -1
    }

    fun deleteEmployee(employee: Employee): Boolean {
        val dbHelper = db?.writableDatabase
        val id = employee.id
        val result = dbHelper?.delete(TABLE_NAME, COLUMN_ID + "=?", arrayOf(id.toString()))?.toLong()
        dbHelper?.close()
        return Integer.parseInt("$result") != -1
    }


}