package com.gustavohnsv.drink_me

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gustavohnsv.drink_me.R
import com.gustavohnsv.drink_me.adapter.DailyRecordsAdapter
import com.gustavohnsv.drink_me.util.DataStorageUtils

class RecordsActivity : AppCompatActivity() {

    private val dataStorageUtils = DataStorageUtils.Companion

    private fun adjustRecords() {
        val recyclerRecords = findViewById<RecyclerView>(R.id.recycler_records)
        recyclerRecords.layoutManager = LinearLayoutManager(this)
        recyclerRecords.adapter = DailyRecordsAdapter(dataStorageUtils.getData(context = this))
    }

    private fun changeToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.records)

        val btnMain = findViewById<Button>(R.id.btn_main)

        btnMain.setOnClickListener { changeToMain() }

        adjustRecords()

        showLogData()

    }

    private fun showLogData() {
        for (record in dataStorageUtils.getData(context = this)) {
            Log.i("DailyRecord[${record.date}]", "Data: $record")
        }
    }

}