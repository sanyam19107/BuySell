package com.iiitd.onCampusUdhaar.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.iiitd.onCampusUdhaar.R;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText forgot;
    private Button btn_send_pass;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

        forgot=findViewById(R.id.forgot_input_email);
        btn_send_pass=findViewById(R.id.btn_send_pass);

        btn_send_pass.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {

        if (view == btn_send_pass) {
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

        progressDialog.setMessage("Sending reset link...");
        progressDialog.show();


        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())
                {
                    Toast.makeText(ForgotPasswordActivity.this,"Password send to your email",Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else
                {
                    Toast.makeText(ForgotPasswordActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
