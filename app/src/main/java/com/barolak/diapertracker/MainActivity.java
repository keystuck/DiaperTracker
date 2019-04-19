package com.barolak.diapertracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    protected static final String WHICH_BABY = "whichBaby";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    void openDetails(View view) {
        int babyID = view.getId();
        String babyName;
        if (babyID == R.id.buttonTop) {
            babyName = getResources().getString(R.string.topBaby);
        } else if (babyID == R.id.buttonMiddle) {
            babyName = getResources().getString(R.string.middleBaby);
        } else if (babyID == R.id.buttonBottom) {
            babyName = getResources().getString(R.string.bottomBaby);
        } else {
            throw new IllegalArgumentException("bad baby ID");
        }

        Intent intent = new Intent(this, DetailedActivity.class);
        intent.putExtra(WHICH_BABY, babyName);
        startActivity(intent);
    }
}
