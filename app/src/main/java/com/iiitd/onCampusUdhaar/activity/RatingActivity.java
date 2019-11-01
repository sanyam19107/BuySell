package com.iiitd.onCampusUdhaar.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;

import com.iiitd.onCampusUdhaar.R;
import com.iiitd.onCampusUdhaar.other.BookingOrder;

public class RatingActivity extends AppCompatActivity {
    private BookingOrder orderDeliver;
    RatingBar ratingbar;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        addListenerOnButtonClick();
        orderDeliver =  (BookingOrder) getIntent().
                getSerializableExtra("orderDeliver");

    }

    public void addListenerOnButtonClick(){
        ratingbar=(RatingBar)findViewById(R.id.ratingBar);
        button=(Button)findViewById(R.id.button);
        //Performing action on Button Click  
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0)
            {

                String rating=String.valueOf(ratingbar.getRating());
               // Toast.makeText(getApplicationContext(), RatingActivity, Toast.LENGTH_LONG).show();
            }


        });


    }}
