package com.example.magiceventkt.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.magiceventkt.models.EventoModel
import com.example.magiceventkt.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CrearEventoActivity : AppCompatActivity() {
    //Inicialización de variables en donde se ingresa el texto en la vista de @CrearEventoXML
    private lateinit var editNombreEvento : EditText
    private lateinit var editDesc : EditText
    private lateinit var editFecha : EditText
    private lateinit var editUbicacion : EditText
    //Inicialización de botones de la vista @CrearEventoXML
    private lateinit var btnGuardarEvento : Button
    private lateinit var btnCancelarEve : Button
    //Inicialización de la referenciación de la base de datos @Firebase
    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_evento)
        // Variables para ingresar datos desde la vista
        editNombreEvento = findViewById(R.id.editNombreEvento)
        editDesc = findViewById(R.id.editDesc)
        editFecha = findViewById(R.id.editFecha)
        editUbicacion = findViewById(R.id.editUbicacion)
        // Inicialización de botones de la vista @CrearEventoXML
        btnGuardarEvento = findViewById(R.id.btnGuardarEvento)
        btnCancelarEve = findViewById(R.id.btnCancelarEve)
        // Establecer una referencia a la base de datos de Firebase
        dbRef = FirebaseDatabase.getInstance().getReference("CreacionEvento")
        // Al hacer click en el botón "Guardar Evento", ejecuta y llama a la función correspondiente
        btnGuardarEvento.setOnClickListener {
            guardarDatosEvento()
        }
    }
    private fun guardarDatosEvento(){
        //Obtener los valores de las variables
        val nombreEvento = editNombreEvento.text.toString()
        val desc = editDesc.text.toString()
        val fecha = editFecha.text.toString()
        val ubicacion = editUbicacion.text.toString()
        //Validaciones de ingreso de datos
        if(nombreEvento.isEmpty()){
            editNombreEvento.error = "Por favor, ingresa el nombre del evento"
        }
        if(desc.isEmpty()){
            editDesc.error = "Por favor, ingresa la descripción del evento"
        }
        if(fecha.isEmpty()){
            editFecha.error = "Por favor, ingresa la fecha del evento"
        }
        if(ubicacion.isEmpty()){
            editUbicacion.error = "Por favor, ingresa la ubicación del evento"
        }
        // Creación de ID única para cada evento, el push hará que se cree un ID única cada vez que
        // se ingrese un nuevo registro
        val eventoID = dbRef.push().key!!
        // Se llena de información el constructor de evento con el objetivo de poder ingresar la información
        //en cada variable creada.
        val evento = EventoModel(eventoID, nombreEvento, desc, fecha, ubicacion)

        dbRef.child(eventoID).setValue(evento).addOnCompleteListener{
            //El toast indicará que el evento ha sido creado con éxito y luego se procederá a borrar los
            //datos en la vista
            Toast.makeText(this, "El evento ha sido agregado con éxito!", Toast.LENGTH_SHORT).show()
            // Una vez introducidos los datos la base de datos, se procederá a borrar la información que están
            // dentro de los contenedores de texto
            editNombreEvento.text.clear()
            editDesc.text.clear()
            editFecha.text.clear()
            editUbicacion.text.clear()
            //En caso de error, la app dará un mensaje de error mediante un Toast
        }.addOnFailureListener{err ->
            Toast.makeText(this, "El evento no ha podido ser creado con éxito", Toast.LENGTH_SHORT).show()

        }

    }
}