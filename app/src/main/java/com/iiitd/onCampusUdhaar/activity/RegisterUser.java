package com.iiitd.onCampusUdhaar.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.iiitd.onCampusUdhaar.R;
import com.iiitd.onCampusUdhaar.other.ConfigurationFirebase;
import com.iiitd.onCampusUdhaar.other.Advertisement;
import com.iiitd.onCampusUdhaar.other.UserDetails;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{
    //decalare views
    private Button buttonRegister;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_register_user);
        parentLayout = findViewById(android.R.id.content);

        progressDialog= new ProgressDialog(this);
        firebaseAuth= FirebaseAuth.getInstance();

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

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
        editTextPassword=findViewById(R.id.editTextPassword);
        textViewSignin=findViewById(R.id.btn_link_login);

        //Attaching onclick listner
        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!(conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED))
            Snackbar.make(parentLayout, R.string.switch_on, Snackbar.LENGTH_SHORT).show();
        else if (view==buttonRegister) {
            registerUser();

        }
        else if(view==textViewSignin) {
            //open login activity
            startActivity(new Intent(this, LoginUser.class));
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

        progressDialog.setMessage("Please wait for a moment..");
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

                            UserDetails userDetails = new UserDetails();
                            userDetails.setName(name);
                            userDetails.setNoOfRating(0);
                            userDetails.setRating(0);
                            userDetails.save();
                            //finish();
                            firebaseAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterUser.this,"Please check your email for verification", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(new Intent(RegisterUser.this, LoginUser.class));
                                    }
                                    else
                                    {
                                        FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                        Log.e("RegisterUser", "Failed Verification", e);
                                    }
                                }
                            }) ;


                        }
                        else
                        {
                            Toast.makeText(RegisterUser.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Log.e("RegisterUser", "Failed Registration", e);
                        }
                    }
                }
                );
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
