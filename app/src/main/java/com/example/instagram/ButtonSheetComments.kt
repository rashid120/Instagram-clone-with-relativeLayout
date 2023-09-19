package com.example.instagram

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ButtonSheetComments : AppCompatActivity() {

    private lateinit var item : ArrayList<String>
    private lateinit var editText: EditText
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_button_sheet_comments)

        val comment = getSharedPreferences("comments", MODE_PRIVATE).getString("firstComment", "Empty")

        item = arrayListOf()
        item.add(comment.toString())
        item.add("Rashid khan")


        val listView : ListView = findViewById(R.id.commentListView)
//        listView.adapter = ArrayAdapter(this, R.layout, item)

        editText = findViewById(R.id.editTextComment)
    }

    fun keyboard(view: View) {
        closeKey()
    }

    private fun closeKey(){
        val view = this.currentFocus
        if (view != null){
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}