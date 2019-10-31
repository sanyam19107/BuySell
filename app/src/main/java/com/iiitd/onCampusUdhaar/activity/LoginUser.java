package com.iiitd.onCampusUdhaar.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.iiitd.onCampusUdhaar.R;

public class LoginUser extends AppCompatActivity {

    private Button buttonAccess;
    private EditText fieldEmail, fieldPassword;
    private FirebaseAuth authentication;
    private View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        parentLayout = findViewById(android.R.id.content);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        authentication=FirebaseAuth.getInstance();
        initializeComponents();
        // authentication = ConfigurationFirebase.getFirebaseAuthentication();

        final ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        buttonAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    String email = fieldEmail.getText().toString();
                    String password = fieldPassword.getText().toString();

                    //validation
                    if (!email.isEmpty()) {

                        if (!password.isEmpty()) {

                            authentication.signInWithEmailAndPassword(
                                    email, password
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        if (authentication.getCurrentUser().isEmailVerified()) {

                                            Toast.makeText(LoginUser.this,
                                                    "Successfully logged in",
                                                    Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), Category.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(LoginUser.this, "Please verify your email id", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        {
                                            Toast.makeText(LoginUser.this, "Incorrect email id or password", Toast.LENGTH_SHORT).show();
                                           // FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                            //Log.e("LoginActivity", "Failed Login", e);
                                        }
                                    }
                                }
                            });
                            // }

                        } else {

                            Toast.makeText(LoginUser.this, "Fill in the password!",
                                    Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        Toast.makeText(LoginUser.this, "Fill in the Email",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Snackbar.make(parentLayout, R.string.switch_on, Snackbar.LENGTH_SHORT).show();
                }

            }

        });

    }

    private void initializeComponents(){

        fieldEmail = findViewById(R.id.editRegisterEmail);
        fieldPassword = findViewById(R.id.editRegisterPassword);
        buttonAccess = findViewById(R.id.buttonAccess);
    }
    public void movetoRegister(View view){
        startActivity(new Intent(getApplicationContext(), RegisterUser.class));
    }
    public void movetoForgetPassword(View view){
        startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
