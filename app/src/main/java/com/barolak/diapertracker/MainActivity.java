package com.barolak.diapertracker;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    protected static final String WHICH_BABY = "whichBaby";


    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    TextView textViewTop;
    TextView textViewTopPoop;
    TextView textViewMiddle;
    TextView textViewMiddlePoop;
    TextView textViewBottom;
    TextView textViewBottomPoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textViewTop = findViewById(R.id.tv_top);
        textViewTopPoop = findViewById(R.id.tv_top_poop);
        textViewMiddle = findViewById(R.id.tv_middle);
        textViewMiddlePoop = findViewById(R.id.tv_middle_poop);
        textViewBottom = findViewById(R.id.tv_bottom);
        textViewBottomPoop = findViewById(R.id.tv_bottom_poop);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef =  firebaseDatabase.getReference(getResources().getString(R.string.firebase_ref_path));


        ArrayList<String> babyNames = new ArrayList<>();
        babyNames.add(getResources().getString(R.string.topBaby));
        babyNames.add(getResources().getString(R.string.middleBaby));
        babyNames.add(getResources().getString(R.string.bottomBaby));
        for (final String name : babyNames) {
            myRef.orderByChild("babyName")
                    .equalTo(name)
                    .limitToLast(1)
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            DiaperChange diaperChange = dataSnapshot.getValue(DiaperChange.class);
                            if (diaperChange != null) {
                                diaperChange.updateDate();
                                updateTextView(diaperChange);
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            DiaperChange diaperChange = dataSnapshot.getValue(DiaperChange.class);
                            if (diaperChange != null) {
                                diaperChange.updateDate();
                                updateTextView(diaperChange);
                            }
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

    }


    public void openDetails(View view) {
        Log.d(LOG_TAG, "openDetails called");
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

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                if (data == null){
                    Log.d(LOG_TAG, "intent is null??");
                } else {
                    DiaperChange result = data.getParcelableExtra(DetailedActivity.DIAPER_INFO);
                    myRef.push().setValue(result);

                    Map<String, Object> changeUpdate = thingsToUpdate(result);
                    myRef.child(result.getBabyName()).updateChildren(changeUpdate);

                }
            }
        }
    }

    void updateTextView(DiaperChange diaperChange){

        if (diaperChange.getBabyName().equals(getResources().getString(R.string.topBaby))){
            textViewTop.setText(diaperChange.toString());
            if (!(diaperChange.getLastPoop().equals(getResources().getString(R.string.unknown)))){
                textViewTopPoop.setText(getResources().getString(R.string.lastPoop) +
                        diaperChange.getLastPoop());

            }

        } else if (diaperChange.getBabyName().equals(getResources().getString(R.string.middleBaby))){
            textViewMiddle.setText(diaperChange.toString());
            if (!(diaperChange.getLastPoop().equals(getResources().getString(R.string.unknown)))){
                textViewMiddlePoop.setText(getResources().getString(R.string.lastPoop) +
                        diaperChange.getLastPoop());
            }
        } else if (diaperChange.getBabyName().equals(getResources().getString(R.string.bottomBaby))){
            textViewBottom.setText(diaperChange.toString());
            if (!(diaperChange.getLastPoop().equals(getResources().getString(R.string.unknown)))){
                textViewBottomPoop.setText(getResources().getString(R.string.lastPoop) +
                        diaperChange.getLastPoop());
            }
        } else {
            Log.d(LOG_TAG, "unidentified baby");
        }
    }

    public HashMap<String, Object> thingsToUpdate(DiaperChange result){
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("changeTime", result.getChangeTime());
        updates.put("comments", result.getComments());
        updates.put("diaperType", result.getDiaperType());
        updates.put("poopPresent", result.getPoopPresent());
        updates.put("timeString", result.getTimeString());
        if (result.getPoopPresent()){
            updates.put("lastPoop", result.getTimeString());
        }
        return updates;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_history){
            Intent i = new Intent(this, HistoryActivity.class);
            startActivity(i);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
