package com.example.snapchatclone

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    var passwordEditText: EditText? = null
    var emailEditText: EditText? = null
    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        if (auth.currentUser != null){
            logIn()
        }
    }

    fun goClicked(view: View){
        auth.signInWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    logIn()
                } else {
                    auth.createUserWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString()).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful){
                            Firebase.database.reference.child("users").child(task.result!!.user!!.uid).child("email").setValue(emailEditText?.text.toString())
                            logIn()
                        } else {
                            Toast.makeText(this, "Login failed. Try again", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
    }

    fun logIn(){
        val intent = Intent(this, SnapsActivity::class.java)
        startActivity(intent)
    }
}