package com.example.transimulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.transimulator.databinding.ActivityLogInBinding;
import com.example.transimulator.models.users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {

    ActivityLogInBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        getSupportActionBar().hide();// to hide the action bar

        binding= ActivityLogInBinding.inflate(getLayoutInflater());


        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(LogInActivity.this);
        progressDialog.setMessage("Logging In");
        progressDialog.setCanceledOnTouchOutside(false);



        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.etEmail.getText().toString().equals("")){
                    binding.etEmail.setError("Please Enter Your Email First");
                    return;
                }
                if (binding.etPass.getText().toString().equals("")){
                    binding.etPass.setError("Please Enter Your Password First");
                    return;
                }
                progressDialog.show();

                auth.signInWithEmailAndPassword(binding.etEmail.getText().toString(),binding.etPass.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(LogInActivity.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//  prevent to go to back activity
                                    startActivity(intent);

                                    //fetching data from database(firebade)




                                    Toast.makeText(LogInActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    progressDialog.dismiss();
                                    Toast.makeText(LogInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
            }
        });

        //adding click listener oon "Create new account"
        binding.etCreateNewAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(LogInActivity.this,SignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//  prevent to go to back activity
                startActivity(intent);
            }
        });

        //for stay logged in after log in once
        if (auth.getCurrentUser() != null){
            Intent intent= new Intent(LogInActivity.this,MainActivity.class);

            startActivity(intent);

        }




    }
}