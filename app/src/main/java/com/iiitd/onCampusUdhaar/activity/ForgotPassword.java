package com.iiitd.onCampusUdhaar.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.iiitd.onCampusUdhaar.R;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText forgot;
    private Button btn_send_pass;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private View parentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        parentLayout = findViewById(android.R.id.content);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

        forgot=findViewById(R.id.forgot_input_email);
        btn_send_pass=findViewById(R.id.btn_send_pass);

        btn_send_pass.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {

        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!(conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED))
            Snackbar.make(parentLayout, R.string.switch_on, Snackbar.LENGTH_SHORT).show();
        else if (view == btn_send_pass) {
            resetPassword();
        }
    }

    public void resetPassword()
    {
        String email=forgot.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Sending reset link..");
        progressDialog.show();


        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())
                //Sending new password to the email
                {
                    Toast.makeText(getApplicationContext(),"Password reset link sent to your email id!",Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginUser.class));
                }
                else
                {
                    Toast.makeText(ForgotPassword.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
