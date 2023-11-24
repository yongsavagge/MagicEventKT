package com.example.magiceventkt.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.magiceventkt.R
import com.google.firebase.auth.FirebaseAuth

class cambiarContrasena : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etNuevaContrasena: EditText
    private lateinit var etConfirmarContrasena: EditText
    private lateinit var btnCambiarContrasena: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambiar_contrasena)

        auth = FirebaseAuth.getInstance()

        etNuevaContrasena = findViewById(R.id.etNuevaContrasena)
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena)
        btnCambiarContrasena = findViewById(R.id.btnCambiarContrasena)

        btnCambiarContrasena.setOnClickListener {
            cambiarContrasena()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cambiarContrasena() {
        val nuevaContrasena = etNuevaContrasena.text.toString()
        val confirmarContrasena = etConfirmarContrasena.text.toString()

        if (nuevaContrasena.isNotEmpty() && confirmarContrasena.isNotEmpty()) {
            if (nuevaContrasena == confirmarContrasena) {
                val user = auth.currentUser
                user?.updatePassword(nuevaContrasena)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Contraseña cambiada exitosamente",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Error al cambiar la contraseña",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    this,
                    "Las contraseñas no coinciden",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                this,
                "Por favor, completa todos los campos",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
