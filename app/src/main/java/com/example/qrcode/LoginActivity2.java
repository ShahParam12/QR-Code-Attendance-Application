package com.example.qrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.qrcode.databinding.ActivityLogin2Binding;
import com.example.qrcode.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity2 extends AppCompatActivity {

    ActivityLogin2Binding binding;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        firebaseAuth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);

        binding=ActivityLogin2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=binding.emailaddress1.getText().toString().trim();
                String password=binding.password1.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity2.this,"ENTER AN EMAIL ADDRESS",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity2.this,"ENTER A PASSWORD",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.length()<6) {
                    Toast.makeText(LoginActivity2.this, "PASSWORD IS TOO SHORT MUST BE 6 CHARACTERS LENGTH", Toast.LENGTH_SHORT).show();
                }

                progressDialog.setTitle("Please Verify your Email");
                progressDialog.show();

                firebaseAuth.signInWithEmailAndPassword(email,password)

                        .addOnCompleteListener(LoginActivity2.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    if (firebaseAuth.getCurrentUser().isEmailVerified()){

                                        Toast.makeText(LoginActivity2.this,"LOGGED IN SUCCESSFULLY",Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));

                                    }


                                } else {

                                    Toast.makeText(LoginActivity2.this,"LOG IN FAILED PLEASE VERIFY YOUR EMAIL ID",Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });




            }
        });

        binding.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=binding.emailaddress1.getText().toString();
                progressDialog.setTitle("Sending Mail");
                progressDialog.show();

                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(LoginActivity2.this, "Email Sent", Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity2.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                        });
            }
        });

        binding.goToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity2.this,Signup2.class));
            }
        });
    }
}