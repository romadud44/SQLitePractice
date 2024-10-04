package com.example.sqlitepractice


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sqlitepractice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var idET: EditText
    private lateinit var nameET: EditText
    private lateinit var weightET: EditText
    private lateinit var priceET: EditText
    private var listAdapter: MyListAdapter? = null
    private val dataBase = DBHelper(this@MainActivity)
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbarMain)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
        binding.saveBTN.setOnClickListener {
            saveRecord()
        }

    }

    override fun onResume() {
        super.onResume()
        binding.editBTN.setOnClickListener {
            updateRecord()
        }
        binding.removeBTN.setOnClickListener {
            deleteRecord()
        }
    }

    private fun deleteRecord() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.delete_dialog, null)
        dialogBuilder.setView(dialogView)
        val choiceDeleteId = dialogView.findViewById<EditText>(R.id.deleteIdET)
        dialogBuilder.setTitle("Удалить запись")
        dialogBuilder.setMessage("Введите id")
        dialogBuilder.setPositiveButton("Удалить") { _, _ ->
            val deleteId = choiceDeleteId.text.toString()
            if (deleteId.trim() != "") {
                val product = Product(Integer.parseInt(deleteId), "", 0, 0)
                dataBase.deleteProduct(product)
                viewDataAdapter()
                Toast.makeText(applicationContext, "Данные удалены", Toast.LENGTH_LONG).show()
            }
        }
        dialogBuilder.setNegativeButton("Отмена") { _, _ ->
        }
        dialogBuilder.create().show()
    }

    private fun updateRecord() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog, null)
        dialogBuilder.setView(dialogView)
        val editId = dialogView.findViewById<EditText>(R.id.updateIdET)
        val editName = dialogView.findViewById<EditText>(R.id.updateNameET)
        val editWeight = dialogView.findViewById<EditText>(R.id.updateWeightET)
        val editPrice = dialogView.findViewById<EditText>(R.id.updatePriceET)
        dialogBuilder.setTitle("Обновить запись")
        dialogBuilder.setMessage("Введите данные ниже")
        dialogBuilder.setPositiveButton("Обновить") { _, _ ->
            val updateID = editId.text.toString()
            val updateName = editName.text.toString()
            val updateWeight = editWeight.text.toString()
            val updatePrice = editPrice.text.toString()
            if (updateID.trim() != "" && updateName.trim() != "" && updateWeight.trim() != "" && updatePrice != "") {
                val product = Product(
                    Integer.parseInt(updateID),
                    updateName,
                    Integer.parseInt(updateWeight),
                    Integer.parseInt(updatePrice)
                )
                dataBase.updateProduct(product)
                viewDataAdapter()
                Toast.makeText(applicationContext, "Данные обновлена", Toast.LENGTH_LONG).show()
            }
        }
        dialogBuilder.setNegativeButton("Отмена") { _, _ ->

        }
        dialogBuilder.create().show()


    }

    private fun viewDataAdapter() {
        Product.products = dataBase.readProduct()
        listAdapter = MyListAdapter(this, Product.products)
        binding.listViewLV.adapter = listAdapter
        listAdapter?.notifyDataSetChanged()
    }

    private fun saveRecord() {
        val id = idET.text.toString()
        val name = nameET.text.toString()
        val weight = weightET.text.toString()
        val price = priceET.text.toString()
        if (id.trim() != "" && name.trim() != "" && weight.trim() != "" && price.trim() != "") {
            val product = Product(
                Integer.parseInt(id),
                name,
                Integer.parseInt(weight),
                Integer.parseInt(price)
            )
            Product.products.add(product)
            dataBase.addProduct(product)
            Toast.makeText(applicationContext, "Продукт добавлен", Toast.LENGTH_LONG).show()
            clearFields()
            viewDataAdapter()
        }
    }

    private fun clearFields() {
        idET.text.clear()
        nameET.text.clear()
        weightET.text.clear()
        priceET.text.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenuMain -> {
                finishAndRemoveTask()
                finishAffinity()
                finish()
                Toast.makeText(this, "Программа завершена", Toast.LENGTH_LONG).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        // Не понял как добавить в биндинг лэйаут table_panel
        idET = findViewById(R.id.iDET)
        nameET = findViewById(R.id.nameET)
        weightET = findViewById(R.id.weightET)
        priceET = findViewById(R.id.priceET)
        viewDataAdapter()
    }
}