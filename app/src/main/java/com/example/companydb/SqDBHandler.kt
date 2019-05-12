package com.example.companydb

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

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


    fun AddEmployee(employee: Employee){

        val dbHandler = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, employee.name)
            put(COLUMN_ADDRESS, employee.address)
            put(COLUMN_POSITION, employee.position)
            put(COLUMN_ID, employee.id)
        }
        dbHandler?.insert(TABLE_NAME, null, values)
        dbHandler?.close()

    }

    @SuppressLint("Recycle")
    fun GetAllEmployees(handler: SqDBHandler): ArrayList<Employee> {
        val listEmployees = ArrayList<Employee>()
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
                    val id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID)))
                    val employee = Employee(name , address , position , id)
                    listEmployees.add(employee)
                }
            }
        }
        dbHandler?.close()
        return listEmployees
    }

    @SuppressLint("Recycle")
    fun searchEmployee(searchWord : String): ArrayList<Employee>{
        val ListEmployee = ArrayList<Employee>()
        val dbHandler = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_NAME = $searchWord"
        var cursor = dbHandler!!.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                    val address = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS))
                    val position = cursor.getString(cursor.getColumnIndex(COLUMN_POSITION))
                    val id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))).toInt()
                    ListEmployee.add(Employee(name , address , position , id))
                    cursor.close()
                }
            }
        }
        dbHandler.close()
        return ListEmployee
    }

    fun deleteAllEmployees() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }
}