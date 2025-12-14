package com.example.finalprojectmedicationmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.finalprojectmedicationmanager.models.Medication;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private TextView welcomeTextView, emptyListTextView;
    private Button logoutButton;
    private FloatingActionButton addMedicationButton;
    private RecyclerView medicationsRecyclerView;
    private MedicationListAdapter adapter;
    private List<Medication> medicationList;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("medications");

        if (currentUser == null) {
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
            return;
        }

        welcomeTextView = findViewById(R.id.welcomeTextView);
        emptyListTextView = findViewById(R.id.emptyListTextView);
        logoutButton = findViewById(R.id.logoutButton);
        addMedicationButton = findViewById(R.id.addMedicationButton);
        medicationsRecyclerView = findViewById(R.id.medicationsRecyclerView);

        String userEmail = currentUser.getEmail();
        String displayName = userEmail != null ? userEmail.split("@")[0] : "User";
        welcomeTextView.setText("Welcome, " + displayName);

        medicationList = new ArrayList<>();
        adapter = new MedicationListAdapter(medicationList);

        medicationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicationsRecyclerView.setAdapter(adapter);

        loadMedications();

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        });

        addMedicationButton.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, AddMedicationActivity.class));
        });
    }

    private void loadMedications() {
        databaseReference.orderByChild("userId").equalTo(currentUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        medicationList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Medication medication = snapshot.getValue(Medication.class);
                            if (medication != null) {
                                medication.setId(snapshot.getKey());
                                medicationList.add(medication);
                            }
                        }

                        // Show/hide empty state
                        if (medicationList.isEmpty()) {
                            emptyListTextView.setVisibility(View.VISIBLE);
                            medicationsRecyclerView.setVisibility(View.GONE);
                        } else {
                            emptyListTextView.setVisibility(View.GONE);
                            medicationsRecyclerView.setVisibility(View.VISIBLE);
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                        emptyListTextView.setText("Error loading medications");
                        emptyListTextView.setVisibility(View.VISIBLE);
                        medicationsRecyclerView.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMedications();
    }
}