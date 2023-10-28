package com.example.magiceventkt.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.magiceventkt.R
import com.example.magiceventkt.adapters.eveRecyclerAdapter
import com.example.magiceventkt.models.EventoModel
import com.example.magiceventkt.models.editActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MisEventosActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var eveRecyclerView : RecyclerView
    private lateinit var eventoList : ArrayList<EventoModel>
    private lateinit var dbRef : DatabaseReference

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_eventos)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

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
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val intentCrear = Intent(this, CrearEventoActivity::class.java)
        val intentCalen = Intent(this, CalendarActivity::class.java)
        val intentProfile = Intent(this, ProfileActivity::class.java)
        when (item.itemId){
            R.id.nav_itemNuevo -> startActivity(intentCrear)
            R.id.nav_itemCalendario -> startActivity(intentCalen)
            R.id.nav_itemPerfil -> startActivity(intentProfile)
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?){
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}