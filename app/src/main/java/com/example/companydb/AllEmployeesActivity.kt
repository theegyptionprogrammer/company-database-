package com.example.companydb

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.helper.ItemTouchHelper.*
import android.widget.LinearLayout
import android.widget.SimpleAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_all_employees.*

class AllEmployeesActivity : AppCompatActivity() {

    private var helper : SqDBHandler ?= null
    private var adapter : RecyclerViewAdapter ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_employees)
        UpdateBtn.setOnClickListener { update() }
        DeleteBtn.setOnClickListener{ clearAll()
        UpdateBtn.performClick()}

        val swipeHandler = object : SwipeToDeleteCallBack(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter = recyclerView.adapter as RecyclerViewAdapter
                adapter!!.removeItem(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun update() {
        helper = SqDBHandler(this, null)
        val customerlist = helper!!.GetAllEmployees(helper!!)
        adapter = RecyclerViewAdapter(customerlist, this)
        val rv = findViewById<RecyclerView>(R.id.recyclerView)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rv.adapter = adapter
    }

    fun clearAll(){
        val helper = SqDBHandler(this, null)
        helper.deleteAllEmployees()
    }

}