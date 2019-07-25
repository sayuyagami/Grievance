package com.app.gre;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Validate extends AppCompatActivity {

    boolean checked = false;
    DatabaseReference data,viewadmin;
    Viewreplies replies;
    SendNotifications notify;
    String[] listitems;
    ListView listView;
    private String m_Text = "";
    public ArrayList<String> feed = new ArrayList<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate);

        notify = new SendNotifications();
        data = FirebaseDatabase.getInstance().getReference().child("SendNotifications");
        viewadmin = FirebaseDatabase.getInstance().getReference().child("Viewreplies");
        replies = new Viewreplies();

        listView = findViewById(R.id.clist);
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference usersdRef = rootRef.child("Complaintdetails");
        final ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    final Integer cid = ds.child("complaintid").getValue(int.class);
                    String un = ds.child("category").getValue(String.class);
                    String dt = ds.child("datetym").getValue(String.class);
                    String em = ds.child("problem").getValue(String.class);
                    String mno = ds.child("mno").getValue(String.class);
                    String dp = ds.child("descp").getValue(String.class);

                    Log.d("TAG", String.valueOf(cid));
                    Log.d("TAG", un);
                    Log.d("TAG", em);
                    Log.d("TAG", mno);
                    Log.d("TAG", dp);

                    feed.add("\n"+dt+"\n\n"+"Complaint ID :"+ cid +"\n"+"Category :" +un +"\n"+"Problem :" +em +"\n"+"Mobile no :" +mno +"\n"+ "Description :" +dp +"\n"+ "");
                    feed.add("REPLY");

                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Validate.this,android.R.layout.simple_list_item_1,feed);
                listView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        usersdRef.addListenerForSingleValueEvent(eventListener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i,
                                    final long id) {
                final String listPosition = feed.get(i);
                if (i%2 != 0){
                    final AlertDialog.Builder valid = new AlertDialog.Builder(Validate.this);
                    listitems = new String[]{"Valid","Invalid"};
                    valid.setSingleChoiceItems(listitems, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0){
                                checked = true;
                                dialog.dismiss();

                                Toast.makeText(Validate.this,"This is set to be valid",Toast.LENGTH_LONG).show();
                                if (checked == true) {
                                    DatabaseReference userRef = rootRef.child("Complaintdetails");
                                    ValueEventListener event = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                final Integer cid = ds.child("complaintid").getValue(int.class);
                                                final String un = ds.child("category").getValue(String.class);
                                                final String em = ds.child("problem").getValue(String.class);
                                                final String dt = ds.child("datetym").getValue(String.class);
                                                final String dcp = ds.child("descp").getValue(String.class);
                                                final String phn = ds.child("mno").getValue(String.class);

                                                //final Button replybtn = feed.get(position);
                                                final AlertDialog.Builder builder = new AlertDialog.Builder(Validate.this);
                                                builder.setTitle("Send Reply");
                                                builder.setMessage("Complaint ID :" + cid + "\n" + "Category :" + un + "\n" + "Problem :" + em + "\n");

                                                // Set up the input
                                                final EditText input = new EditText(Validate.this);
                                                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                                                input.setText("");
                                                builder.setView(input);

                                                // Set up the buttons
                                                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int listposition) {
                                                        m_Text = input.getText().toString().trim();
                                                        //String compid = listPosition;

                                                        notify.setComid(cid);
                                                        notify.setCategory(un);
                                                        notify.setPrblm(em);
                                                        notify.setDescp(dcp);
                                                        notify.setStatus("Request accepted");
                                                        notify.setMessage(m_Text);

                                                        replies.setComid(cid);
                                                        replies.setCategory(un);
                                                        replies.setPrblm(em);
                                                        replies.setDescp(dcp);
                                                        replies.setDate(dt);
                                                        replies.setMno(phn);
                                                        replies.setStatus("Request accepted");
                                                        replies.setMessage(m_Text);

                                                        data.child(phn).child(String.valueOf(cid)).setValue(notify);
                                                        viewadmin.child(String.valueOf(cid)).setValue(replies);
                                                        Toast.makeText(Validate.this, "Reply Sent Successfully", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                });

                                                builder.show();

                                            }
                                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Validate.this, android.R.layout.simple_list_item_1, feed);
                                            listView.setAdapter(arrayAdapter);
                                            arrayAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    };userRef.addListenerForSingleValueEvent(event);
                                }

                            }else {
                                checked = false;
                                final DatabaseReference dreff = FirebaseDatabase.getInstance().getReference().child("Invalid");
                                final Invalid invinfo = new Invalid();
                                Toast.makeText(Validate.this,"This is set to be invalid",Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                ValueEventListener event = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            final Integer cid = ds.child("complaintid").getValue(int.class);
                                            final String un = ds.child("category").getValue(String.class);
                                            final String em = ds.child("problem").getValue(String.class);
                                            final String desp = ds.child("descp").getValue(String.class);
                                            final String phn = ds.child("mno").getValue(String.class);
                                            final String dt = ds.child("datetym").getValue(String.class);

                                            final String set = "Invalid";

                                            //final Button replybtn = feed.get(position);
                                            final AlertDialog.Builder builder = new AlertDialog.Builder(Validate.this);
                                            builder.setTitle("Send To Admin");
                                            builder.setMessage("Complaint ID :" + cid + "\n" + "Category :" + un + "\n" + "Problem :" + em + "\n"+"Set : "+set+"\n");

                                            // Set up the buttons
                                            builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int listposition) {
                                                    String compid = listPosition;

                                                    invinfo.setComid(cid);
                                                    invinfo.setCategory(un);
                                                    invinfo.setPrblm(em);
                                                    invinfo.setDescrip(desp);
                                                    invinfo.setMessage(set);
                                                    invinfo.setDate(dt);
                                                    invinfo.setMno(phn);

                                                    dreff.child(String.valueOf(cid)).setValue(invinfo);
                                                    Toast.makeText(Validate.this, "Reply Sent Successfully", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });

                                            builder.show();

                                        }
                                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Validate.this, android.R.layout.simple_list_item_1, feed);
                                        listView.setAdapter(arrayAdapter);
                                        arrayAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                };usersdRef.addListenerForSingleValueEvent(event);
                            }
                        }
                    });
                    AlertDialog checkdata = valid.create();
                    checkdata.show();
                   //usersdRef.addListenerForSingleValueEvent(eventListener);
                }
            }
        });
    }
}
