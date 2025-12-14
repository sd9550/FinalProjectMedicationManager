package com.example.finalprojectmedicationmanager;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.finalprojectmedicationmanager.models.Medication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MedicationDetailActivity extends AppCompatActivity {

    private EditText nameEditText, dosageEditText, frequencyEditText,
            instructionsEditText, startDateEditText, endDateEditText,
            prescriberEditText;
    private Button updateButton, deleteButton;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private Medication medication;
    private String medicationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("medications");

        medication = (Medication) getIntent().getSerializableExtra("medication");
        if (medication == null) {
            finish();
            return;
        }

        medicationId = getIntent().getStringExtra("medicationId");

        nameEditText = findViewById(R.id.nameEditText);
        dosageEditText = findViewById(R.id.dosageEditText);
        frequencyEditText = findViewById(R.id.frequencyEditText);
        instructionsEditText = findViewById(R.id.instructionsEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        prescriberEditText = findViewById(R.id.prescriberEditText);
        updateButton = findViewById(R.id.saveButton);
        updateButton.setText("Update Medication");

        // Add delete button if not present in layout
        deleteButton = new Button(this);
        deleteButton.setText("Delete Medication");
        deleteButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        deleteButton.setTextColor(getResources().getColor(android.R.color.white));

        loadMedicationData();

        updateButton.setOnClickListener(v -> updateMedication());
        deleteButton.setOnClickListener(v -> deleteMedication());
    }

    private void loadMedicationData() {
        nameEditText.setText(medication.getName());
        dosageEditText.setText(medication.getDosage());
        frequencyEditText.setText(medication.getFrequency());
        instructionsEditText.setText(medication.getInstructions());
        startDateEditText.setText(medication.getStartDate());
        endDateEditText.setText(medication.getEndDate());
        prescriberEditText.setText(medication.getPrescriber());
    }

    private void updateMedication() {
        String name = nameEditText.getText().toString().trim();
        String dosage = dosageEditText.getText().toString().trim();
        String frequency = frequencyEditText.getText().toString().trim();
        String instructions = instructionsEditText.getText().toString().trim();
        String startDate = startDateEditText.getText().toString().trim();
        String endDate = endDateEditText.getText().toString().trim();
        String prescriber = prescriberEditText.getText().toString().trim();

        if (name.isEmpty() || dosage.isEmpty() || frequency.isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        Medication updatedMedication = new Medication(name, dosage, frequency, instructions,
                startDate, endDate, prescriber,
                currentUser.getUid());

        if (medicationId != null) {
            databaseReference.child(medicationId).setValue(updatedMedication)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Medication updated successfully",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to update medication: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void deleteMedication() {
        if (medicationId != null) {
            databaseReference.child(medicationId).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Medication deleted successfully",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to delete medication: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
        }
    }
}