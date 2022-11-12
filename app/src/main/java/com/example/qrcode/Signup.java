package com.example.qrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.qrcode.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Signup extends AppCompatActivity {

    ActivitySignupBinding binding;

    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        binding=ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        progressDialog=new ProgressDialog(this);

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=binding.fullname.getText().toString();
                String number=binding.number.getText().toString();
                String email=binding.emailaddress.getText().toString().trim();
                String password=binding.password.getText().toString();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(Signup.this,"ENTER YOUR FULL NAME",Toast.LENGTH_SHORT).show();
                    return;
                }


                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Signup.this,"ENTER AN EMAIL ADDRESS",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Signup.this,"ENTER A PASSWORD",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(number)){
                    Toast.makeText(Signup.this,"ENTER A NUMBER",Toast.LENGTH_SHORT).show();
                    return;
                }


                if(password.length()<6) {
                    Toast.makeText( Signup.this, "PASSWORD IS TOO SHORT MUST BE 6 CHARACTERS LENGTH", Toast.LENGTH_SHORT).show();
                }



                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(email,password)

                        .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {



                                if (task.isSuccessful()) {

                                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) ;
                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                            Toast.makeText(Signup.this, "REGISTRATION SUCCESSFUL PLEASE VERIFY YOUR EMAIL ID", Toast.LENGTH_SHORT).show();
                                        }

                                    });
                                }

                                else {
                                    Toast.makeText(Signup.this,"EMAIL ID IS ALREADY USED",Toast.LENGTH_SHORT).show();
                                }


                            }
                        })



                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                startActivity(new Intent(Signup.this,LoginActivity.class));
                                progressDialog.cancel();

                                firestore.collection("User")
                                        .document(FirebaseAuth.getInstance().getUid())
                                        .set(new UserModel(name,number,email));

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                        });


            }
        });
    }
}