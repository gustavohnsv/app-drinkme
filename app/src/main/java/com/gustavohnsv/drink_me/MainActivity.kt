package com.gustavohnsv.drink_me

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.gustavohnsv.drink_me.R
import com.gustavohnsv.drink_me.model.DailyRecord
import com.gustavohnsv.drink_me.util.DataStorageUtils
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private val viewModels: MainViewModel by viewModels()
    private val dataStorageUtils = DataStorageUtils.Companion

    private fun saveMainActivityData() {
        dataStorageUtils.saveData(
            context = this, data = DailyRecord(
                LocalDate.now().toString(),
                viewModels.totalWeight,
                viewModels.totalWater,
                viewModels.goalWater,
                viewModels.isGoalReached
            )
        )
    }

    private fun checkDailyData() {
        val dailyRecord = dataStorageUtils.getDailyData(context = this)
        if (dailyRecord != null) {
            viewModels.totalWater = dailyRecord.waterIntake
            viewModels.goalWater = dailyRecord.waterGoal
            viewModels.totalWeight = dailyRecord.weight
            viewModels.isGoalReached = dailyRecord.goalReached
            Log.i("DailyRecord[${dailyRecord.date}]", "Data found: $dailyRecord")
        } else {
            Log.i("DailyRecord[${LocalDate.now()}]", "Data not found")
        }
    }

    private fun checkGoalReached(txtWaterTotal: TextView) {
        if (viewModels.isGoalReached) {
            txtWaterTotal.text = buildString {
                append(viewModels.goalReachedText)
            }
        } else {
            txtWaterTotal.text = buildString {
                append("${viewModels.totalWater}ml / ${viewModels.goalWater}ml")
            }
        }
    }

    private fun incrementTotalWater(btn: Button?, txtWaterTotal: TextView?, value: Int, pgbar: ProgressBar?) {
        btn?.setOnClickListener {
            if (viewModels.totalWeight <= 0 || pgbar == null) {
                return@setOnClickListener
            }
            viewModels.totalWater += value
            pgbar.progress = viewModels.totalWater
            if (viewModels.totalWater < viewModels.goalWater) {
                Log.i("GoalSituation", "Goal not reached with ${viewModels.totalWater}ml")
                viewModels.isGoalReached = false
                txtWaterTotal?.text = buildString {
                    append("${viewModels.totalWater}ml / ${viewModels.goalWater}ml")
                }
            } else {
                Log.i("GoalSituation", "Goal reached with ${viewModels.totalWater}ml")
                viewModels.isGoalReached = true
                saveMainActivityData()
                txtWaterTotal?.text = buildString {
                    append(viewModels.goalReachedText)
                }
            }
        }
    }

    private fun showAlert(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") {
                dialog, _ ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

    private fun adjustClear(pgbarWaterIntake: ProgressBar, txtWaterTotal: TextView) {
        viewModels.totalWater = 0
        viewModels.isGoalReached = false
        pgbarWaterIntake.progress = viewModels.totalWater
        txtWaterTotal.text = buildString {
            append("${viewModels.totalWater}ml / ${viewModels.goalWater}ml")
        }
    }

    private fun adjustWeight(pgbarWaterIntake: ProgressBar, txtWaterTotal: TextView, inputManager: InputMethodManager) {
        val inputWeight = findViewById<EditText>(R.id.input_weight)
        if (inputWeight.text.isNotEmpty() && inputWeight.text.toString().toInt() > 0) {
            viewModels.totalWeight = inputWeight.text.toString().toInt()
            viewModels.goalWater = inputWeight.text.toString().toInt() * 35
            pgbarWaterIntake.max = viewModels.goalWater
            pgbarWaterIntake.progress = viewModels.totalWater
            txtWaterTotal.text = buildString {
                append("${viewModels.totalWater}ml / ${viewModels.goalWater}ml")
            }
            if (viewModels.totalWater > viewModels.goalWater) {
                viewModels.isGoalReached = true
                txtWaterTotal.text = buildString {
                    append(viewModels.goalReachedText)
                }
            } else {
                viewModels.isGoalReached = false
                txtWaterTotal.text = buildString {
                    append("${viewModels.totalWater}ml / ${viewModels.goalWater}ml")
                }
            }
            showAlert("Peso salvo", "Peso salvo com sucesso!")
        } else {
            viewModels.totalWater = 0
            viewModels.goalWater = 0
            viewModels.totalWeight = 0
            viewModels.isGoalReached = false
            txtWaterTotal.text = buildString {
                append("Valor inválido")
            }
            showAlert("Peso inválido", "Por favor, insira um peso válido!")
        }
        inputWeight.text.clear()
        inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun adjustProgressBar(pgbarWaterIntake: ProgressBar) {
        pgbarWaterIntake.progressTintList = getColorStateList(R.color.green_500)
        pgbarWaterIntake.max = viewModels.goalWater
        pgbarWaterIntake.progress = viewModels.totalWater
    }

    private fun changeToRecords() {
        val intent = Intent(this, RecordsActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        checkDailyData()

        val btn100ml = findViewById<Button>(R.id.btn_100ml)
        val btn200ml = findViewById<Button>(R.id.btn_200ml)
        val btn500ml = findViewById<Button>(R.id.btn_500ml)
        val btn700ml = findViewById<Button>(R.id.btn_700ml)
        val btn1000ml = findViewById<Button>(R.id.btn_1000ml)
        val btn1500ml = findViewById<Button>(R.id.btn_1500ml)
        val btnClear = findViewById<Button>(R.id.btn_clear)
        val btnWeight = findViewById<Button>(R.id.btn_weight)
        val btnRecords = findViewById<Button>(R.id.btn_records)

        val txtWaterTotal = findViewById<TextView>(R.id.txt_water_total)

        val pgbarWaterIntake = findViewById<ProgressBar>(R.id.pgbar_waterIntake)

        adjustProgressBar(pgbarWaterIntake)

        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        this.incrementTotalWater(btn100ml, txtWaterTotal, 100, pgbarWaterIntake)
        this.incrementTotalWater(btn200ml, txtWaterTotal, 200, pgbarWaterIntake)
        this.incrementTotalWater(btn500ml, txtWaterTotal, 500, pgbarWaterIntake)
        this.incrementTotalWater(btn700ml, txtWaterTotal, 700, pgbarWaterIntake)
        this.incrementTotalWater(btn1000ml, txtWaterTotal, 1000, pgbarWaterIntake)
        this.incrementTotalWater(btn1500ml, txtWaterTotal, 1500, pgbarWaterIntake)

        btnWeight.setOnClickListener { adjustWeight(pgbarWaterIntake, txtWaterTotal, inputManager) }

        // dev tool
        btnClear.setOnClickListener { adjustClear(pgbarWaterIntake, txtWaterTotal) }

        btnRecords.setOnClickListener { changeToRecords() }

        checkGoalReached(txtWaterTotal)
        saveMainActivityData()

    }

    override fun onPause() {
        super.onPause()
        saveMainActivityData()
    }

}

class MainViewModel : ViewModel() {
    var totalWater = 0
    var goalWater = 0
    var totalWeight = 0
    var isGoalReached = false
    var goalReachedText = "Meta atingida!"
}
