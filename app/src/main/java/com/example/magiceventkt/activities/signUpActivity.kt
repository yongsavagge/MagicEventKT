package com.example.magiceventkt.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.magiceventkt.R
import com.google.firebase.auth.FirebaseAuth

class signUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnGuardarRegistro: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Inicialización de Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Obtener referencias a los elementos de la interfaz de usuario
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnGuardarRegistro = findViewById(R.id.btnGuardarRegistro)

        // Configuración del click listener para el botón de registro
        btnGuardarRegistro.setOnClickListener {
            // Obtener el correo electrónico y la contraseña ingresados por el usuario
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            // Verificar que ambos campos no estén vacíos
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Intento de registro de usuario en Firebase
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Registro exitoso, mostrar mensaje de éxito y redirigir al usuario a la actividad de inicio de sesión
                            Toast.makeText(this, "Te has registrado con éxito!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            // Error en el registro, mostrar mensaje de error
                            Toast.makeText(this, "Ha ocurrido un error en el registro", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}
