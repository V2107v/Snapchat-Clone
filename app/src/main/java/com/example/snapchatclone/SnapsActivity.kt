package com.example.snapchatclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class SnapsActivity : AppCompatActivity() {

    val mAuth  = FirebaseAuth.getInstance()
    var snapListView : ListView? = null
    var emails : ArrayList<String> = ArrayList()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item?.itemId == R.id.createSnap) {

            val intent = Intent(this,CreateSnapActivity::class.java)
            startActivity(intent)

        } else if (item?.itemId == R.id.logout) {

            mAuth.signOut()
            Toast.makeText(applicationContext,"Logged Out",Toast.LENGTH_SHORT).show()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mAuth.signOut()
        Toast.makeText(applicationContext,"Logged Out",Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snaps)

        setTitle("Snaps")

        snapListView = findViewById(R.id.myListView)
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,emails)
        snapListView?.adapter = adapter

        mAuth.currentUser?.uid?.let {
            FirebaseDatabase.getInstance().getReference().child("users").child(it).child("snaps").addChildEventListener(object: ChildEventListener{
                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    emails.add(p0.child("from").value as String)
                    adapter.notifyDataSetChanged()
                }
                override fun onCancelled(p0: DatabaseError) {}
                override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
                override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
                override fun onChildRemoved(p0: DataSnapshot) {}
            })
        }
    }

}
