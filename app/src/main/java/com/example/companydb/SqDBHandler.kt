package com.example.companydb

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import java.util.*

@Suppress("NAME_SHADOWING")
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
                "${COLUMN_ID} INTEGER )"

    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    var db: SqDBHandler? = null


    fun AddEmployee(employee: Employee, handler: SqDBHandler): Boolean {
        db = handler
        val dbHandler = db?.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, employee.Name)
            put(COLUMN_ADDRESS, employee.Address)
            put(COLUMN_POSITION, employee.position)
            put(COLUMN_ID, employee.id)
        }
        dbHandler?.insert(TABLE_NAME, null, values)
        dbHandler?.close()
        return true
    }

    @SuppressLint("Recycle")
    fun GetAllEmployees(handler: SqDBHandler): ArrayList<String> {
        val listEmployees = ArrayList<String>()
        db = handler
        val dbHandler = db?.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = dbHandler?.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                    val address = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS))
                    val position = cursor.getString(cursor.getColumnIndex(COLUMN_POSITION))
                    val id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))).toDouble()
                    val result = "$name  \n $address \n $position \n $id"
                    listEmployees.add(result)
                }
            }
        }
        dbHandler?.close()
        return listEmployees
    }

    fun searchEmployee(id: Int, handler: SqDBHandler): ArrayList<String> {
        val listEmployees = ArrayList<String>()
        db = handler
        val dbHandler = db?.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $id "
        val cursor = dbHandler?.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                    val address = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS))
                    val position = cursor.getString(cursor.getColumnIndex(COLUMN_POSITION))
                    val id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))).toDouble()
                    val result = "$name  \n $address \n $position \n $id"
                    listEmployees.add(result)
                }
            }
        }
        dbHandler?.close()
        return listEmployees
    }

    fun deleteAllEmployees(): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, null, null).toLong()
        db.close()
        return (Integer.parseInt("$result")) != -1
    }

    fun deleteEmployee(employee: Employee): Boolean {
        val db = this.writableDatabase
        val id = employee.id
        val result = db.delete(TABLE_NAME, COLUMN_ID + "=?", arrayOf(id.toString())).toLong()
        db.close()
        return Integer.parseInt("$result") != -1

    }


}