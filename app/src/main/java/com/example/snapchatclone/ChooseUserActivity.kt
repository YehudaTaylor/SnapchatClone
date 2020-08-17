package com.example.snapchatclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.FirebaseDatabase
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError


class ChooseUserActivity() : AppCompatActivity(), Parcelable {

    private var chooseUserListView: ListView? = null
    var emails: ArrayList<String> = ArrayList()
    var keys: ArrayList<String> = ArrayList()

    constructor(parcel: Parcel) : this() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_user)

        chooseUserListView = findViewById(R.id.chooseUserListView)
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,emails)
        chooseUserListView?.adapter = adapter

        FirebaseDatabase.getInstance().reference.child("users").addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val email = snapshot?.child("email")?.value as String
                emails.add(email)
                snapshot.key?.let { keys.add(it) }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                println(error)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

        })

        chooseUserListView?.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val snapMap: Map<String, String> = mapOf("from" to FirebaseAuth.getInstance().currentUser!!.email!!,
                "imageName" to intent.getStringExtra("imageName"),
                "imageURL" to intent.getStringExtra("imageURL"),
                "message" to intent.getStringExtra("message"))

            FirebaseDatabase.getInstance().reference.child("users").child(keys[i]).child("snaps").push().setValue(snapMap)

            val intent = Intent(this, SnapsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChooseUserActivity> {
        override fun createFromParcel(parcel: Parcel): ChooseUserActivity {
            return ChooseUserActivity(parcel)
        }

        override fun newArray(size: Int): Array<ChooseUserActivity?> {
            return arrayOfNulls(size)
        }
    }
}