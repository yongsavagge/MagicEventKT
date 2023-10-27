package com.example.magiceventkt.activities


import android.content.Intent
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.content.res.Configuration
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.magiceventkt.R
import com.google.android.material.navigation.NavigationView

class menuInicio : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_inicio)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        // Se llama cada botón que está en el Layout de @MenuInicio
        val btnCrear = findViewById<Button>(R.id.btnCrear)
        btnCrear.setOnClickListener{
            val intentCrear = Intent(this, CrearEventoActivity::class.java)
            startActivity(intentCrear)
        }

        val btnMisEve = findViewById<Button>(R.id.btnMisEve)
        btnMisEve.setOnClickListener{
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

        val btnConfigs = findViewById<Button>(R.id.btnConfigs)
        btnConfigs.setOnClickListener {
            val intentConfig = Intent(this, ConfigActivity::class.java)
            startActivity(intentConfig)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val intentCrear = Intent(this, CrearEventoActivity::class.java)
        val intentMisEve = Intent(this, MisEventosActivity::class.java)
        val intentCalen = Intent(this, CalendarActivity::class.java)
        val intentProfile = Intent(this, ProfileActivity::class.java)
        val intentConfig = Intent(this, ConfigActivity::class.java)
        when (item.itemId){
            R.id.nav_itemNuevo -> startActivity(intentCrear)
            R.id.nav_itemMisEve -> startActivity(intentMisEve)
            R.id.nav_itemCalendario -> startActivity(intentCalen)
            R.id.nav_itemPerfil -> startActivity(intentProfile)
            R.id.nav_itemConfig -> startActivity(intentConfig)
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
