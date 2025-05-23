package com.example.pickmycoach;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickmycoach.model.Coach;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class CoachListActivity extends AppCompatActivity implements CoachAdapter.OnCoachClickListener {

    private RecyclerView recyclerView;
    private CoachAdapter adapter;
    private List<Coach> coachList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_list);

        // Initialize Firebase instances
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Set up action bar
        ImageButton menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenuInflater().inflate(R.menu.main_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                // Handle menu item clicks
                return onOptionsItemSelected(item);
            });

            popup.show();
        });

        initViews();
        loadCoaches();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewCoaches);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        coachList = new ArrayList<>();
        adapter = new CoachAdapter(coachList, this);
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadCoaches() {
        // Show loading indicator
        Toast.makeText(this, "Loading coaches...", Toast.LENGTH_SHORT).show();

        db.collection("coaches")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        coachList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Coach coach = document.toObject(Coach.class);
                            coach.setId(document.getId());
                            coachList.add(coach);
                        }
                        adapter.notifyDataSetChanged();

                        if (coachList.isEmpty()) {
                            Toast.makeText(this, "No coaches available", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Failed to load coaches: " +
                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onCoachClick(Coach coach) {
        Intent intent = new Intent(this, CoachDetailActivity.class);
        intent.putExtra("coach_id", coach.getId());
        intent.putExtra("coach_name", coach.getName());
        intent.putExtra("coach_speciality", coach.getSpeciality());
        intent.putExtra("coach_experience", coach.getExperience());
        intent.putExtra("coach_rating", coach.getRating());
        intent.putExtra("coach_price", coach.getPrice());
        intent.putExtra("coach_description", coach.getDescription());
        intent.putExtra("coach_availability", coach.getAvailability());
        startActivity(intent);
    }

    // Menu implementation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_logout) {
            handleLogout();
            return true;
        } else if (id == R.id.menu_my_appointments) {
            showAppointments();
            return true;
        } else if (id == R.id.menu_profile) {
            showProfile();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleLogout() {
        mAuth.signOut();
        startActivity(new Intent(this, MainActivity.class));
        finish();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }

    private void showAppointments() {
        // Replace with actual implementation when ready
        Toast.makeText(this, "My Appointments feature coming soon", Toast.LENGTH_SHORT).show();

        // For future implementation:
        // startActivity(new Intent(this, AppointmentsActivity.class));
    }

    private void showProfile() {
        // Replace with actual implementation when ready
        startActivity(new Intent(this, ProfileActivity.class));

        // For future implementation:
        // startActivity(new Intent(this, ProfileActivity.class));
    }
}
