package com.iiitd.onCampusUdhaar.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iiitd.onCampusUdhaar.R;

public class MainActivity extends AppCompatActivity {

    LinearLayout l1;
    LinearLayout l2;
    TextView textView;
    Button button;
    Animation animation1,animation2;
    private  static  int spalash_time=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=(TextView) findViewById(R.id.text_view);

        l1=(LinearLayout) findViewById(R.id.ll1);
        animation2= AnimationUtils.loadAnimation(this,R.anim.downtoup);
        textView.setAnimation(animation2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                Intent intent=new Intent(MainActivity.this,Category.class);
                startActivity(intent);
//                intent=new Intent(MainActivity.this,LoginUser.class);
//                startActivity(intent);
            }
        },spalash_time);


    }
}
