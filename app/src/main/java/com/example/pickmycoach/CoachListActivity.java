package com.example.pickmycoach;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

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
        // Add sample coaches to Firestore if they don't exist
        addSampleCoaches();

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
                    } else {
                        Toast.makeText(this, "Failed to load coaches", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addSampleCoaches() {
        // Sample coaches data
        Coach[] sampleCoaches = {
                new Coach("1", "John Smith", "Weight Training", "5 years", "4.8", "$50/hour",
                        "", "Experienced weight training coach specializing in strength building and muscle gain.",
                        "Mon-Fri: 9AM-6PM"),
                new Coach("2", "Sarah Johnson", "Cardio & HIIT", "3 years", "4.9", "$45/hour",
                        "", "High-energy cardio specialist focusing on HIIT workouts and endurance training.",
                        "Tue-Sat: 7AM-5PM"),
                new Coach("3", "Mike Wilson", "Yoga & Flexibility", "7 years", "4.7", "$40/hour",
                        "", "Certified yoga instructor with expertise in flexibility and mindfulness training.",
                        "Mon-Sun: 6AM-8PM")
        };

        for (Coach coach : sampleCoaches) {
            db.collection("coaches").document(coach.getId()).set(coach);
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_logout) {
            mAuth.signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        } else if (id == R.id.menu_my_appointments) {
            Toast.makeText(this, "My Appointments feature coming soon", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_profile) {
            Toast.makeText(this, "Profile feature coming soon", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
