package com.barolak.diapertracker;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    String diaperType = "unknown";
    boolean poopPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_data);

        textViewBabyName = findViewById(R.id.tvName);
        radioGroupDiaperType = findViewById(R.id.rg_diaper_type);
        editTextComments = findViewById(R.id.etComments);
        checkBoxPoop = findViewById(R.id.cb_poop_present);



        String babyName = getIntent().getStringExtra(MainActivity.WHICH_BABY);
        if (babyName == null){
            Log.d(LOG_TAG, "babyName is nulL");
        }
        if (textViewBabyName == null){
            Log.d(LOG_TAG, "textViewBabyName is null");
        }
        textViewBabyName.setText(babyName);
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
        }
    }


    public void saveDiaper(View view){

        DiaperChange diaperInfo = new DiaperChange(textViewBabyName.getText().toString(),
                System.currentTimeMillis(),
                diaperType,
                poopPresent,
                editTextComments.getText().toString());


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(DIAPER_INFO, diaperInfo);
        setResult(Activity.RESULT_OK, intent);

        finish();


    }

}
