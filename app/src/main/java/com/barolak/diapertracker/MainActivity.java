package com.barolak.diapertracker;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    protected static final String WHICH_BABY = "whichBaby";


    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    DatabaseReference topRef;
    DatabaseReference middleRef;
    DatabaseReference bottomRef;

    TextView textViewTop;
    TextView textViewMiddle;
    TextView textViewBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTop = findViewById(R.id.tv_top);
        textViewMiddle = findViewById(R.id.tv_middle);
        textViewBottom = findViewById(R.id.tv_bottom);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef =  firebaseDatabase.getReference("changes");
/*
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DiaperChange diaperChange = dataSnapshot.getValue(DiaperChange.class);
                }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DiaperChange diaperChange = dataSnapshot.getValue(DiaperChange.class);
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
*/
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
                    myRef.child(result.getBabyName()).setValue(result);
                }
            }
        }
    }

    void updateTextView(DiaperChange diaperChange){

        if (diaperChange.getBabyName().equals(getResources().getString(R.string.topBaby))){
            textViewTop.setText(diaperChange.toString());
        } else if (diaperChange.getBabyName().equals(getResources().getString(R.string.middleBaby))){
            textViewMiddle.setText(diaperChange.toString());
        } else if (diaperChange.getBabyName().equals(getResources().getString(R.string.bottomBaby))){
            textViewBottom.setText(diaperChange.toString());
        } else {
            Log.d(LOG_TAG, "unidentified baby");
        }
    }
}
