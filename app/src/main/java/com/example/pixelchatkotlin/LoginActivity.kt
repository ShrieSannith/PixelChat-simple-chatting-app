package com.example.pixelchatkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.sign

class LoginActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var signupinstead: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        signupinstead = findViewById(R.id.signupinstead)

        signupinstead.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        login.setOnClickListener{
            val email = username.text.toString()
            val pswrd = password.text.toString()
            login_method(email,pswrd)
        }


    }

    private fun login_method(email:String, pswrd:String){
        auth.signInWithEmailAndPassword(email, pswrd)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(this@LoginActivity, "User Not Found", Toast.LENGTH_SHORT).show()
                }
            }
    }
}