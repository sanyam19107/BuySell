package com.iiitd.onCampusUdhaar.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.iiitd.onCampusUdhaar.R;

public class UnderConstruction extends AppCompatActivity {

    //private View parentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_under_construction);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button
    }

    public boolean onSupportNavigateUp(){ // for back button
        finish();
        return true;
    }
}
