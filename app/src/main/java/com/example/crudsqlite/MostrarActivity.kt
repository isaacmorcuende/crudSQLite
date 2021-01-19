package com.example.crudsqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_mostrar.*

class MostrarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar)

        val admin = AdminSQLiteOpenHelper(this,"administracion",null,1)
        val bd = admin.readableDatabase

        val fila = bd.rawQuery("select codigo,nombre,cantante from discos",null)

        val items = mutableListOf<String>()

        while (fila.moveToNext()) {
            val item = "${fila.getString(0)}, ${fila.getString(1)} \n ${fila.getString(2)}"
            items.add(item)
        }

        bd.close()

        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, items)
        lista_mostrar.adapter = adapter

    }
}