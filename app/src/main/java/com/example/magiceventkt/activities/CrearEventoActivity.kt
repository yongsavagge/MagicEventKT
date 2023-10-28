package com.example.magiceventkt.activities

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.magiceventkt.models.EventoModel
import com.example.magiceventkt.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CrearEventoActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var editNombreEvento: EditText
    private lateinit var editDesc: EditText
    private lateinit var editFecha: EditText
    private lateinit var editUbicacion: EditText
    private lateinit var btnGuardarEvento: Button
    private lateinit var dbRef: DatabaseReference
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_evento)

        editNombreEvento = findViewById(R.id.editNombreEvento)
        editDesc = findViewById(R.id.editDesc)
        editFecha = findViewById(R.id.editFecha)
        editUbicacion = findViewById(R.id.editUbicacion)

        btnGuardarEvento = findViewById(R.id.btnGuardarEvento)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        dbRef = FirebaseDatabase.getInstance().getReference("CreacionEvento")

        btnGuardarEvento.setOnClickListener {
            guardarDatosEvento()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val intentMisEve = Intent(this, MisEventosActivity::class.java)
        val intentCalen = Intent(this, CalendarActivity::class.java)
        val intentProfile = Intent(this, ProfileActivity::class.java)
        when (item.itemId) {
            R.id.nav_itemMisEve -> startActivity(intentMisEve)
            R.id.nav_itemCalendario -> startActivity(intentCalen)
            R.id.nav_itemPerfil -> startActivity(intentProfile)
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun guardarDatosEvento() {
        val nombreEvento = editNombreEvento.text.toString()
        val desc = editDesc.text.toString()
        val fecha = editFecha.text.toString()
        val ubicacion = editUbicacion.text.toString()

        if (nombreEvento.isEmpty()) {
            editNombreEvento.error = "Por favor, ingresa el nombre del evento"
        }
        if (desc.isEmpty()) {
            editDesc.error = "Por favor, ingresa la descripción del evento"
        }
        if (fecha.isEmpty()) {
            editFecha.error = "Por favor, ingresa la fecha del evento"
        }
        if (ubicacion.isEmpty()) {
            editUbicacion.error = "Por favor, ingresa la ubicación del evento"
        }

        val eventoID = dbRef.push().key!!
        val evento = EventoModel(eventoID, nombreEvento, desc, fecha, ubicacion)

        dbRef.child(eventoID).setValue(evento).addOnCompleteListener {
            Toast.makeText(this, "El evento ha sido agregado con éxito!", Toast.LENGTH_SHORT).show()
            editNombreEvento.text.clear()
            editDesc.text.clear()
            editFecha.text.clear()
            editUbicacion.text.clear()
        }.addOnFailureListener { err ->
            Toast.makeText(this, "El evento no ha podido ser creado con éxito", Toast.LENGTH_SHORT).show()
        }
    }
}
