package com.app.gre;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Check_status extends AppCompatActivity {

    public ArrayList<String> msg = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_status);
        SharedPreferences share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        final String phnumber = share.getString("phnumber","");

        final ListView listView = (ListView)findViewById(R.id.notify);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = rootRef.child("SendNotifications").child(phnumber);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    final Integer cid = ds.child("comid").getValue(int.class);
                    String cat = ds.child("category").getValue(String.class).trim();
                    String prblm = ds.child("prblm").getValue(String.class).trim();
                    String un = ds.child("message").getValue(String.class).trim();
                    String sta = ds.child("status").getValue(String.class).trim();

                    Log.d("TAG", String.valueOf(cid));
                    Log.d("TAG", cat);
                    Log.d("TAG", prblm);
                    Log.d("TAG", un);

                    msg.add("\n"+"Complaint Id :" +cid+"\n"+"Category : "+cat+"\n"+"Problem Type:"+prblm+"\n"+"Status : "+sta+"\n"+"Message :"+un+"\n");

                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Check_status.this,android.R.layout.simple_list_item_1,msg);
                listView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        usersdRef.addListenerForSingleValueEvent(eventListener);
    }
}
