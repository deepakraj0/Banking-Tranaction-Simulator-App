package com.example.transimulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.transimulator.databinding.ActivitySignUpBinding;
import com.example.transimulator.models.users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();// to hide the action bar

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(SignUpActivity.this); // Loading dialog
        progressDialog.setMessage("Creating New Account");

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                // Validating No any field should be empty
                if (binding.etName.getText().toString().equals("")){
                    binding.etName.setError("Please Enter Your Name");
                    return;
                }
                if (binding.etEmail.getText().toString().equals("")){
                    binding.etEmail.setError("Please Enter Your Email");
                    return;
                }
                if (binding.etACNum.getText().toString().equals("")){
                    binding.etACNum.setError("Please Enter Your Account Number");
                    return;
                }
                if (binding.etIFSC.getText().toString().equals("")){
                    binding.etIFSC.setError("Please Enter IFSC of Your Bank");
                    return;
                }
                if (binding.etBal.getText().toString().equals("")){
                    binding.etBal.setError("Please Enter Your Current Balance");
                    return;
                }
                if (binding.etPass.getText().toString().equals("")){
                    binding.etPass.setError("Please Enter a Secure Password");
                    return;
                }
                if (binding.etConPass.getText().toString().equals("")){
                    binding.etConPass.setError("Please Confirm Your Password");
                    return;
                }

                users user = new users(binding.etName.getText().toString(),binding.etEmail.getText().toString(),
                        binding.etIFSC.getText().toString(),binding.etPass.getText().toString(),
                        binding.etConPass.getText().toString(),Long.parseLong(binding.etACNum.getText().toString()),
                        Integer.parseInt(binding.etBal.getText().toString()),0,0);
                // Validating confirm password
                if(!user.getPass().equals(user.getConPass())){
                    Snackbar snackbar = Snackbar.make(view,"Password & ConfirmPassword Should be equal",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    return;
                }

                progressDialog.show();// show loading dialog after clicking signup btn
                auth.createUserWithEmailAndPassword(binding.etEmail.getText().toString(),binding.etPass.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();


                            String id = task.getResult().getUser().getUid();

                            database.getReference().child("Users").child(id).setValue(user);

                            Toast.makeText(SignUpActivity.this, "Signed up Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this,LogInActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//  prevent to go to back activity
                            startActivity(intent);
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        binding.twAlreadyAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,LogInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//  prevent to go to back activity
                startActivity(intent);
            }
        });

    }
}