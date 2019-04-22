package com.example.companydb

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AddBtn.setOnClickListener {
            val intent = Intent(this, AddEmployeeActivity::class.java)
            startActivity(intent)
        }

        AllUsersBtn.setOnClickListener {
            val intent = Intent(this, AllEmployeesActivity::class.java)
            startActivity(intent)
        }

        SearchBtn.setOnClickListener {
            val intent = Intent(this, SearchEmployeeActivity::class.java)
            startActivity(intent)
        }
    }


}
