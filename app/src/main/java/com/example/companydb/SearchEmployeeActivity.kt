package com.example.companydb

import android.app.DownloadManager
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Filter
import android.widget.LinearLayout
import android.widget.SearchView


class SearchEmployeeActivity : AppCompatActivity() , RecyclerViewAdapter.ClickListner{

    private var adapter : RecyclerViewAdapter ?= null
    private var searchView : SearchView ?= null
    private var mainLayout: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_employee)

        val helper = SqDBHandler(this , null)
        val rv = findViewById<RecyclerView>(R.id.myRecycler)
        val employeeList = helper.GetAllEmployees(helper)

        adapter = RecyclerViewAdapter(employeeList , this)
        rv.layoutManager = LinearLayoutManager(this , LinearLayout.VERTICAL , false)
        rv.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu , menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView!!.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView!!.maxWidth = Integer.MAX_VALUE
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter?.filter?.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter?.filter?.filter(newText)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        return if (id == R.id.action_search) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onClickListner(employee: Employee){
        Snackbar.make(mainLayout!!, "You Clicked: " + employee.name + ", position: " + employee.position + ", address " + employee.address + " , id" + employee.id , Snackbar.LENGTH_LONG).show()
    }
}