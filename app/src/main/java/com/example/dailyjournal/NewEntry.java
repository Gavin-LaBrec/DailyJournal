package com.example.dailyjournal;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.dailyjournal.databinding.ActivityNewEntryBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NewEntry extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityNewEntryBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureText();
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

        Button finishButton = (Button) findViewById(R.id.editButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                // Collect data for entry
                String date = DatabaseHelper.formatDate(LocalDateTime.now());

                TextView improveTextView = (TextView) findViewById(R.id.improveTextMultiLine);
                String improveText = improveTextView.getText().toString();
                TextView gratitudeTextView = (TextView) findViewById(R.id.gratitudeTextMultiLine);
                String gratitudeText = gratitudeTextView.getText().toString();

                // Create and add entry to database
                Entry newEntry = new Entry(date, improveText, gratitudeText);
                DatabaseHelper databaseHelper = new DatabaseHelper(NewEntry.this);
                databaseHelper.addDatabaseEntry(newEntry);
                finish();
            }
        });
    }

    /**
     * Initializes text on starting activity
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configureText() {
        // Display current date
        String currentDate = DatabaseHelper.formatDate(LocalDateTime.now());
        TextView dateView = (TextView) findViewById(R.id.dateTextView);
        dateView.setText(currentDate);
    }


}