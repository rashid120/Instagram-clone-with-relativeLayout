package com.example.instagram

import android.annotation.SuppressLint
//import android.content.Context
import android.content.Intent
//import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth


// With FirebaseAuth
class LoginActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.loginUsername)
        val password = findViewById<EditText>(R.id.loginPassword)
        val loginBtn: Button = findViewById(R.id.loginBtn)

        loginBtn.setOnClickListener {
            if (email.text.isEmpty() && password.text.isEmpty()) {

                Toast.makeText(this, "Please add your information", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            login(email.text.toString(), password.text.toString())
        }

        val newAccount: TextView = findViewById(R.id.newAccount)
        newAccount.setOnClickListener {
            newAccount()
        }

        if (auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }
    @SuppressLint("MissingInflatedId", "InflateParams")
    //save email, password to firebaseAuth
    fun newAccount() {

        val bottomSheetDialog = BottomSheetDialog(this)
        val layout = LayoutInflater.from(this).inflate(R.layout.sign_up, null)

        val newUsername = layout.findViewById<EditText>(R.id.signUpUsername)
        val newPassword = layout.findViewById<EditText>(R.id.signUpPassword)
        val signUpBtn = layout.findViewById<Button>(R.id.signUpBtn)
        bottomSheetDialog.setContentView(layout)

        signUpBtn.setOnClickListener {
            if (newPassword.text.isEmpty() && newUsername.text.isEmpty()) {
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(newUsername.text.toString(), newPassword.text.toString()).addOnSuccessListener {

                    val id = it.user?.uid
                    val username = it.user?.email

                    Toast.makeText(this, "Successfully created your account please login $id", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    //Login
    private fun login(email : String, password : String){

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {

                Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener {

                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }

}


// with getSharedPreferences
/*
class LoginActivity : AppCompatActivity() {

    lateinit var loginDataStorage: SharedPreferences
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.loginUsername)
        val password = findViewById<EditText>(R.id.loginPassword)
        val loginBtn: Button = findViewById(R.id.loginBtn)

        loginDataStorage = getSharedPreferences("loginDetails", Context.MODE_PRIVATE)

        loginBtn.setOnClickListener {

            if (username.text.isEmpty()) {
                return@setOnClickListener
            }

            // get username and password from loginDataStorage
            val getUsername = loginDataStorage.getString("username", "invalid")
            val getPassword = loginDataStorage.getString("password", "invalid")

            //username == username and password == password
            val condition = username.text.toString() == getUsername.toString() && password.text.toString() == getPassword.toString()

            if (condition) {

                val edit = loginDataStorage.edit()
                edit.putBoolean("loginStatus", true).apply()

                //if condition is true then go to MainActivity
                startActivity(Intent(this, MainActivity::class.java))
                finish()

            } else if (username.text.toString() != getUsername.toString()) {
                Toast.makeText(this, "Wrong Username", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show()
            }
        }

        val newAccount: TextView = findViewById(R.id.newAccount)
        newAccount.setOnClickListener {
            newAccount()
        }

        val check = loginDataStorage.getBoolean("loginStatus", false)

        if (check){

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

    @SuppressLint("MissingInflatedId")

    //save username, password and loginStatus to local storage
    fun newAccount() {

        val bottomSheetDialog = BottomSheetDialog(this)
        val layout = LayoutInflater.from(this).inflate(R.layout.sign_up, null)

        val newUsername = layout.findViewById<EditText>(R.id.signUpUsername)
        val newPassword = layout.findViewById<EditText>(R.id.signUpPassword)
        val signUpBtn = layout.findViewById<Button>(R.id.signUpBtn)

        bottomSheetDialog.setContentView(layout)

        signUpBtn.setOnClickListener {

            if (newPassword.text.isEmpty() && newUsername.text.isEmpty()) {
                return@setOnClickListener
            }

            val addNew = loginDataStorage.edit()

            addNew.putString("username", newUsername.text.toString())
            addNew.putString("password", newPassword.text.toString()).apply()

            Toast.makeText(this, "Successfully created your account please login", Toast.LENGTH_SHORT).show()
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

}*/
