package com.example.dailyjournal;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
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
        try {
            configureText();
        } catch (Exception emptyDB) {
            // Popup error if there are no entries
            startActivity(new Intent(ReviewEntry.this, EmptyEntryPop.class));
            finish();
        }

        configureButtons();
    }

    /**
     * Initializes text on starting activity
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configureText() throws Exception {
        DatabaseHelper databaseHelper = new DatabaseHelper(ReviewEntry.this);
        String date = databaseHelper.getRecentDate();
        String formattedDate = databaseHelper.getDateText(date);
        updateText(date);
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

        ImageButton olderButton = (ImageButton) findViewById(R.id.olderButton);
        olderButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                TextView dateTextView = findViewById(R.id.dateTextView);

                // Get older date
                DatabaseHelper databaseHelper = new DatabaseHelper(ReviewEntry.this);
                String selectedDate = (String) dateTextView.getText();
                String olderDateString = databaseHelper.getNextDate(selectedDate, "older");
                try {
                    updateText(olderDateString);
                } catch (Exception e) {
                    // May want to handle moving from oldest entry better
                    e.printStackTrace();
                }
            }
        });

        ImageButton newerButton = (ImageButton) findViewById(R.id.newerButton);
        newerButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                TextView dateTextView = findViewById(R.id.dateTextView);

                // Get newer date
                DatabaseHelper databaseHelper = new DatabaseHelper(ReviewEntry.this);
                String selectedDate = (String) dateTextView.getText();
                String olderDateString = databaseHelper.getNextDate(selectedDate, "newer");
                try {
                    updateText(olderDateString);
                } catch (Exception e) {
                    // May want to handle moving from oldest entry better
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Updates the text displayed based on the given date
     *
     * @param dateString string to look up to find next date
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateText(String dateString) throws Exception {
        // Set up TextViews
        TextView dateTextView = findViewById(R.id.dateTextView);
        TextView improveTextView = findViewById(R.id.improveTextMultiLine);
        TextView gratitudeTextView = findViewById(R.id.gratitudeTextMultiLine);

        DatabaseHelper databaseHelper = new DatabaseHelper(ReviewEntry.this);
        Entry olderDate = databaseHelper.getEntry(dateString);

        // Update display text
        String improve = olderDate.getImproveText();
        String gratitude = olderDate.getGratitudeText();

        improveTextView.setText(improve);
        gratitudeTextView.setText(gratitude);
        dateTextView.setText(dateString);
    }
}