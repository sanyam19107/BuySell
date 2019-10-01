package com.iiitd.onCampusUdhaar.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import com.iiitd.onCampusUdhaar.R;
import com.iiitd.onCampusUdhaar.helper.ConfigurationFirebase;

public class MainActivity extends AppCompatActivity {

    private Button buttonAccess;
    private EditText fieldEmail, fieldPassword;
    private Switch typeAccess;
    private ImageView eyeBtn;

    private FirebaseAuth authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();
        authentication = ConfigurationFirebase.getFirebaseAuthentication();
        eyeBtn=(ImageView) findViewById(R.id.eye);

        eyeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fieldPassword.setTransformationMethod(null);

            }
        });
        buttonAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = fieldEmail.getText().toString();
                String password = fieldPassword.getText().toString();

                //validation
                if(!email.isEmpty()){

                    if(!password.isEmpty()){

                        //verificate state of switch -
//                        if(typeAccess.isChecked()) //Register
//                        {
////                            authentication.createUserWithEmailAndPassword(
////                                    email, password
////                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
////                                @Override
////                                public void onComplete(@NonNull Task<AuthResult> task) {
////                                    if(task.isSuccessful()){
////
////                                        Toast.makeText(MainActivity.this, "Successful registration",
////                                                Toast.LENGTH_SHORT).show();
////
////                                        //redirect user for principe screen app
////
////                                    }else{
////
////                                         String errorException = "";
////
////                                        try{
////                                            throw task.getException();
////                                        }catch (FirebaseAuthWeakPasswordException e){
////                                            errorException = "Enter a stronger password!";
////                                        }catch (FirebaseAuthInvalidCredentialsException e){
////                                            errorException = "Please, type a valid email";
////                                        }catch (FirebaseAuthUserCollisionException e){
////                                            errorException = "This account has already been registered.";
////                                        } catch (Exception e) {
////                                            errorException = "When registering user: "  + e.getMessage();
////                                            e.printStackTrace();
////                                        }
////
////                                        Toast.makeText(MainActivity.this,
////                                                "Erro: " + errorException ,
////                                                Toast.LENGTH_SHORT).show();
////                                    }
////                                }
////                            });
//                   startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
//                        }else{ //Login

                            authentication.signInWithEmailAndPassword(
                                    email,password
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){

                                        Toast.makeText(MainActivity.this,
                                                "Successfully logged in",
                                                Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(getApplicationContext(), AdvertisementActivity.class));

                                    }else{

                                        Toast.makeText(MainActivity.this,
                                                "Error signing in" + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                       // }

                    }else{

                        Toast.makeText(MainActivity.this, "Fill in the password!",
                                Toast.LENGTH_SHORT).show();
                    }

                }else{

                    Toast.makeText(MainActivity.this, "Fill in the Email",
                            Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }
    public void movetoForgetPassword(View view){
        startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
    }
}
