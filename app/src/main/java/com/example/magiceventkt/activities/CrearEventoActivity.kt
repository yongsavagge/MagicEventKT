package com.example.magiceventkt.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import android.content.res.Configuration
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.drawerlayout.widget.DrawerLayout
import com.example.magiceventkt.R
import com.example.magiceventkt.models.EventoModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class CrearEventoActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var editNombreEvento: TextView
    private lateinit var editDesc: TextView
    private lateinit var txtFecha: TextView
    private lateinit var editUbicacion: TextView
    private lateinit var btnGuardarEvento: Button
    private lateinit var dbRef: DatabaseReference
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var spnCategoria: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_evento)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        editNombreEvento = findViewById(R.id.editNombreEvento)
        editDesc = findViewById(R.id.editDesc)
        txtFecha = findViewById(R.id.editFecha) // Cambiado a TextView
        editUbicacion = findViewById(R.id.editUbicacion)
        btnGuardarEvento = findViewById(R.id.btnGuardarEvento)
        spnCategoria = findViewById(R.id.spnCategoria)
        val categorias = arrayOf(
            "Cultural",
            "Deportivo",
            "Corporativo",
            "Educativo",
            "Celebración Social",
            "Religioso",
            "Benéfico",
            "Gastronómico",
            "Tecnológico",
            "Otro"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnCategoria.adapter = adapter
        spnCategoria.setSelection(9, false)
        spnCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

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

        val selectedDateMillis = intent.getLongExtra("selectedDate", 0)

        if (selectedDateMillis != 0L) {
            val selectedDate = Date(selectedDateMillis)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            txtFecha.text = dateFormat.format(selectedDate)
        }

        // Deshabilitar la edición directa del TextView
        txtFecha.keyListener = null

        // Mostrar el DatePickerDialog al hacer clic en el TextView de fecha
        txtFecha.setOnClickListener {
            mostrarDatePicker()
        }
    }

    private fun mostrarDatePicker() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                txtFecha.text = dateFormat.format(selectedCalendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val intentMisEve = Intent(this, MisEventosActivity::class.java)
        val intentCalen = Intent(this, CalendarActivity::class.java)
        val intentProfile = Intent(this, ProfileActivity::class.java)

        when (item.itemId) {
            R.id.nav_itemMisEve -> startActivity(intentMisEve)
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
        val nombreEvento = editNombreEvento.text.toString().trim()
        val desc = editDesc.text.toString().trim()
        val fecha = txtFecha.text.toString().trim()
        val ubicacion = editUbicacion.text.toString().trim()
        val categoria = spnCategoria.selectedItem.toString().trim()

        if (nombreEvento.isEmpty() || desc.isEmpty() || fecha.isEmpty() || ubicacion.isEmpty() || categoria.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos del evento", Toast.LENGTH_SHORT).show()
            return
        }

        val eventoID = dbRef.push().key!!
        val evento = EventoModel(eventoID, nombreEvento, desc, fecha, ubicacion, categoria)

        dbRef.child(eventoID).setValue(evento).addOnCompleteListener {
            Toast.makeText(this, "El evento ha sido agregado con éxito!", Toast.LENGTH_SHORT).show()
            editNombreEvento.text = ""
            editDesc.text = ""
            txtFecha.text = ""
            editUbicacion.text = ""
            spnCategoria.setSelection(9, false)
        }.addOnFailureListener { err ->
            Toast.makeText(this, "El evento no ha podido ser creado con éxito", Toast.LENGTH_SHORT).show()
        }
    }
    private fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(this, "Has cerrado sesión exitosamente!", Toast.LENGTH_SHORT).show()
        val intentLogin = Intent(this, LoginActivity::class.java)
        startActivity(intentLogin)
        finish()
    }
}
