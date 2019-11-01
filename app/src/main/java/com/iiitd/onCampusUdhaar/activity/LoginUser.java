package com.iiitd.onCampusUdhaar.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.iiitd.onCampusUdhaar.R;
import com.iiitd.onCampusUdhaar.other.ConfigurationFirebase;
import com.iiitd.onCampusUdhaar.other.UserDetails;


public class LoginUser extends AppCompatActivity {

    private Button buttonAccess;
    private EditText fieldEmail, fieldPassword;
    private FirebaseAuth authentication;
    private View parentLayout;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1;
    private String TAG = "Check Google Sign In";
    private SignInButton gsignin;
    final String emailPattern = "[a-zA-Z0-9._-]+@iiitd.ac.in";

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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // authentication = ConfigurationFirebase.getFirebaseAuthentication();

        gsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

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
                                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                            Log.e("LoginActivity", "Failed Login", e);
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

        gsignin = (SignInButton) findViewById(R.id.sign_in_button);
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


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("UserDetails", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        authentication.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = authentication.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), Category.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            registerUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginUser.this, "not valid", Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });
    }

    private void registerUser() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            final String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
//            String personId = acct.getId();
//            Uri personPhoto = acct.getPhotoUrl();
            if (!personEmail.matches(emailPattern)) {
                logoutInvalid();
                Toast.makeText(LoginUser.this,
                        R.string.invalidEmail,
                        Toast.LENGTH_LONG).show();
            }
            else {
                Log.d("UserDetails", "registerUser: " + personName);

                DatabaseReference userRef = ConfigurationFirebase.getFirebase()
                        .child("users").child(ConfigurationFirebase.getIdUser());

                Log.d("UserDetails", "Successfully logged in as " + ConfigurationFirebase.getIdUser());

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.hasChild("name")) {
                            // run some code
                            Log.d("UserDetails", "No entry in users");
                            UserDetails userDetails = new UserDetails();
                            userDetails.setName(personName);
                            userDetails.setNoOfRating("0");
                            userDetails.setRating("0");
                            userDetails.save();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Toast.makeText(LoginUser.this,
                        "Successfully logged in",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void logoutInvalid() {

        authentication = ConfigurationFirebase.getFirebaseAuthentication();
        authentication.signOut();
        invalidateOptionsMenu();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
}