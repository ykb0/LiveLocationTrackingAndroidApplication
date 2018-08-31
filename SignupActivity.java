package com.example.dell.map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignUp";
    private EditText mName;
    private EditText mContact;
    private EditText mEmail;
    private EditText mPassword;
    private Button mButton;
    private AwesomeValidation awesomeValidation;
    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressDialog = new ProgressDialog(this);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        mAuth = FirebaseAuth.getInstance();
        mName = findViewById(R.id.name);
        mContact = findViewById(R.id.contact);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mButton = findViewById(R.id.signup);

        awesomeValidation.addValidation(SignupActivity.this, R.id.name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$" , R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.contact,"^[7-9]{1}[0-9]{9}$", R.string.mobileerror);
        awesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.emailerror);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v ) {
                progressDialog.show();
                progressDialog.setMessage("Please wait a moment..");
                signUp();
            }
        });

    }

    private void signUp() {

        final String name, contact, email, password;
        name = mName.getText().toString();
        contact = mContact.getText().toString();
        email=mEmail.getText().toString();
        password=mPassword.getText().toString();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(contact) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) )
        {
            progressDialog.dismiss();
            Toast.makeText(SignupActivity.this,"Fields are empty",Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(awesomeValidation.validate() && task.isSuccessful())
                {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        progressDialog.dismiss();
                        user.sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "Verification Email sent.");
                                            FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                                            String uid=current_user.getUid();
                                            mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                                            HashMap<String,String> userMap=new HashMap<>();
                                            userMap.put("name",name);
                                            userMap.put("contact",contact);
                                            mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(SignupActivity.this,"Successfully Registration!!",Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(SignupActivity.this, Authentication.class));
                                                }

                                            });
                                        }
                                    }
                                });


                    }

                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(SignupActivity.this,"Oops! You're already Registered or Try Again",Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
