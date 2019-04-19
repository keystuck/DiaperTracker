package com.barolak.diapertracker;

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

public class DetailedActivity extends AppCompatActivity {
    private static final String LOG_TAG = DetailedActivity.class.getSimpleName();

    TextView textViewBabyName;
    TextView textViewTime;
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
        textViewTime = findViewById(R.id.tvTime);
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
        textViewTime.setText(System.currentTimeMillis()+"");
    }

    void onRadioButtonClicked(View view){
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

    void onCheckBoxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();
        if (view.getId() == R.id.cb_poop_present){
            if (checked){
                poopPresent = true;
            }
        }
    }


    void saveDiaper(View view){
        String diaperInfo = "";
        diaperInfo += textViewBabyName.getText().toString() + "; ";
        diaperInfo += textViewTime.getText().toString() + "; ";
        diaperInfo += diaperType + "; ";
        diaperInfo += poopPresent + "; ";
        String comments = editTextComments.getText().toString();
        diaperInfo += comments;

        Toast.makeText(this, diaperInfo, Toast.LENGTH_LONG).show();

    }

}
