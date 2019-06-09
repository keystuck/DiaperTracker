package com.barolak.diapertracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailedActivity extends AppCompatActivity {
    private static final String LOG_TAG = DetailedActivity.class.getSimpleName();

    protected static final String DIAPER_INFO = "diaperInfo";



    TextView textViewBabyName;
    RadioGroup radioGroupDiaperType;
    EditText editTextComments;
    CheckBox checkBoxPoop;
    CheckBox checkBoxBath;
    String diaperType = "unknown";
    boolean poopPresent = false;
    boolean bath = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_data);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textViewBabyName = findViewById(R.id.tvName);
        radioGroupDiaperType = findViewById(R.id.rg_diaper_type);
        editTextComments = findViewById(R.id.etComments);
        checkBoxPoop = findViewById(R.id.cb_poop_present);
        checkBoxBath = findViewById(R.id.cb_bath);




        String babyName = getIntent().getStringExtra(MainActivity.WHICH_BABY);
        if (babyName == null){
            Log.d(LOG_TAG, "babyName is nulL");
        }
        if (textViewBabyName == null){
            Log.d(LOG_TAG, "textViewBabyName is null");
        }
        textViewBabyName.setText(babyName);
        if (babyName.equals(getResources().getString(R.string.topBaby))){
            textViewBabyName.setBackgroundColor(getResources().getColor(R.color.colorTopBaby));
        } else if (babyName.equals(getResources().getString(R.string.middleBaby))){
            textViewBabyName.setBackgroundColor(getResources().getColor(R.color.colorMiddleBaby));
        } else if (babyName.equals(getResources().getString(R.string.bottomBaby))){
            textViewBabyName.setBackgroundColor(getResources().getColor(R.color.colorBottomBaby));
        }

    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        if (view.getId() == R.id.rb_cloth){
            if (checked) {
                diaperType = getResources().getString(R.string.clothType);
            }
        }
        else if (view.getId() == R.id.rb_disposable){
            if (checked){
                diaperType = getResources().getString(R.string.disposableType);
            }
        }
    }

    public void onCheckBoxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();
        if (view.getId() == R.id.cb_poop_present){
            if (checked){
                poopPresent = true;
            }
        } else if (view.getId() == R.id.cb_bath){
            if (checked){
                bath = true;
            }
        }
    }


    public void saveDiaper(View view){

        DiaperChange diaperInfo = new DiaperChange(textViewBabyName.getText().toString(),
                System.currentTimeMillis(),
                diaperType,
                poopPresent,
                bath,
                editTextComments.getText().toString());

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(DIAPER_INFO, diaperInfo);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    public void cancel(View view){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }



}
