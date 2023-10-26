package com.example.magiceventkt.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.magiceventkt.R
import com.example.magiceventkt.adapters.eveRecyclerAdapter
import com.example.magiceventkt.models.EventoModel
import com.example.magiceventkt.models.editActivity
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MisEventosActivity : AppCompatActivity() {

    private lateinit var eveRecyclerView : RecyclerView
    private lateinit var eventoList : ArrayList<EventoModel>
    private lateinit var dbRef : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_eventos)

        eveRecyclerView = findViewById(R.id.rcViewEventos)
        eveRecyclerView.layoutManager = LinearLayoutManager(this)
        eveRecyclerView.setHasFixedSize(true)

        eventoList = arrayListOf<EventoModel>()

        getEventData()
    }

    private fun getEventData() {
        eveRecyclerView.visibility = View.VISIBLE
        dbRef = FirebaseDatabase.getInstance().getReference("CreacionEvento")
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                eventoList.clear()
                if (snapshot.exists()){
                    for(eveSnp in snapshot.children){
                        val eventData = eveSnp.getValue(EventoModel::class.java)
                        eventoList.add(eventData!!)
                    }
                    val mAdapter = eveRecyclerAdapter(eventoList)
                    eveRecyclerView.adapter = mAdapter
                    mAdapter.setOnItemClickListener(object : eveRecyclerAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@MisEventosActivity, editActivity::class.java)

                            intent.putExtra("eventoID", eventoList[position].eventoID)
                            intent.putExtra("nombreEvento", eventoList[position].nombreEvento)
                            intent.putExtra("desc", eventoList[position].desc)
                            intent.putExtra("fecha", eventoList[position].fecha)
                            intent.putExtra("ubicacion", eventoList[position].ubicacion)
                            startActivity(intent)

                        }

                    })
                    eveRecyclerView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}