package com.example.qrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private Button generateqr,sheets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        generateqr=findViewById(R.id.idbuttonGenerate);
        sheets=findViewById(R.id.sheets);

        sheets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/spreadsheets/d/1FQruuvUfoGzY8q31p8b_Jh7ZT0Dkx5HVPCLzcWTP3pk/edit#gid=0"));
                startActivity(intent);

            }
        });

        generateqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(HomeActivity.this,GenerateQR.class);
                startActivity(i);

            }
        });


    }
}