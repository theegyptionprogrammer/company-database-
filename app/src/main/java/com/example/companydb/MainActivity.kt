package com.example.companydb

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AddBtn.setOnClickListener {
            val intent = Intent(this, AddEmployeeActivity::class.java)
            startActivity(intent)
        }

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
        val customerlist = helper.GetAllEmployees()
        val adapter = RecyclerViewAdapter(customerlist, this)
        adapter.additems(customerlist)
        val rv = findViewById<RecyclerView>(R.id.recyclerView)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rv.adapter = adapter
    }
}
