package com.example.magiceventkt.activities

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.magiceventkt.R
import com.google.android.material.navigation.NavigationView

class CalendarActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

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
    }

    // Manejo de elementos del menú de navegación
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val intentCrear = Intent(this, CrearEventoActivity::class.java)
        val intentMisEve = Intent(this, MisEventosActivity::class.java)
        val intentProfile = Intent(this, ProfileActivity::class.java)
        when (item.itemId) {
            R.id.nav_itemNuevo -> startActivity(intentCrear)
            R.id.nav_itemMisEve -> startActivity(intentMisEve)
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
}
