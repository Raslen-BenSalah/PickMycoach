package com.example.pickmycoach;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CoachDetailActivity extends AppCompatActivity {
    private TextView tvName, tvSpeciality, tvExperience, tvRating, tvPrice, tvDescription, tvAvailability;
    private Button btnBookAppointment;
    private String coachId, selectedDate, selectedTime;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_detail);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        initViews();
        loadCoachData();
        setupClickListeners();
    }

    private void initViews() {
        tvName = findViewById(R.id.tvDetailName);
        tvSpeciality = findViewById(R.id.tvDetailSpeciality);
        tvExperience = findViewById(R.id.tvDetailExperience);
        tvRating = findViewById(R.id.tvDetailRating);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvDescription = findViewById(R.id.tvDetailDescription);
        tvAvailability = findViewById(R.id.tvDetailAvailability);
        btnBookAppointment = findViewById(R.id.btnBookAppointment);
    }

    private void loadCoachData() {
        coachId = getIntent().getStringExtra("coach_id");
        tvName.setText(getIntent().getStringExtra("coach_name"));
        tvSpeciality.setText(getIntent().getStringExtra("coach_speciality"));
        tvExperience.setText("Experience: " + getIntent().getStringExtra("coach_experience"));
        tvRating.setText("Rating: " + getIntent().getStringExtra("coach_rating") + "★");
        tvPrice.setText(getIntent().getStringExtra("coach_price"));
        tvDescription.setText(getIntent().getStringExtra("coach_description"));
        tvAvailability.setText("Available: " + getIntent().getStringExtra("coach_availability"));
    }

    private void setupClickListeners() {
        btnBookAppointment.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    showTimePicker();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                    bookAppointment();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void bookAppointment() {
        String userId = mAuth.getCurrentUser().getUid();

        Map<String, Object> appointment = new HashMap<>();
        appointment.put("userId", userId);
        appointment.put("coachId", coachId);
        appointment.put("coachName", getIntent().getStringExtra("coach_name"));
        appointment.put("date", selectedDate);
        appointment.put("time", selectedTime);
        appointment.put("status", "pending");
        appointment.put("createdAt", System.currentTimeMillis());
        appointment.put("price", getIntent().getStringExtra("coach_price"));

        db.collection("appointments")
                .add(appointment)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Appointment booked successfully!", Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to book appointment: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_back) {
            finish();
            return true;
        } else if (id == R.id.menu_share) {
            Toast.makeText(this, "Share feature coming soon", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
