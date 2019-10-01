package com.iiitd.onCampusUdhaar.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.iiitd.onCampusUdhaar.R;
import com.iiitd.onCampusUdhaar.helper.ConfigurationFirebase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    //decalare views
    private Button buttonRegister;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignin;
    private ImageView eyeBtn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_register);

        progressDialog= new ProgressDialog(this);
        firebaseAuth= FirebaseAuth.getInstance();


//        if(firebaseAuth.getCurrentUser()!=null && firebaseAuth.getCurrentUser().isEmailVerified())
//        {
//            //user already logged in.Start profile activity
//            finish();
//            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
//
//        }

        //intiliase views
        buttonRegister=findViewById(R.id.btn_signup);
        editTextName=findViewById(R.id.signup_input_name);
        editTextEmail=findViewById(R.id.signup_input_email);
        editTextPassword=findViewById(R.id.signup_input_password);
        textViewSignin=findViewById(R.id.btn_link_login);
        eyeBtn=(ImageView) findViewById(R.id.eye);

        eyeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editTextPassword.setTransformationMethod(null);

            }
        });

        //Attaching onclick listner
        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(view==buttonRegister) {
            registerUser();

        }
        if(view==textViewSignin) {
            //open login activity
            startActivity(new Intent(this,MainActivity.class));
        }
    }

    private void registerUser()
    {
        final String emailPattern = "[a-zA-Z0-9._-]+@iiitd.ac.in";
        final String name=editTextName.getText().toString().trim();
        String email=editTextEmail.getText().toString().trim();
        String passowrd=editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(passowrd))
        {
            Toast.makeText(this,"Enter enter password", Toast.LENGTH_SHORT).show();
            return;
        }else if (!(email.matches(emailPattern)))
        {
            Toast.makeText(this,"Please enter valid IIITD email", Toast.LENGTH_SHORT).show();
            return;
        }

        //Everything fine

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        //Register user to firebase

        firebaseAuth.createUserWithEmailAndPassword(email,passowrd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //To check if user registeres successfully
                        if(task.isSuccessful())
                        {
                            String idUser = ConfigurationFirebase.getIdUser();
                            DatabaseReference advertisementRef = ConfigurationFirebase.getFirebase();
                            advertisementRef.child("users").child(idUser).push().setValue(name);

                            //finish();
                            firebaseAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this,"Please check your email for verification", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                    }
                                    else
                                    {
                                        FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                        Log.e("RegisterActivity", "Failed Verification", e);
                                    }
                                }
                            }) ;


                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Log.e("RegisterActivity", "Failed Registration", e);
                        }
                    }
                }
                );
    }
}
