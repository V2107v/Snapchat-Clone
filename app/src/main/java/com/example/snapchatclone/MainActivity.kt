package com.example.snapchatclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {

    var emailEditText : EditText? = null
    var passwordEditText : EditText? = null
    val mAuth  = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("Login/SignUp")

        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)

        if(mAuth.currentUser != null) {
           login()
        }

    }

    fun go (view:View) {
        //check if we can login the user, or else sign up.
        mAuth.signInWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"Sign In Success!!",Toast.LENGTH_SHORT).show()
                    login()
                } else {
                   //Sign up
                    mAuth.createUserWithEmailAndPassword(emailEditText?.text.toString(),passwordEditText?.text.toString())
                        .addOnCompleteListener(this) {task ->
                        if(task.isSuccessful) {
                            Toast.makeText(this,"Sign Up Success!!",Toast.LENGTH_SHORT).show()
                            task.result?.user?.uid?.let {
                                FirebaseDatabase.getInstance().getReference().child("users").child(it).child("email").setValue(emailEditText?.text.toString())
                            }
                            login()
                        } else {
                            Toast.makeText(this,"Try Again!!",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }

    fun login() {
        // Move to next Activity
        val intent = Intent(this,SnapsActivity::class.java)
        startActivity(intent)
    }
}
