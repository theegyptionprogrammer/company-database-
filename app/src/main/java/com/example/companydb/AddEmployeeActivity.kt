package com.example.companydb

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_employee.*

@SuppressLint("Registered")
class AddEmployeeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_employee)
        DoneBtn.setOnClickListener {
            save()
            clear()
        }

    }

    fun save() {
        val helper = SqDBHandler(this, null)
        val employee = Employee()
        if (NameTE.text.isEmpty() && PositionTE.text.isEmpty() && AddressTE.text.isEmpty() && IdTE.text.isEmpty()) {
            Toast.makeText(this, "fill all the blancks ", Toast.LENGTH_SHORT).show()
        } else {
            employee.Name = NameTE.text.toString()
            employee.position = PositionTE.text.toString()
            employee.Address = AddressTE.text.toString()
            employee.id = IdTE.text.toString().toDouble()
            helper.AddEmployee(employee, helper)
            Toast.makeText(this, "the Employee added", Toast.LENGTH_SHORT).show()
        }
    }

    fun clear() {
        NameTE.setText("")
        PositionTE.setText("")
        AddressTE.setText("")
        IdTE.setText("")
    }


}