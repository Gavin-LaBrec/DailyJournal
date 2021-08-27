package com.example.dailyjournal;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.dailyjournal.databinding.ActivityReviewEntryBinding;

import java.time.LocalDateTime;

public class ReviewEntry extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityReviewEntryBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityReviewEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureText();
        configureButtons();
    }

    /**
     * Initializes text on starting activity
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configureText() {
        // Set up TextViews
        TextView improveTextView = findViewById(R.id.improveTextMultiLine);
        TextView gratitudeTextView = findViewById(R.id.gratitudeTextMultiLine);

        // ONLY GETS CURRENT DATE!!!
        // MUST BE CHANGED TO LAST DATE OF ENTRIES!!!
        LocalDateTime currentDate = LocalDateTime.now();
        int month = currentDate.getMonthValue();
        int day = currentDate.getDayOfMonth();
        int year = currentDate.getYear();
        String date = month + "/" + day + "/" + year;

        DatabaseHelper databaseHelper = new DatabaseHelper(ReviewEntry.this);
        Entry lastEntry = databaseHelper.getEntry(date);
        String lastImprove = lastEntry.getImproveText();
        String lastGratitude = lastEntry.getGratitudeText();

        improveTextView.setText(lastImprove);
        gratitudeTextView.setText(lastGratitude);
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
    }
}