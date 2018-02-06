package com.android.volvocars.drawsomestuff;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_layout);
        // setContentView(new CustomView(this));
        SinGauge gaugeView = findViewById(R.id.sinGview);
        TextView textView = findViewById(R.id.textSview);
        textView.setValue(120);
        gaugeView.setValue(9000);
    }


}
