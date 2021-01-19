package com.example.crudsqlite

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import kotlinx.android.synthetic.main.activity_main.*;

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        boton_alta.setOnClickListener{

            if(et_codigo.text.isNotEmpty() && et_nombre.text.isNotEmpty() && et_cantante.text.isNotEmpty()){
                val admin = AdminSQLiteOpenHelper(this,"administracion",null,1)
                val bd = admin.writableDatabase

                val registro = ContentValues()
                registro.put("codigo", et_codigo.text.toString())
                registro.put("nombre", et_nombre.text.toString())
                registro.put("cantante", et_cantante.text.toString())
                bd.insert("discos",null,registro)
                bd.close()

                et_codigo.setText("")
                et_nombre.setText("")
                et_cantante.setText("")

                Toast.makeText(this, "Disco añadido",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Introduce todos los campos!",Toast.LENGTH_SHORT).show()
            }

        }

        boton_consulta.setOnClickListener{
            val admin = AdminSQLiteOpenHelper(this,"administracion",null,1)
            val bd = admin.readableDatabase
            val fila = bd.rawQuery("select nombre,cantante from discos where codigo=${et_codigo.text.toString()}",null)

            if(fila.moveToFirst()){
                et_nombre.setText(fila.getString(0))
                et_cantante.setText(fila.getString(1))
            }else{
                Toast.makeText(this, "No existe ningún disco con ese código",Toast.LENGTH_SHORT).show()
                et_nombre.setText("")
                et_cantante.setText("")
            }
            bd.close()
        }

        boton_baja.setOnClickListener{
            val admin = AdminSQLiteOpenHelper(this,"administracion",null,1)
            val bd = admin.writableDatabase

            val fila = bd.rawQuery("select nombre,cantante from discos where codigo=${et_codigo.text.toString()}",null)

            if(fila.moveToFirst()){
                AlertDialog.Builder(this).apply {
                    setTitle("Eliminar disco")
                    setMessage("¿Estás seguro de que quieres eliminar ${fila.getString(0)} de ${fila.getString(1)}?")
                    setPositiveButton("Si") { _: DialogInterface, _: Int ->
                        val cant = bd.delete("discos", "codigo=${et_codigo.text.toString()}",null)
                        bd.close()
                        if(cant==1){
                            Toast.makeText(this@MainActivity, "El disco se ha borrado con éxito", Toast.LENGTH_SHORT).show()
                            et_codigo.setText("")
                        }
                    }
                    setNegativeButton("No"){ _: DialogInterface, _: Int ->
                        bd.close()
                    }
                }.show()
            }else{
                Toast.makeText(this, "No existe ningún disco con ese código",Toast.LENGTH_SHORT).show()
            }
        }

        boton_modificar.setOnClickListener {
            val admin = AdminSQLiteOpenHelper(this,"administracion",null,1)
            val bd = admin.writableDatabase

            val registro = ContentValues()
            registro.put("nombre", et_nombre.text.toString())
            registro.put("cantante", et_cantante.text.toString())

            val cant = bd.update("discos",registro,"codigo=${et_codigo.text}",null)
            bd.close()

            if(cant==1){
                Toast.makeText(this, "Se han modificado los datos",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "No existe ningún disco con ese código",Toast.LENGTH_SHORT).show()
            }
        }

        boton_mostrar.setOnClickListener {
            val intent = Intent(this, MostrarActivity::class.java)
            startActivity(intent)
        }
    }
}