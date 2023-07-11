package com.example.pixelchatkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ChatActivity : AppCompatActivity() {

    private lateinit var messageRecyclerView: RecyclerView;
    private lateinit var messagebox: EditText
    private lateinit var sendbutton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var database: DatabaseReference

    //privacy policy lol
    var receiverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        database=FirebaseDatabase.getInstance().getReference()
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        senderRoom = receiverUid+senderUid
        receiverRoom = senderUid+receiverUid

        messageList= ArrayList()
        messageAdapter= MessageAdapter(this,messageList)

        supportActionBar?.title=name

        messageRecyclerView = findViewById(R.id.chatrecyclerview)
        messagebox = findViewById(R.id.messagebox)
        sendbutton = findViewById(R.id.send)

        messageRecyclerView.layoutManager=LinearLayoutManager(this)
        messageRecyclerView.adapter=messageAdapter

        //Add data to recycler view
        database.child("chat").child(senderRoom!!).child("messages")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postSnapshot in snapshot.children){

                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }

                    messageAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {

                }

        })

        //adding msg to db
        sendbutton.setOnClickListener{
            val message = messagebox.text.toString()
            val messageObject = Message(message, senderUid)

            database.child("chat").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    database.child("chat").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messagebox.setText("")
        }
    }
}