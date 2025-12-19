package com.example.finalprojectmedicationmanager.data;

import androidx.lifecycle.LiveData;
import com.example.finalprojectmedicationmanager.models.Medication;
import java.util.List;

public interface IMedicationRepository {

    // Retrieves data for the current user
    LiveData<List<Medication>> getAllUserMedications(String userId);

    // Saves a new medication
    void saveMedication(Medication medication, Callback<Boolean> callback);

    // Deletes a medication by its ID.
    void deleteMedication(String medicationId, Callback<Boolean> callback);

    // Simple callback interface for asynchronous operations
    interface Callback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);

        void onSuccess();

        void onError(Exception e);
    }
}