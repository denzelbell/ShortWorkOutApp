package com.drawbytess.shortworkoutapp

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_exercise_status.view.*


class ExerciseStatusAdapter(private val items: ArrayList<ExerciseModel>, private val context: Context): RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tvItem = view.tvItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_exercise_status, // Allows something to be viewable in the list
                        parent,false))
    }

    // Assigns value to each recycle view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model: ExerciseModel = items[position]

        holder.tvItem.text = model.getId().toString()

        // Set color set for recycleViewbased on completion
        if(model.getIsSelected()){
            holder.tvItem.background =
                    ContextCompat.getDrawable(context, R.drawable.item_circular_thin_color_accent_border)
            holder.tvItem.setTextColor(Color.parseColor("#212121"))
        } else if(model.getIsCompleted()){
            holder.tvItem.background =
                    ContextCompat.getDrawable(context, R.drawable.item_circular_color_accent_background)
            holder.tvItem.setTextColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.tvItem.background =
                    ContextCompat.getDrawable(context, R.drawable.item_circular_color_grey_background)
            holder.tvItem.setTextColor(Color.parseColor("#212121"))
        }
    }

    // Returns the number of recycle counts
    override fun getItemCount(): Int {
        return items.size
    }

}