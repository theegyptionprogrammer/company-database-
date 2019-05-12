package com.example.companydb

import android.annotation.SuppressLint
import android.os.Bundle
import android.speech.tts.TextToSpeech
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
        }

    }

    fun save() {
        val helper = SqDBHandler(this, null)

        if (NameTE.text.isEmpty() && PositionTE.text.isEmpty() && AddressTE.text.isEmpty() && IdTE.text.isEmpty()) {
            Toast.makeText(this, "fill all the blancks ", Toast.LENGTH_SHORT).show()
        } else {
            val name = NameTE.text.toString()
            val position = PositionTE.text.toString()
            val address = AddressTE.text.toString()
            val id = Integer.parseInt(IdTE.text.toString())
            val employee = Employee(name , position , address , id)
            helper.AddEmployee(employee)
            Toast.makeText(this, "the Employee added", Toast.LENGTH_SHORT).show()
            NameTE.setText("")
            PositionTE.setText("")
            AddressTE.setText("")
            IdTE.setText("")
        }
    }
}