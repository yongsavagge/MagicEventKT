package com.example.magiceventkt.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.magiceventkt.R
import com.example.magiceventkt.models.navigationDrawer

class menuInicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_inicio)

        // Se llama cada botón que está en el Layout de @MenuInicio
        // Se crear un ClickListener que es el que escuchará cuando el usuario presione el botón
        // Cuando ocurra ese evento, se cambiará de clase a la que aparezca dentro del Intent.
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
}