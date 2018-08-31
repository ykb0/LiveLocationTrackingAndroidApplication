package com.example.dell.map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class Authentication extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Button mLogin;
    private Button mSignUp;
    private AwesomeValidation awesomeValidation;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        mEmail =  findViewById(R.id.email);
        mPassword =  findViewById(R.id.password);
        mLogin = findViewById(R.id.login);
        mSignUp =  findViewById(R.id.signup);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.emailerror);

        mLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                progressDialog.show();
                progressDialog.setMessage("Please wait a moment..");
                login();
            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Authentication.this, SignupActivity.class));
            }
        });

    }

    private void login() {
        final String email,password;
        email = mEmail.getText().toString();
        password = mPassword.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            progressDialog.dismiss();
            Toast.makeText(Authentication.this, "Fields are empty", Toast.LENGTH_LONG).show();
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (awesomeValidation.validate() && task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(Authentication.this, "Welcome", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Authentication.this, com.example.dell.map.Button.class));

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Authentication.this, "Login Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
