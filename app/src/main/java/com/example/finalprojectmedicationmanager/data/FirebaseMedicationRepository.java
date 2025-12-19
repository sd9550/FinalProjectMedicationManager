package com.example.finalprojectmedicationmanager.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.finalprojectmedicationmanager.models.Medication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

// This class implements the abstraction
public class FirebaseMedicationRepository implements IMedicationRepository {

    private final DatabaseReference databaseReference;
    private final FirebaseUser currentUser;

    public FirebaseMedicationRepository() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference("medications");
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public LiveData<List<Medication>> getAllUserMedications(String userId) {
        MutableLiveData<List<Medication>> liveData = new MutableLiveData<>();

        databaseReference.orderByChild("userId").equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Medication> medicationList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Medication medication = snapshot.getValue(Medication.class);
                            if (medication != null) {
                                // Set the key as the ID for updates/deletes
                                medication.setId(snapshot.getKey());
                                medicationList.add(medication);
                            }
                        }
                        liveData.setValue(medicationList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle errors like a null list
                        liveData.setValue(new ArrayList<>());
                    }
                });
        return liveData;
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    @Override
    public void saveMedication(Medication medication, Callback<Boolean> callback) {
        String medicationId = medication.getId();
        if (medicationId == null || medicationId.isEmpty()) {
            // New medication
            medicationId = databaseReference.push().getKey();
            medication.setId(medicationId); // Update model with key
        }

        if (medicationId != null) {
            databaseReference.child(medicationId).setValue(medication)
                    .addOnSuccessListener(aVoid -> callback.onSuccess(true)) // Successful save
                    .addOnFailureListener(callback::onFailure); // Failed save
        } else {
            callback.onFailure(new Exception("Could not generate a medication ID."));
        }
    }

    @Override
    public void deleteMedication(String medicationId, Callback<Boolean> callback) {
        if (medicationId != null) {
            databaseReference.child(medicationId).removeValue()
                    .addOnSuccessListener(aVoid -> callback.onSuccess(true)) // Successful delete
                    .addOnFailureListener(callback::onFailure); // Failed delete
        }
    }
}