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

public class AddMedicationActivity extends AppCompatActivity {

    private EditText nameEditText, dosageEditText, frequencyEditText,
            instructionsEditText, startDateEditText, endDateEditText,
            prescriberEditText;
    private Button saveButton, cancelButton;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("medications");

        nameEditText = findViewById(R.id.nameEditText);
        dosageEditText = findViewById(R.id.dosageEditText);
        frequencyEditText = findViewById(R.id.frequencyEditText);
        instructionsEditText = findViewById(R.id.instructionsEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        prescriberEditText = findViewById(R.id.prescriberEditText);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> saveMedication());

        // Set up cancel button
        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> finish());
    }

    private void saveMedication() {
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

        String medicationId = databaseReference.push().getKey();
        Medication medication = new Medication(name, dosage, frequency, instructions,
                startDate, endDate, prescriber,
                currentUser.getUid());

        if (medicationId != null) {
            databaseReference.child(medicationId).setValue(medication)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddMedicationActivity.this,
                                "Medication saved successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddMedicationActivity.this,
                                "Failed to save medication: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
        }
    }
}