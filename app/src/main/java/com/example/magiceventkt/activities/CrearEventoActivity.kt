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

        // Inicialización de elementos de la interfaz de usuario
        editNombreEvento = findViewById(R.id.editNombreEvento)
        editDesc = findViewById(R.id.editDesc)
        editFecha = findViewById(R.id.editFecha)
        editUbicacion = findViewById(R.id.editUbicacion)
        btnGuardarEvento = findViewById(R.id.btnGuardarEvento)

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

        // Inicialización de la referencia a la base de datos de Firebase
        dbRef = FirebaseDatabase.getInstance().getReference("CreacionEvento")

        // Configuración del click listener para el botón de guardar evento
        btnGuardarEvento.setOnClickListener {
            guardarDatosEvento()
        }
    }

    // Manejo de elementos del menú de navegación
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Definición de las intenciones para otras actividades
        val intentMisEve = Intent(this, MisEventosActivity::class.java)
        val intentCalen = Intent(this, CalendarActivity::class.java)
        val intentProfile = Intent(this, ProfileActivity::class.java)

        // Manejo de selección de elementos del menú de navegación
        when (item.itemId) {
            R.id.nav_itemMisEve -> startActivity(intentMisEve)
            R.id.nav_itemCalendario -> startActivity(intentCalen)
            R.id.nav_itemPerfil -> startActivity(intentProfile)
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

    private fun guardarDatosEvento() {
        // Obtener los datos del evento ingresados por el usuario
        val nombreEvento = editNombreEvento.text.toString()
        val desc = editDesc.text.toString()
        val fecha = editFecha.text.toString()
        val ubicacion = editUbicacion.text.toString()

        // Validar los campos obligatorios
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

        // Generar una clave única para el evento en Firebase
        val eventoID = dbRef.push().key!!

        // Crear un objeto EventoModel con los datos del evento
        val evento = EventoModel(eventoID, nombreEvento, desc, fecha, ubicacion)

        // Guardar el evento en la base de datos de Firebase
        dbRef.child(eventoID).setValue(evento).addOnCompleteListener {
            // Mostrar un mensaje de éxito y limpiar los campos
            Toast.makeText(this, "El evento ha sido agregado con éxito!", Toast.LENGTH_SHORT).show()
            editNombreEvento.text.clear()
            editDesc.text.clear()
            editFecha.text.clear()
            editUbicacion.text.clear()
        }.addOnFailureListener { err ->
            // Mostrar un mensaje de error si la operación falla
            Toast.makeText(this, "El evento no ha podido ser creado con éxito", Toast.LENGTH_SHORT).show()
        }
    }
}
