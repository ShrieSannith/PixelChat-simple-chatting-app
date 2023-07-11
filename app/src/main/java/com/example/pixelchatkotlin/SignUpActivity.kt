package com.example.pixelchatkotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app


class SignUpActivity : AppCompatActivity() {

    private lateinit var txtname: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var signup: Button


    private lateinit var auth: FirebaseAuth
    private lateinit var database:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        auth = Firebase.auth


        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        signup = findViewById(R.id.signup)
        txtname = findViewById(R.id.name)

        signup.setOnClickListener{
            val name = txtname.text.toString()
            val email = username.text.toString()
            val pswrd = password.text.toString()
            signup_method(name,email,pswrd)
        }
    }
    private fun signup_method(name: String, email: String, pswrd: String){
        auth.createUserWithEmailAndPassword(email, pswrd).addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    addUserToDatabase(name,email,auth.currentUser?.uid!!)
                    val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {

                    Toast.makeText(this@SignUpActivity, "Error" + it.exception, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String){
        database = FirebaseDatabase.getInstance().getReference()
        database.child("user").child(uid).setValue(User(name,email,uid))
    }
}