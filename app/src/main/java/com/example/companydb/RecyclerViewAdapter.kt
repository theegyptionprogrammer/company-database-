package com.example.companydb

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView

@Suppress("NAME_SHADOWING")
class
RecyclerViewAdapter(val employeeList: ArrayList<Employee>, val context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() , Filterable {


    private var searchEmployeeList : ArrayList<Employee> ?= null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.employee, parent, false))
    }

    override fun getItemCount(): Int {
        return employeeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.TvName?.text = employeeList[position].name
        holder.TvAddress?.text = employeeList[position].address
        holder.TvPosition?.text = employeeList[position].position
        holder.TvId?.text = employeeList[position].id.toString()

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val TvName = view.findViewById<TextView>(R.id.NameTV)
        val TvAddress = view.findViewById<TextView>(R.id.AddressTV)
        val TvPosition = view.findViewById<TextView>(R.id.PositionTV)
        val TvId = view.findViewById<TextView>(R.id.IdTV)
    }

    init { this.searchEmployeeList = employeeList }

    fun removeItem(position: Int){
        employeeList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()) {
                    searchEmployeeList = employeeList
                } else {
                    val filteredList = ArrayList<Employee>()
                    for (employee in employeeList) {
                        if (employee.name.toLowerCase().contains(charString.toLowerCase())
                            || employee.address.toLowerCase().contains(charString.toLowerCase())
                            || employee.position.toLowerCase().contains(charString.toLowerCase())
                        ) {
                            filteredList.add(employee)
                        }
                            searchEmployeeList = employeeList
                    }
                }
                val filteredResults = Filter.FilterResults()
                filteredResults.values = searchEmployeeList
                return filteredResults
            }

            override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults?) {
                searchEmployeeList = results?.values as ArrayList<Employee>
                notifyDataSetChanged()
            }
        }
    }
    }


