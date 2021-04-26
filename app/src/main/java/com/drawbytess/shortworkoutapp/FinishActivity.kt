package com.drawbytess.shortworkoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_finish.*
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        // Set up toolbar instructions
        setSupportActionBar(toolbar_exercise_activity2)
        val actionbar = supportActionBar
        if (actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        // Set up toolbar actions
        toolbar_exercise_activity2.setNavigationOnClickListener{
            onBackPressed()
        }

        tv_finaltext.text = "Congratulations! \nYou have completed the 7 minute workout!"

        btn_finished.setOnClickListener {
            finish()
        }

        addDateToDatabase()

    }

    private fun addDateToDatabase(){
        val calendar = Calendar.getInstance()
        val dateTime = calendar.time
        Log.i("Date", "" + dateTime)

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(dateTime)

        val dbHandler = SqliteOpenHelper(this, null)
        dbHandler.addDate(date)
        Log.i("DATE: ", "ADDED")


    }

}