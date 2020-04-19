package com.example.halodoc.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.halodoc.R
import com.example.halodoc.model.Hit
import org.koin.ext.getScopeId

class SearchNewsAdapter(private val context: Context, private val listener: AdapterResponseListener) : RecyclerView.Adapter<SearchNewsAdapter.ViewHolder>(), View.OnClickListener {

    private var list: MutableList<Hit>? = ArrayList()


    fun updateList(list: List<Hit>?, isPaginated: Boolean) {
        list?.let {
            if(!isPaginated) { this.list?.clear() }

            this.list?.addAll(it)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false)
        val holder = ViewHolder(itemView)
        holder.cvContainer.setOnClickListener(this)

        Log.v("test123", "data" + holder)
        Log.v("test123", "data" + holder.getScopeId())


        return holder
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cvContainer.tag = position

        list?.let {
           val hit = it[position]
            holder.tvTitle.text = hit.title
            holder.tvLink.text = hit.url
        }

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.container -> {
                val position = view.tag as Int
                val url = list?.get(position)?.url
                listener.onAdapterResponded(url)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvContainer: LinearLayout = itemView.findViewById(R.id.container)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvLink: TextView = itemView.findViewById(R.id.tvLink)
    }

}