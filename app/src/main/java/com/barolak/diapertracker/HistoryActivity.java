package com.barolak.diapertracker;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private static final String LOG_TAG = HistoryActivity.class.getSimpleName();

    ArrayAdapter<DiaperChange> adapterTop;
    ArrayAdapter<DiaperChange> adapterMiddle;
    ArrayAdapter<DiaperChange> adapterBottom;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    ListView listViewTop;
    ListView listViewMiddle;
    ListView listViewBottom;
    ArrayList<DiaperChange> topList = new ArrayList<>();
    ArrayList<DiaperChange> middleList = new ArrayList<>();
    ArrayList<DiaperChange> bottomList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //get reference from firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef =  firebaseDatabase.getReference(getResources().getString(R.string.firebase_ref_path));

        listViewTop = findViewById(R.id.lv_top);
        listViewMiddle = findViewById(R.id.lv_middle);
        listViewBottom = findViewById(R.id.lv_bottom);

        adapterTop = new ArrayAdapter<DiaperChange>(this, R.layout.list_item, topList);
        adapterMiddle = new ArrayAdapter<DiaperChange>(this, R.layout.list_item, middleList);
        adapterBottom = new ArrayAdapter<DiaperChange>(this, R.layout.list_item, bottomList);

        listViewTop.setAdapter(adapterTop);
        listViewMiddle.setAdapter(adapterMiddle);
        listViewBottom.setAdapter(adapterBottom);

        ArrayList<String> babyNames = new ArrayList<>();
        babyNames.add(getResources().getString(R.string.topBaby));
        babyNames.add(getResources().getString(R.string.middleBaby));
        babyNames.add(getResources().getString(R.string.bottomBaby));

        for (final String name : babyNames) {

            myRef.orderByChild("babyName")
                    .equalTo(name)
                    .limitToLast(6)
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            DiaperChange diaperChange = dataSnapshot.getValue(DiaperChange.class);
                            if (diaperChange != null) {
                                diaperChange.updateDate();
                                Log.v(LOG_TAG, diaperChange.getTimeString().toString());
                                if (name.equals(getResources().getString(R.string.topBaby))
                                     && !topList.contains(diaperChange)){
                                    topList.add(0, diaperChange);

                                    //TODO: sort out this mess
//                                    adapterTop.add(diaperChange);
                                    Log.v(LOG_TAG, "adding " + diaperChange.getTimeString());
                                } else if (name.equals(getResources().getString(R.string.middleBaby))
                                    && !middleList.contains(diaperChange)){
                                    middleList.add(0, diaperChange);
//                                    adapterMiddle.add(diaperChange);
                                } else if (name.equals(getResources().getString(R.string.bottomBaby))
                                       && !bottomList.contains(diaperChange)){
//                                    bottomList.add(diaperChange);
                                    adapterBottom.insert(diaperChange, 0);
                                }
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
/*                            DiaperChange diaperChange = dataSnapshot.getValue(DiaperChange.class);
                            if (diaperChange != null) {
                                diaperChange.updateDate();
                                if (name.equals(getResources().getString(R.string.topBaby))){
                                    topList.add(diaperChange);
                                } else if (name.equals(getResources().getString(R.string.middleBaby))){
                                    middleList.add(diaperChange);
                                } else {
                                    bottomList.add(diaperChange);
                                }
                            }
*/                        }

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

}
