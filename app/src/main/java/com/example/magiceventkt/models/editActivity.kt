package com.example.magiceventkt.models

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.magiceventkt.R
import com.example.magiceventkt.activities.MisEventosActivity
import com.google.firebase.database.FirebaseDatabase

class editActivity : AppCompatActivity() {

    private lateinit var tvNombre: TextView
    private lateinit var tvDesc: TextView
    private lateinit var tvFecha: TextView
    private lateinit var tvUbicacion: TextView
    private lateinit var tvCate: TextView
    private lateinit var btnEditar: Button
    private lateinit var btnEliminarEvento: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        tvNombre = findViewById(R.id.tvNombre1)
        tvDesc = findViewById(R.id.tvDesc1)
        tvFecha = findViewById(R.id.tvFecha1)
        tvUbicacion = findViewById(R.id.tvUbicacion1)
        tvCate = findViewById(R.id.tvCate)
        btnEditar = findViewById(R.id.btnEditar)
        btnEliminarEvento = findViewById(R.id.btnEliminarEvento)

        btnEditar.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("eventoID").toString(),
                intent.getStringExtra("nombreEvento").toString()
            )
        }

        btnEliminarEvento.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Eliminar Evento")
            builder.setMessage(R.string.eliminarApprove)

            builder.setPositiveButton(R.string.approve) { dialog, which ->
                deleteRecord(
                    intent.getStringExtra("eventoID").toString()
                )
            }
            builder.setNegativeButton(R.string.deny) { dialog, which ->
                dialog.dismiss()
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }

        initView()
        setValuesToViews()
    }

    private fun deleteRecord(
        eventoID: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("CreacionEvento").child(eventoID)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Evento borrado exitosamente", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MisEventosActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Error al borrar el evento", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView() {
        // Puede añadirse lógica de inicialización de vistas aquí si es necesario.
    }

    private fun setValuesToViews() {
        tvNombre.text = "Nombre del Evento: " +intent.getStringExtra("nombreEvento")
        tvDesc.text = "Descripción: " + intent.getStringExtra("desc")
        tvFecha.text = "Fecha: " + intent.getStringExtra("fecha")
        tvUbicacion.text = "Ubicación: " + intent.getStringExtra("ubicacion")
        tvCate.text = "Categoria: " + intent.getStringExtra("categoria")
    }

    private fun openUpdateDialog(eventoID: String, nombreEvento: String) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.edit_info_event, null)
        mDialog.setView(mDialogView)

        val etNombre = mDialogView.findViewById<EditText>(R.id.edNombre)
        val etDesc = mDialogView.findViewById<EditText>(R.id.edDesc)
        val etFecha = mDialogView.findViewById<EditText>(R.id.edFecha)
        val etUbicacion = mDialogView.findViewById<EditText>(R.id.edUbicacion)
        val spnCate = mDialogView.findViewById<Spinner>(R.id.spnCatego)
        val btnGuardar = mDialogView.findViewById<Button>(R.id.btnGuardar)

        val categories = arrayOf(
            "Cultural",
            "Deportivo",
            "Corporativo",
            "Educativo",
            "Celebración Social",
            "Religioso",
            "Benéfico",
            "Gastronómico",
            "Tecnológico",
            "Otro"
        )

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spnCate.adapter = spinnerAdapter

        val selectedCategory = intent.getStringExtra("categoria") ?: ""
        val selectedCategoryPosition = categories.indexOf(selectedCategory)
        spnCate.setSelection(selectedCategoryPosition)

        etNombre.setText(intent.getStringExtra("nombreEvento").toString())
        etDesc.setText(intent.getStringExtra("desc").toString())
        etFecha.setText(intent.getStringExtra("fecha").toString())
        etUbicacion.setText(intent.getStringExtra("ubicacion").toString())
        spnCate.setSelection(selectedCategoryPosition)

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnGuardar.setOnClickListener {
            updateEventData(
                eventoID,
                etNombre.text.toString(),
                etDesc.text.toString(),
                etFecha.text.toString(),
                etUbicacion.text.toString(),
                spnCate.selectedItem.toString()
            )

            alertDialog.dismiss()
            Toast.makeText(applicationContext, "Evento actualizado", Toast.LENGTH_SHORT).show()

            tvNombre.text = etNombre.text.toString()
            tvDesc.text = etDesc.text.toString()
            tvFecha.text = etFecha.text.toString()
            tvUbicacion.text = etUbicacion.text.toString()
            tvCate.text = spnCate.selectedItem.toString()
        }
    }


    private fun updateEventData(
        eventoID: String,
        nombreEvento: String,
        desc: String,
        fecha: String,
        ubicacion: String,
        categoria: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("CreacionEvento").child(eventoID)
        val eventInfo = EventoModel(eventoID, nombreEvento, desc, fecha, ubicacion, categoria)
        dbRef.setValue(eventInfo)
    }
}
