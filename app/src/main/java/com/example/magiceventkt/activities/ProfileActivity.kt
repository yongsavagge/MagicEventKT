package com.example.magiceventkt.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.magiceventkt.R
import com.example.magiceventkt.models.PerfilModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSave: Button
    private lateinit var btnCloseSession: Button
    private lateinit var dbRef: DatabaseReference
    private var isEditMode = false
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etPhone = findViewById(R.id.etPhone)
        etEmail = findViewById(R.id.etEmail)
        btnSave = findViewById(R.id.btnSave)
        btnCloseSession = findViewById(R.id.btnCloseSession)

        dbRef = FirebaseDatabase.getInstance().getReference("PerfilUsuario")
        sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)

        // Leer datos del perfil y mostrarlos al iniciar la actividad
        leerDatosPerfil()
        isEditMode = false

        btnSave.setOnClickListener {
            if (isEditMode) {
                guardarCambios()
            } else {
                habilitarEdicion()
            }
        }

        btnCloseSession.setOnClickListener {
            // Crear un AlertDialog de confirmación
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

    private fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(this, "Has cerrado sesión exitosamente!", Toast.LENGTH_SHORT).show()
        val intentLogin = Intent(this, LoginActivity::class.java)
        startActivity(intentLogin)
        finish()
    }

    private fun guardarCambios() {
        val firstName = etFirstName.text.toString()
        val lastName = etLastName.text.toString()
        val phone = etPhone.text.toString()
        val email = etEmail.text.toString()

        if (firstName.isEmpty()) {
            etFirstName.error = "Por favor, ingresa el nombre del usuario"
            return
        }
        if (lastName.isEmpty()) {
            etLastName.error = "Por favor, ingresa el apellido del usuario"
            return
        }
        if (phone.isEmpty()) {
            etPhone.error = "Por favor, ingresa el número del usuario"
            return
        }
        if (email.isEmpty()) {
            etEmail.error = "Por favor, ingresa el correo del usuario"
            return
        }

        val usuarioID = "usuarioID" // Reemplaza con el ID real del usuario actual

        val usuario = PerfilModel(usuarioID, firstName, lastName, phone, email)

        dbRef.child(usuarioID).setValue(usuario).addOnCompleteListener {
            Toast.makeText(this, "El perfil ha sido actualizado con éxito!", Toast.LENGTH_SHORT).show()
            deshabilitarEdicion()

            // Guardar los datos en SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("firstName", firstName)
            editor.putString("lastName", lastName)
            editor.putString("phone", phone)
            editor.putString("email", email)
            editor.apply()
        }.addOnFailureListener { err ->
            Toast.makeText(this, "Favor, revisa los datos ingresados", Toast.LENGTH_SHORT).show()
        }
    }

    private fun leerDatosPerfil() {
        val usuarioID = "usuarioID" // Reemplaza con el ID real del usuario actual

        dbRef.child(usuarioID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val perfilModel = dataSnapshot.getValue(PerfilModel::class.java)
                    mostrarDatosDePerfil(perfilModel)
                } else {
                    // Si no hay datos en la base de datos, establece los campos de texto en blanco
                    mostrarDatosDePerfil(null)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@ProfileActivity, "Error al leer los datos del perfil", Toast.LENGTH_SHORT).show()
            }
        })

        // Cargar los datos almacenados en SharedPreferences con operador ?.
        val firstName = sharedPreferences.getString("firstName", "")
        val lastName = sharedPreferences.getString("lastName", "")
        val phone = sharedPreferences.getString("phone", "")
        val email = sharedPreferences.getString("email", "")
        mostrarDatosDePerfil(PerfilModel("usuarioID", firstName, lastName, phone, email))
    }

    private fun habilitarEdicion() {
        isEditMode = true
        etFirstName.isEnabled = true
        etLastName.isEnabled = true
        etPhone.isEnabled = true
        etEmail.isEnabled = true
        btnSave.text = "Guardar"
    }

    private fun deshabilitarEdicion() {
        isEditMode = false
        etFirstName.isEnabled = false
        etLastName.isEnabled = false
        etPhone.isEnabled = false
        etEmail.isEnabled = false
        btnSave.text = "Editar"
    }

    private fun mostrarDatosDePerfil(perfilModel: PerfilModel?) {
        etFirstName.setText(perfilModel?.firstName ?: "")
        etLastName.setText(perfilModel?.lastName ?: "")
        etPhone.setText(perfilModel?.phone ?: "")
        etEmail.setText(perfilModel?.email ?: "")
    }
}
