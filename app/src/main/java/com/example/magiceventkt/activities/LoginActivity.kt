package com.example.magiceventkt.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.magiceventkt.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val btnInicioSesion = findViewById<Button>(R.id.btnInicioSesion)
        val usuario = findViewById<TextInputEditText>(R.id.toETUsuario)
        val contrasena = findViewById<TextInputEditText>(R.id.toETContrasena)

        btnInicioSesion.setOnClickListener {
            val email = usuario.text.toString()
            val password = contrasena.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                signIn(email, password)
            }
        }

        val btnCreaCuenta = findViewById<Button>(R.id.btnRegistrate)
        btnCreaCuenta.setOnClickListener {
            val intentC = Intent(this, signUpActivity::class.java)
            startActivity(intentC)
        }
    }

    private fun signIn(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Bienvenid@ a Evento Mágico!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, menuInicio::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Error en la autentificación", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
