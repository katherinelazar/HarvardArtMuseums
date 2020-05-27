package hu.bme.aut.bottomnavfragmentsdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.bottomnavfragmentsdemo.R
import kotlinx.android.synthetic.main.info_row.view.*

class ImageDetailsAdapter : RecyclerView.Adapter<ImageDetailsAdapter.ViewHolder> {

    var infoItems = mutableListOf<String?>()
    val context: Context

    constructor(context: Context, listInfo: ArrayList<String?>) {
        this.context = context
        infoItems.addAll(listInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.info_row, parent, false
        )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return infoItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentInfo = infoItems[position]

        holder.tvRowItem.text = currentInfo
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRowItem = itemView.tvRowItem
    }

}