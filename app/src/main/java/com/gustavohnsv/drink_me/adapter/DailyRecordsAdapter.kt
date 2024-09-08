package com.gustavohnsv.drink_me.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gustavohnsv.drink_me.R
import com.gustavohnsv.drink_me.model.DailyRecord

class DailyRecordsAdapter(private val records: List<DailyRecord>) : RecyclerView.Adapter<DailyRecordsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateView: TextView = view.findViewById(R.id.item_date)
        val goalReachedView: TextView = view.findViewById(R.id.item_goalReached)
        val waterView: TextView = view.findViewById(R.id.item_water)
        val weightView: TextView = view.findViewById(R.id.item_weight)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_daily_record, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = records[position]
        holder.dateView.text = buildString {
            append("Data: ${record.date}")
        }
        holder.goalReachedView.text = buildString {
            append("Meta alcançada? ${if (record.goalReached) "Sim" else "Não"}")
        }
        holder.waterView.text = buildString {
            append("Quantidade de água: ${record.waterIntake}ml")
        }
        holder.weightView.text = buildString {
            append("Peso: ${record.weight}kg")
        }
    }

    override fun getItemCount(): Int = records.size

}