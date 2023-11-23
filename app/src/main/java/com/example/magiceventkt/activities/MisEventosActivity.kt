package com.example.magiceventkt.activities

import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MisEventosActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var eveRecyclerView: RecyclerView
    private lateinit var eventoList: ArrayList<EventoModel>
    private lateinit var dbRef: DatabaseReference

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_eventos)

        // Configuración de la barra de herramientas (Toolbar)
        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        // Inicialización del DrawerLayout
        drawer = findViewById(R.id.drawer_layout)

        // Configuración del ActionBarDrawerToggle para abrir y cerrar el menú lateral
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        // Configuración de la acción de inicio y la habilitación de botón de inicio en la barra de herramientas
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        // Configuración del menú de navegación
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        // Inicialización del RecyclerView
        eveRecyclerView = findViewById(R.id.rcViewEventos)
        eveRecyclerView.layoutManager = LinearLayoutManager(this)
        eveRecyclerView.setHasFixedSize(true)

        // Inicialización de la lista de eventos
        eventoList = arrayListOf<EventoModel>()

        // Obtener datos de eventos
        getEventData()
    }

    // Función para obtener datos de eventos desde Firebase
    private fun getEventData() {
        eveRecyclerView.visibility = View.VISIBLE
        dbRef = FirebaseDatabase.getInstance().getReference("CreacionEvento")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                eventoList.clear()
                if (snapshot.exists()) {
                    for (eveSnp in snapshot.children) {
                        val eventData = eveSnp.getValue(EventoModel::class.java)
                        eventoList.add(eventData!!)
                    }
                    // Configuración del adaptador para el RecyclerView
                    val mAdapter = eveRecyclerAdapter(eventoList)
                    eveRecyclerView.adapter = mAdapter
                    mAdapter.setOnItemClickListener(object : eveRecyclerAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            // Redirigir a la actividad de edición con los detalles del evento
                            val intent = Intent(this@MisEventosActivity, editActivity::class.java)
                            intent.putExtra("eventoID", eventoList[position].eventoID)
                            intent.putExtra("nombreEvento", eventoList[position].nombreEvento)
                            intent.putExtra("desc", eventoList[position].desc)
                            intent.putExtra("fecha", eventoList[position].fecha)
                            intent.putExtra("ubicacion", eventoList[position].ubicacion)
                            intent.putExtra("categoria", eventoList[position].categoria)
                            startActivity(intent)
                        }
                    })
                    eveRecyclerView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejo de errores si la operación se cancela
                TODO("Not yet implemented")
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Definición de las intenciones para otras actividades
        val intentCrear = Intent(this, CrearEventoActivity::class.java)
        val intentCalen = Intent(this, CalendarActivity::class.java)
        val intentProfile = Intent(this, ProfileActivity::class.java)

        when (item.itemId) {
            R.id.nav_itemNuevo -> startActivity(intentCrear)
            R.id.nav_itemCalendario -> startActivity(intentCalen)
            R.id.nav_itemPerfil -> startActivity(intentProfile)
            R.id.nav_CerrarSesion -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.closesession)
                builder.setMessage(R.string.cerrarSesionApprove)

                builder.setPositiveButton(R.string.approve) { dialog, which ->
                    cerrarSesion() // Función para cerrar sesión
                }

                builder.setNegativeButton(R.string.deny) { dialog, which ->
                    dialog.dismiss()
                }

                val alertDialog = builder.create()
                alertDialog.show()
            }
        }
        // Cerrar el menú lateral después de la selección
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sincronizar el estado del ActionBarDrawerToggle después de que se haya restaurado el estado de la actividad
        toggle.syncState()
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Actualizar la configuración del ActionBarDrawerToggle en función de la configuración de la actividad
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            // Manejar eventos de selección en el ActionBarDrawerToggle
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(this, "Has cerrado sesión exitosamente!", Toast.LENGTH_SHORT).show()
        val intentLogin = Intent(this, LoginActivity::class.java)
        startActivity(intentLogin)
        finish()
    }
}
