package com.example.magiceventkt.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.magiceventkt.R
import com.example.magiceventkt.models.EventoModel

class eveRecyclerAdapter (private val eventList : ArrayList<EventoModel>) : RecyclerView.Adapter<eveRecyclerAdapter.ViewHolder>(){

    private lateinit var mListener : onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position : Int)

    }
    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.evento_view, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: eveRecyclerAdapter.ViewHolder, position: Int) {
        val currentEvent = eventList[position]
        holder.tvNombre.text = currentEvent.nombreEvento
        holder.tvDesc.text = currentEvent.desc
        holder.tvFecha.text = currentEvent.fecha
        holder.tvUbi.text = currentEvent.ubicacion
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    class ViewHolder (itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val tvNombre : TextView = itemView.findViewById(R.id.tvNombre)
        val tvDesc : TextView = itemView.findViewById(R.id.tvDesc)
        val tvFecha : TextView = itemView.findViewById(R.id.tvFecha)
        val tvUbi : TextView = itemView.findViewById(R.id.tvUbicacion)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }

    }
}
