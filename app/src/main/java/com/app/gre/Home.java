package com.app.gre;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {
    FirebaseAuth mauth;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageView reg = findViewById(R.id.reg);
        TextView source = findViewById(R.id.source);
        ImageView bt = findViewById(R.id.bt);
        TextView dest = findViewById(R.id.dest);
        Button logout = findViewById(R.id.logout);

        mauth = FirebaseAuth.getInstance();
        reff = FirebaseDatabase.getInstance().getReference();
        loaduserinfo();

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Complaints.class);
                startActivity(intent);
            }
        });
        source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Complaints.class);
                startActivity(intent);
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Check_status.class);
                startActivity(intent);
            }
        });
        dest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Check_status.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Home.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                Toast.makeText(Home.this, "Successfully Logged Out", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mauth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, Login.class));
        }
    }

    private void loaduserinfo() {
        FirebaseUser user = mauth.getCurrentUser();
        final String umail = user.getEmail();

        reff.child("Registered_Members").orderByChild("mail").equalTo(user.getEmail()).limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String uemail = ds.child("mail").getValue(String.class);
                    String uname = ds.child("userid").getValue(String.class);
                    String phnum = ds.child("phn").getValue(String.class);

                    TextView vuname = findViewById(R.id.usernamee);
                    vuname.setText("Welcome " + uname);

                    SharedPreferences share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = share.edit();
                    editor.putString("mail",uemail);
                    editor.putString("cuser",uname);
                    editor.putString("phnumber",phnum);
                    editor.apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.taskbar, menu);
        menu.getItem(0).getSubMenu().getItem(0).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent intent = new Intent(Home.this, Profiledata.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}