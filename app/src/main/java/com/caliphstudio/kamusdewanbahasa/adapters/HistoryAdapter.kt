package com.caliphstudio.kamusdewanbahasa.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.balysv.materialripple.MaterialRippleLayout
import com.caliphstudio.kamusdewanbahasa.HistoryInterface
import com.caliphstudio.kamusdewanbahasa.R
import com.caliphstudio.kamusdewanbahasa.activities.Search
import com.caliphstudio.kamusdewanbahasa.models.History

class HistoryAdapter (myContext: Context, history: ArrayList<History>): RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {

    private var context:Context ?= null
    private var historyList:ArrayList<History>
    private var myInterface:HistoryInterface = myContext as HistoryInterface

    init {
        this.context = myContext
        this.historyList = history
    }
    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        val historyKeyWord = historyList[position]
        holder!!.word.text = historyKeyWord.word

        holder.cardView.setOnClickListener {
            val intent = Intent(context, Search::class.java)
            intent.putExtra("searchText", historyKeyWord.word)

            context!!.startActivity(intent)
        }

        holder.btnDel.setOnClickListener {
            myInterface.removeSearchHistory(historyKeyWord.word)
            myInterface.getSearchHistory()


        }
    }
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val word: TextView = view.findViewById(R.id.tv_search_key)
        val btnDel:ImageButton = view.findViewById(R.id.btn_del_hist)
        val cardView: MaterialRippleLayout = view.findViewById(R.id.ripple_history)


    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HistoryAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.history_list_row, parent, false)
        return MyViewHolder(view)
    }
}