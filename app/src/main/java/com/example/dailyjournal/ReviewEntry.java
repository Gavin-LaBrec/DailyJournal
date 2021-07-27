package com.example.dailyjournal;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.dailyjournal.databinding.ActivityNewEntryBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReviewEntry extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityNewEntryBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureDate();
        configureButtons();
    }


    /**
     * Initializes buttons on starting activity
     */
    private void configureButtons() {
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button editButton = (Button) findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                // bring up basically new entry but with stuff already filled out

            }
        });
    }

    /**
     * Initializes text on starting activity
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    // PROBABLY NOT USEFUL FUNCTION
    private void configureInitialDate() {
        // Display current date
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter DateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        CharSequence timeText = (CharSequence) currentTime.format(DateFormat);
        TextView dateView = (TextView) findViewById(R.id.dateTextView);
        dateView.setText(timeText);
    }

    /**
     * Changes dateButton to date of entry
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configureDate() {
        // Currently displays current time
        // Will change to most recent entry
        // Eventually add parameter LocalDateTime date
        LocalDateTime currentTime = LocalDateTime.now();


        DateTimeFormatter DateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        CharSequence timeText = (CharSequence) currentTime.format(DateFormat);
        Button dateView = (Button) findViewById(R.id.dateButton);
        dateView.setText(timeText);
    }

}