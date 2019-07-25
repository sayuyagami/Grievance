package com.app.gre;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static java.lang.Integer.parseInt;

public class Complaints extends AppCompatActivity {

    TextView complaintid,userno;
    EditText descp;
    SpinnerDialog categoryspinnerDialog, prblmspinnerDialog;
    Button complaintsubmit, category, problem;

    DatabaseReference reff, creff,viewadmin;

    Complaintdetails details;
    SendNotifications data;
    Viewreplies replies;

    public ArrayList<String> msg = new ArrayList<>();

    ArrayList<String> type1 = new ArrayList<>();
    ArrayList<String> type2 = new ArrayList<>();
    ArrayList<String> type3 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);

        viewadmin = FirebaseDatabase.getInstance().getReference().child("Viewreplies");
        replies = new Viewreplies();

        details = new Complaintdetails();
        complaintid = findViewById(R.id.complaintid);
        category = (Button) findViewById(R.id.category);
        problem = (Button) findViewById(R.id.problem);
        userno = findViewById(R.id.userno);
        descp = findViewById(R.id.descp);
        complaintsubmit = (Button) findViewById(R.id.complaintsubmit);
        complaintid.setText(gp());

        //current user mobile number retrieved
        SharedPreferences share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        final String phnumber = share.getString("phnumber","");

        userno.setText(phnumber);

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryspinnerDialog.showSpinerDialog();
            }
        });

        initItems1();
        categoryspinnerDialog = new SpinnerDialog(Complaints.this, type1, "Select Grievance Type");
        categoryspinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(final String type1, int position) {
                category.setText(type1);
                //Toast.makeText(Complaints.this,"Selected" +type,Toast.LENGTH_LONG).show();

                switch (type1) {
                    case "Emergency case":
                        problem.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                prblmspinnerDialog.showSpinerDialog();

                            }
                        });
                        type2.clear();
                        initItems2();
                        prblmspinnerDialog = new SpinnerDialog(Complaints.this, type2, "Select your problem");
                        prblmspinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String type2, int position) {
                                problem.setText(type2);
                            }
                        });
                        break;
                    case "Normal case":
                        // Set up the input
                        problem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(Complaints.this);
                                builder.setTitle("Name Problem Type");

                                // Set up the input
                                final EditText input = new EditText(Complaints.this);
                                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                                input.setText("");
                                builder.setView(input);

                                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        String mtext = input.getText().toString().trim();
                                        problem.setText(mtext);
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
                        });
                        break;
                }
            }
        });

        //database connection
        reff = FirebaseDatabase.getInstance().getReference().child("Complaintdetails");
        creff = FirebaseDatabase.getInstance().getReference().child("SendNotifications");
        data = new SendNotifications();

        complaintsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int cid = Integer.parseInt(complaintid.getText().toString().trim());
                String cat = category.getText().toString().trim();
                String prblm = problem.getText().toString().trim();
                String dcp = descp.getText().toString().trim();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date();
                String dt = dateFormat.format(date);

                try {
                    if (!cat.isEmpty() && !prblm.isEmpty() && !dcp.isEmpty()) {
                        details.setComplaintid(cid);
                        details.setCategory(cat);
                        details.setProblem(prblm);
                        details.setDatetym(dt);
                        details.setMno(phnumber);
                        details.setDescp(dcp);

                        data.setComid(cid);
                        data.setCategory(cat);
                        data.setPrblm(prblm);
                        data.setDescp(dcp);
                        data.setMessage("in process");
                        data.setStatus("Wait for reply");

                        replies.setComid(cid);
                        replies.setCategory(cat);
                        replies.setPrblm(prblm);
                        replies.setDescp(dcp);
                        replies.setDate(dt);
                        replies.setMno(phnumber);
                        replies.setStatus("Wait for reply");
                        replies.setMessage("in process");

                        viewadmin.child(String.valueOf(cid)).setValue(replies);
                        reff.child(String.valueOf(cid)).setValue(details);
                        creff.child(phnumber).child(String.valueOf(cid)).setValue(data);
                        Toast.makeText(Complaints.this, "Complaint Submitted", Toast.LENGTH_LONG).show();
                        complaintid.setText(gp());
                        category.setText("SELECT CATEGORY");
                        problem.setText("SELECT PROBLEM TYPE");
                        descp.setText("");
                    }
                } catch (NullPointerException e) {
                    Toast.makeText(Complaints.this, "Please fill all the feilds ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public String gp() {
        int rp = (int)(Math.random()*9000)+1000;
        final String rrp = String.valueOf(rp);
        return rrp;
    }

    //actionbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //handle actionbar handle clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {

            Intent intent = new Intent(Complaints.this,Home.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initItems1(){
        type1.add("Emergency case");
        type1.add("Normal case");
    }

    private void initItems2() {
        type2.add("Illegal Road Cutting");
        type2.add("Repairs of Nala / Drain");
        type2.add("Water logging");
        type2.add("Desilting of Nala / Drain");
        type2.add("Manhole Cover Open");
        type2.add("Drainage Line Damage");
        type2.add("No Water Supply in Public Toilets");
        type2.add("Overflowing of Nala / Drain");
        type2.add("Fencing of Nala /Drain");
        type2.add("Street Lights Glowing Continuously");
        type2.add("Loose Wire Connections");
    }
}
