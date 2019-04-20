package com.example.companydb

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.employee.view.*

class RecyclerViewAdapter(val items: ArrayList<String>, val context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    fun additems(items: List<String>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.employee, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.nameTV?.text = items.get(position)
        holder?.idTV?.text = items.get(position)
        holder?.addressTV?.text = items.get(position)
        holder?.positionTV?.text = items.get(position)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTV = view.NameTV
        val addressTV = view.AddressTV
        val positionTV = view.PositionTV
        val idTV = view.IdTV
        val photo = view.photo
    }
}