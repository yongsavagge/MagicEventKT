package com.example.magiceventkt.models

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.magiceventkt.R
import com.example.magiceventkt.activities.MisEventosActivity
import com.google.firebase.database.FirebaseDatabase

class editActivity : AppCompatActivity() {

    // Declaración de variables miembro
    private lateinit var tvNombre: TextView
    private lateinit var tvDesc: TextView
    private lateinit var tvFecha: TextView
    private lateinit var tvUbicacion: TextView
    private lateinit var btnEditar: Button
    private lateinit var btnEliminarEvento : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        // Inicialización de vistas y botones
        tvNombre = findViewById(R.id.tvNombre1)
        tvDesc = findViewById(R.id.tvDesc1)
        tvFecha = findViewById(R.id.tvFecha1)
        tvUbicacion = findViewById(R.id.tvUbicacion1)
        btnEditar = findViewById(R.id.btnEditar)
        btnEliminarEvento = findViewById(R.id.btnEliminarEvento)

        // Configuración del clic en el botón "Editar"
        btnEditar.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("eventoID").toString(),
                intent.getStringExtra("nombreEvento").toString()
            )
        }

        btnEliminarEvento.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("eventoID").toString()
            )
        }

        // Inicialización y configuración de las vistas con datos iniciales
        initView()
        setValuesToViews()
    }

    private fun deleteRecord(
        eventoID : String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("CreacionEvento").child(eventoID)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Evento borrado exitosamente", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MisEventosActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{
            error -> Toast.makeText(this, "Error al borrar el evento", Toast.LENGTH_SHORT).show()
        } 
    }

    // Función para inicializar vistas adicionales (puede dejarse vacía)
    private fun initView() {
        // Puede añadirse lógica de inicialización de vistas aquí si es necesario.
    }

    // Función para establecer valores iniciales en las vistas
    private fun setValuesToViews() {
        tvNombre.text = intent.getStringExtra("nombreEvento")
        tvDesc.text = intent.getStringExtra("desc")
        tvFecha.text = intent.getStringExtra("fecha")
        tvUbicacion.text = intent.getStringExtra("ubicacion")
    }

    // Función para abrir un cuadro de diálogo de actualización
    private fun openUpdateDialog(eventoID: String, nombreEvento: String) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.edit_info_event, null)
        mDialog.setView(mDialogView)
        val etNombre = mDialogView.findViewById<EditText>(R.id.edNombre)
        val etDesc = mDialogView.findViewById<EditText>(R.id.edDesc)
        val etFecha = mDialogView.findViewById<EditText>(R.id.edFecha)
        val etUbicacion = mDialogView.findViewById<EditText>(R.id.edUbicacion)
        val btnGuardar = mDialogView.findViewById<Button>(R.id.btnGuardar)

        // Establecer valores iniciales en el cuadro de diálogo
        etNombre.setText(intent.getStringExtra("nombreEvento").toString())
        etDesc.setText(intent.getStringExtra("desc").toString())
        etFecha.setText(intent.getStringExtra("fecha").toString())
        etUbicacion.setText(intent.getStringExtra("ubicacion").toString())

        val alertDialog = mDialog.create()
        alertDialog.show()

        // Configurar el clic en el botón "Guardar" dentro del cuadro de diálogo
        btnGuardar.setOnClickListener {
            updateEventData(
                eventoID,
                etNombre.text.toString(),
                etDesc.text.toString(),
                etFecha.text.toString(),
                etUbicacion.text.toString()
            )

            // Cerrar el cuadro de diálogo y mostrar un mensaje Toast de confirmación
            alertDialog.dismiss()
            Toast.makeText(applicationContext, "Evento actualizado", Toast.LENGTH_SHORT).show()

            // Actualizar las vistas principales con los nuevos valores
            tvNombre.text = etNombre.text.toString()
            tvDesc.text = etDesc.text.toString()
            tvFecha.text = etFecha.text.toString()
            tvUbicacion.text = etUbicacion.text.toString()
        }
    }

    // Función para actualizar los datos del evento en la base de datos
    private fun updateEventData(
        eventoID: String,
        nombreEvento: String,
        desc: String,
        fecha: String,
        ubicacion: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("CreacionEvento").child(eventoID)
        val eventInfo = EventoModel(eventoID, nombreEvento, desc, fecha, ubicacion)
        dbRef.setValue(eventInfo)
    }
}
