package com.example.dailyjournal;

import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.dailyjournal.databinding.ActivityNewEntryBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NewEntry extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityNewEntryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_new_entry);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        configureText();
        configureButtons();
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_new_entry);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

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

        Button finishButton = (Button) findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView improveTextView = (TextView) findViewById(R.id.improveTextMultiLine);
                String improveText = improveTextView.getText().toString();
                TextView gratitudeTextView = (TextView) findViewById(R.id.GratitudeTextMultiLine);
                String gratitudeText = gratitudeTextView.getText().toString();
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
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter DateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        CharSequence timeText = (CharSequence) currentTime.format(DateFormat);
        TextView dateView = (TextView) findViewById(R.id.dateTextView);
        dateView.setText(timeText);
    }

}