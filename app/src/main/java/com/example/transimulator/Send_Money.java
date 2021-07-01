package com.example.transimulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.transimulator.databinding.ActivitySendMoneyBinding;
import com.example.transimulator.models.users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Send_Money extends AppCompatActivity {

    ActivitySendMoneyBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    List<users> usersList = new ArrayList<>();
    users enteredOb;
    int currentUserBal,sent,received;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySendMoneyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        progressDialog= new ProgressDialog(Send_Money.this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Transferring");
        progressDialog.setCanceledOnTouchOutside(false);
        reference= FirebaseDatabase.getInstance().getReference().child("Users");



        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Validating No any field should be empty
                if (binding.etName.getText().toString().equals("")){
                    binding.etName.setError("Enter Beneficiary Name");
                    return;
                }
                if (binding.etACNum.getText().toString().equals("")){
                    binding.etACNum.setError("Enter Beneficiary Account Number");
                    return;
                }
                if (binding.etIFSC.getText().toString().equals("")){
                    binding.etIFSC.setError("Enter IFSC of Beneficiary's Bank");
                    return;
                }
                if (binding.amount.getText().toString().equals("")){
                    binding.amount.setError("Enter Amount to be Sent");
                    return;
                }

                
                progressDialog.show();
                getCurrentUserBal();
                 sendMoney();



                return;
            }

        });


    }

    private void getCurrentUserBal() {

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                currentUserBal = Integer.parseInt(snapshot.child(user.getUid()).child("balance").getValue().toString());
                sent= Integer.parseInt(snapshot.child(user.getUid()).child("sent").getValue().toString());
                received= Integer.parseInt(snapshot.child(user.getUid()).child("received").getValue().toString());


            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void sendMoney() {


        getReceiverDetails();



        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                int flag=0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                   users receiverOb = dataSnapshot.getValue(users.class);

                    if (enteredOb.getAcNumber() == receiverOb.getAcNumber() &&
                            enteredOb.getName().equalsIgnoreCase(receiverOb.getName()) &&
                            enteredOb.getFiscCode().equals(receiverOb.getFiscCode())){

                        flag++;

                       if(enteredOb.getBalance() <= currentUserBal){
                           Map<String,Object> map = new HashMap<>();


                           int enteredBal = enteredOb.getBalance();
                           int currentUserNewBal = currentUserBal - enteredBal;
                           int receiverBal = receiverOb.getBalance();
                           int receiverNewBal = receiverBal + enteredBal;
                           final String receiverId = dataSnapshot.getKey();

                           int newSent= sent + 1;
                           int newReceived = receiverOb.getReceived() + 1;

                           map.put("/"+user.getUid()+"/balance/",currentUserNewBal);
                           map.put("/"+user.getUid()+"/sent/",newSent);
                           map.put("/"+receiverId+"/balance/",receiverNewBal);
                           map.put("/"+receiverId+"/received/",newReceived);
                           reference.updateChildren(map);


                           break;
                       }
                       else {
                           binding.amount.setError("UnSufficient Fund");
                           progressDialog.dismiss();
                           return;
                       }
                    }


                }
                if (flag == 0){
                    Toast.makeText(Send_Money.this, "Wrong Beneficiary Details, Plz Re-Enter", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
                else {
                    Toast.makeText(Send_Money.this, "Transaction Successful", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    Intent intent= new Intent(Send_Money.this,MainActivity.class);
                    startActivity(intent);
                }



            }

            @Override
            public void onCancelled(DatabaseError error) {

            }

        });


    }





    private void getReceiverDetails() {
        enteredOb = new users(binding.etName.getText().toString(),Long.parseLong(binding.etACNum.getText().toString()),
                binding.etIFSC.getText().toString(),Integer.parseInt(binding.amount.getText().toString()));


    }
}