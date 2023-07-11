package com.example.pixelchatkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeActivity : AppCompatActivity() {

    private lateinit var userrecyclerview: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var auth:FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        userList = ArrayList()
        adapter = UserAdapter(this, userList)
        userrecyclerview = findViewById(R.id.recyclerview)
        database = FirebaseDatabase.getInstance().getReference()

        userrecyclerview.layoutManager = LinearLayoutManager(this)
        userrecyclerview.adapter=adapter

        database.child("user").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(postsnapshot in snapshot.children){
                    val currentUser = postsnapshot.getValue(User::class.java)
                    if(auth.currentUser?.uid!=currentUser?.uid) {
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            //log out
            auth.signOut()
            finish()
            return true
        }
        return true
        return super.onOptionsItemSelected(item)
    }
}
