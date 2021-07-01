package com.example.transimulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.transimulator.databinding.ActivityMainBinding;
import com.example.transimulator.models.users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser user;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Fetching Data From Server");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());



        showAllData();

        binding.btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Send_Money.class);
                startActivity(intent);
            }
        });



    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private void showAllData() {

        progressDialog.show();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                users userob = snapshot.getValue(users.class);

                //String fullName = snapshot.child("name").getValue().toString();
                binding.name.setText(userob.getName());
                binding.balance.setText(Integer.toString(userob.getBalance()));
                binding.etName.setText(userob.getName());
                binding.etEmail.setText(userob.geteMail());
                binding.etACNum.setText(Long.toString(userob.getAcNumber()));
                binding.etIFSC.setText(userob.getFiscCode());
                binding.noOfTrans.setText(Integer.toString(userob.getSent() + userob.getReceived()));

                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.help:
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
                break;

            case R.id.logout:
                auth.signOut();
                Intent intent = new Intent(MainActivity.this,LogInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//  prevent to go to back activity
                startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
}