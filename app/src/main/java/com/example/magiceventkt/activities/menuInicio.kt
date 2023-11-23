package com.example.magiceventkt.activities

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.content.res.Configuration
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.magiceventkt.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class menuInicio : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_inicio)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        // Inicialización del DrawerLayout
        drawer = findViewById(R.id.drawer_layout)

        // Configuración del ActionBarDrawerToggle
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        // Configuración del menú de navegación
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        // BOTONES
        val btnCrear = findViewById<Button>(R.id.btnCrear)
        btnCrear.setOnClickListener {
            val intentCrear = Intent(this, CrearEventoActivity::class.java)
            startActivity(intentCrear)
        }

        val btnMisEve = findViewById<Button>(R.id.btnMisEve)
        btnMisEve.setOnClickListener {
            val intentMisEve = Intent(this, MisEventosActivity::class.java)
            startActivity(intentMisEve)
        }

        val btnCalendar = findViewById<Button>(R.id.btnCalendar)
        btnCalendar.setOnClickListener {
            val intentCalen = Intent(this, CalendarActivity::class.java)
            startActivity(intentCalen)
        }

        val btnProfile = findViewById<Button>(R.id.btnProfile)
        btnProfile.setOnClickListener {
            val intentProfile = Intent(this, ProfileActivity::class.java)
            startActivity(intentProfile)
        }
    }

    // Manejo de elementos del menú de navegación
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val intentCrear = Intent(this, CrearEventoActivity::class.java)
        val intentMisEve = Intent(this, MisEventosActivity::class.java)
        val intentCalen = Intent(this, CalendarActivity::class.java)
        val intentProfile = Intent(this, ProfileActivity::class.java)
        when (item.itemId) {
            R.id.nav_itemNuevo -> startActivity(intentCrear)
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
    private fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(this, "Has cerrado sesión exitosamente!", Toast.LENGTH_SHORT).show()
        val intentLogin = Intent(this, LoginActivity::class.java)
        startActivity(intentLogin)
        finish()
    }
}
