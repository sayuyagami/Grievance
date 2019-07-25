package com.app.gre;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Profiledata extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiledata);

        SharedPreferences share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String cmail = share.getString("mail","");
        String usern = share.getString("cuser","");
        String phnumber = share.getString("phnumber","");

        TextView email = findViewById(R.id.a);
        TextView uname = findViewById(R.id.b);
        TextView userphn = findViewById(R.id.c);

        email.setText(cmail);
        uname.setText(usern);
        userphn.setText(phnumber);
    }
}
