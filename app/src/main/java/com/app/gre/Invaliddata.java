package com.app.gre;

import android.content.DialogInterface;
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

public class Invaliddata extends AppCompatActivity {

    boolean checked = false;
    String[] listitems;
    DatabaseReference data,viewadmin;
    SendNotifications notify;
    Viewreplies replies;
    String m_Text;
    ListView listView;
    public ArrayList<String> feed = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invaliddata);

        notify = new SendNotifications();
        data = FirebaseDatabase.getInstance().getReference().child("SendNotifications");
        viewadmin = FirebaseDatabase.getInstance().getReference().child("Viewreplies");
        replies = new Viewreplies();

        listView = findViewById(R.id.clist);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference usersdRef = rootRef.child("Invalid");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    final Integer cid = ds.child("comid").getValue(int.class);
                    String dt = ds.child("date").getValue(String.class);
                    String un = ds.child("category").getValue(String.class);
                    String em = ds.child("prblm").getValue(String.class);
                    String dp = ds.child("descrip").getValue(String.class);
                    String msg = ds.child("message").getValue(String.class);

                    Log.d("TAG", String.valueOf(cid));
                    Log.d("TAG", un);
                    Log.d("TAG", em);

                    feed.add("\n"+"Complaint ID :"+ cid +"\n"+"Category :" +un +"\n"+"Problem :" +em +"\n"+ "Description :" +dp +"\n"+ "");
                    feed.add("REPLY");

                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Invaliddata.this,android.R.layout.simple_list_item_1,feed);
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
                    final AlertDialog.Builder valid = new AlertDialog.Builder(Invaliddata.this);
                    listitems = new String[]{"Request Rejected","Request Accepted"};
                    valid.setSingleChoiceItems(listitems, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0){
                                checked = true;
                                dialog.dismiss();

                                if (checked == true) {
                                    ValueEventListener event = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                final Integer cid = ds.child("comid").getValue(int.class);
                                                final String un = ds.child("category").getValue(String.class);
                                                final String dt = ds.child("date").getValue(String.class);
                                                final String em = ds.child("prblm").getValue(String.class);
                                                final String dp = ds.child("descrip").getValue(String.class);
                                                final String msg = ds.child("message").getValue(String.class);
                                                final String phn = ds.child("mno").getValue(String.class);

                                                //final String sta = ds.child("status").getValue(String.class);

                                                //final Button replybtn = feed.get(position);
                                                final AlertDialog.Builder builder = new AlertDialog.Builder(Invaliddata.this);
                                                builder.setTitle("Send Reply");
                                                builder.setMessage("Complaint ID :" + cid + "\n" + "Category :" + un + "\n" + "Problem :" + em + "\n"+"Description :"+dp+"\n"+"Message :"+msg+"\n");

                                                // Set up the buttons
                                                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int listposition) {

                                                        notify.setComid(cid);
                                                        notify.setCategory(un);
                                                        notify.setPrblm(em);
                                                        notify.setDescp(dp);
                                                        notify.setStatus("Request Rejected");
                                                        notify.setMessage("Invalid Complaint");

                                                        replies.setComid(cid);
                                                        replies.setCategory(un);
                                                        replies.setPrblm(em);
                                                        replies.setDescp(dp);
                                                        replies.setDate(dt);
                                                        replies.setStatus("Request Rejected");
                                                        replies.setMessage("Invalid Complaint");

                                                        viewadmin.child(String.valueOf(cid)).setValue(replies);
                                                        data.child(phn).child(String.valueOf(cid)).setValue(notify);
                                                        Toast.makeText(Invaliddata.this, "Reply Sent Successfully", Toast.LENGTH_LONG).show();
                                                        usersdRef.child(String.valueOf(cid)).setValue(null);
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
                                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Invaliddata.this, android.R.layout.simple_list_item_1, feed);
                                            listView.setAdapter(arrayAdapter);
                                            arrayAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    };usersdRef.addListenerForSingleValueEvent(event);
                                }

                            }else {
                                checked = false;
                                final DatabaseReference dreff = FirebaseDatabase.getInstance().getReference().child("Invalid");
                                final Invalid invinfo = new Invalid();

                                dialog.dismiss();
                                ValueEventListener event = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            final Integer cid = ds.child("comid").getValue(int.class);
                                            final String un = ds.child("category").getValue(String.class);
                                            final String dt = ds.child("date").getValue(String.class);
                                            final String em = ds.child("prblm").getValue(String.class);
                                            final String dp = ds.child("descrip").getValue(String.class);
                                            final String phn = ds.child("mno").getValue(String.class);

                                            final String set = "Request accepted";

                                            //final Button replybtn = feed.get(position);
                                            final AlertDialog.Builder builder = new AlertDialog.Builder(Invaliddata.this);
                                            builder.setTitle("Send Reply");
                                            builder.setMessage("Complaint ID :" + cid + "\n" + "Category :" + un + "\n" + "Problem :" + em + "\n"+"Status : "+set+"\n");

                                            // Set up the input
                                            final EditText input = new EditText(Invaliddata.this);
                                            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                                            input.setText("");
                                            builder.setView(input);

                                            // Set up the buttons
                                            builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int listposition) {
                                                    String compid = listPosition;
                                                    m_Text = input.getText().toString().trim();

                                                    notify.setComid(cid);
                                                    notify.setCategory(un);
                                                    notify.setPrblm(em);
                                                    notify.setStatus(set);
                                                    notify.setDescp(dp);
                                                    notify.setMessage(m_Text);

                                                    replies.setComid(cid);
                                                    replies.setCategory(un);
                                                    replies.setPrblm(em);
                                                    replies.setDescp(dp);
                                                    replies.setDate(dt);
                                                    replies.setStatus(set);
                                                    replies.setMessage(m_Text);

                                                    viewadmin.child(String.valueOf(cid)).setValue(replies);
                                                    data.child(phn).child(String.valueOf(cid)).setValue(notify);
                                                    Toast.makeText(Invaliddata.this, "Reply Sent Successfully", Toast.LENGTH_LONG).show();
                                                    usersdRef.child(String.valueOf(cid)).setValue(null);

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
                                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Invaliddata.this, android.R.layout.simple_list_item_1, feed);
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