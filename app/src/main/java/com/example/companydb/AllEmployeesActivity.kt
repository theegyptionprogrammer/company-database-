package com.example.companydb

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_all_employees.*

class AllEmployeesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_employees)
        UpdateBtn.setOnClickListener {
            try {
                update()
            } catch (e: Exception) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun update() {
        val helper = SqDBHandler(this, null)
        val customerlist = helper.GetAllEmployees(helper)
        val adapter = RecyclerViewAdapter(customerlist, this)
        adapter.additems(customerlist)
        val rv = findViewById<RecyclerView>(R.id.recyclerView)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rv.adapter = adapter
    }
}