package com.example.magiceventkt.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.magiceventkt.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    // Conexion a base de datos
    private lateinit var auth: FirebaseAuth;
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val currentUser = auth.currentUser
        //Inicializar las variables de botones y editores de texto en la vista de @Login
        val btnInicioSesion = findViewById<Button>(R.id.btnInicioSesion)

        val usuario = findViewById<TextInputEditText>(R.id.userEditText)

        val contrasena = findViewById<TextInputEditText>(R.id.pswEditText)

        btnInicioSesion.setOnClickListener {
            signIn(usuario.text.toString(), contrasena.text.toString())
        }

        val btnCreaCuenta = findViewById<Button>(R.id.btnRegistrate)
        btnCreaCuenta.setOnClickListener {
            val intentC = Intent(this, signUpActivity::class.java)
            startActivity(intentC)
        }
    }

    // Validación de los elementos entregados por el usuario y arrojar un error en caso de que sean erróneos
    // En caso de que sean ingresados correctamente, se cambia de vista a la de @MenuInicio.
    private fun signIn(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){
            task ->
            if (task.isSuccessful){
                Toast.makeText(this, "Bienvenid@ a Evento Mágico!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, menuInicio::class.java)
                startActivity(intent)
            } else
                Toast.makeText(this, "Error en la autentificación", Toast.LENGTH_SHORT).show()
        }
    }
}