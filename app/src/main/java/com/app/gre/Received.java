package com.app.gre;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Received extends AppCompatActivity {

    DatabaseReference data;
    SendNotifications notify;
    ListView listView;
    public ArrayList<String> feed = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received);

        notify = new SendNotifications();
        //data = FirebaseDatabase.getInstance().getReference().child("SendNotifications");

        listView = findViewById(R.id.clist);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference usersdRef = rootRef.child("SendNotifications");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String cid = ds.child("comid").getValue(String.class);
                    String un = ds.child("category").getValue(String.class);
                    String em = ds.child("prblm").getValue(String.class);
                    String mno = ds.child("status").getValue(String.class);
                    String dp = ds.child("descp").getValue(String.class);
                    String msg = ds.child("message").getValue(String.class);

                    Log.d("TAG",cid);
                    Log.d("TAG", un);
                    Log.d("TAG", em);
                    Log.d("TAG", mno);
                    Log.d("TAG", dp);

                    feed.add("\n"+"Complaint ID :"+ cid +"\n"+"Category :" +un +"\n"+"Problem :" +em +"\n"+"Status :" +mno +"\n"+ "Description :" +dp +"\n"+ "Reply :"+msg+"\n");
                    //feed.add("REPLY");

                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Received.this,android.R.layout.simple_list_item_1,feed);
                listView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        usersdRef.addListenerForSingleValueEvent(eventListener);
    }
}

