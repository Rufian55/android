package com.blitspost.quotetrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

public class QuoteTrack extends AppCompatActivity {
    // The initial app landing view.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotetrack);
        String e_mail = getIntent().getStringExtra("E_MAIL");
        TextView txt_Email = (TextView) findViewById(R.id.txt_Email);
        txt_Email.setText(e_mail);
    }

}
