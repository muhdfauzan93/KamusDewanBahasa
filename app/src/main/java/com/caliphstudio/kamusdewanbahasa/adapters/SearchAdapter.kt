package com.caliphstudio.kamusdewanbahasa.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.balysv.materialripple.MaterialRippleLayout
import com.caliphstudio.kamusdewanbahasa.R
import com.caliphstudio.kamusdewanbahasa.activities.Details
import com.caliphstudio.kamusdewanbahasa.models.KamusDewan
import java.util.*

class SearchAdapter (myContext: Context, meaning: ArrayList<KamusDewan>): RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {

    private var meaningList: List<KamusDewan>? = null
    private var context:Context ?= null

    init {
        this.meaningList = meaning
        this.context = myContext
    }
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tv_title)
        val desc: TextView = view.findViewById(R.id.tv_desc)
        val edition: TextView = view.findViewById(R.id.tv_edition)
        val cardView: MaterialRippleLayout = view.findViewById(R.id.ripple_search_response)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val meaning = meaningList!![position]
        @Suppress("DEPRECATION")
        holder.title.text = Html.fromHtml(meaning.tajuk)
        @Suppress("DEPRECATION")
        holder.desc.text = Html.fromHtml(meaning.maksud)
        holder.edition.text = meaning.edisi

        holder.cardView.setOnClickListener {

            val intent = Intent(context,Details::class.java)
            intent.putExtra("tajuk", meaning.tajuk)
            intent.putExtra("maksud", meaning.maksud)
            intent.putExtra("edisi",meaning.edisi)

            context!!.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder{

        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.search_list_row, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return meaningList!!.size
    }
}