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
        // Set up TextViews
        TextView dateTextView = findViewById(R.id.dateTextView);
        TextView improveTextView = findViewById(R.id.improveTextMultiLine);
        TextView gratitudeTextView = findViewById(R.id.gratitudeTextMultiLine);

        DatabaseHelper databaseHelper = new DatabaseHelper(ReviewEntry.this);
        String date = databaseHelper.getRecentDate();
        dateTextView.setText(date);

        try {
            Entry lastEntry = databaseHelper.getEntry(date);
            String lastImprove = lastEntry.getImproveText();
            String lastGratitude = lastEntry.getGratitudeText();

            improveTextView.setText(lastImprove);
            gratitudeTextView.setText(lastGratitude);
        } catch (Exception emptyDB) {
            throw new Exception();
        }
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