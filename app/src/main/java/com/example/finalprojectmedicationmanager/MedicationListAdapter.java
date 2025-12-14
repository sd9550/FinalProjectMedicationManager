package com.example.finalprojectmedicationmanager;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.finalprojectmedicationmanager.models.Medication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class MedicationListAdapter extends RecyclerView.Adapter<MedicationListAdapter.ViewHolder> {

    private List<Medication> medicationList;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    public MedicationListAdapter(List<Medication> medicationList) {
        this.medicationList = medicationList;
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.databaseReference = FirebaseDatabase.getInstance().getReference("medications");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medication, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Medication medication = medicationList.get(position);

        holder.nameTextView.setText(medication.getName());
        holder.dosageTextView.setText("Dosage: " + medication.getDosage());
        holder.frequencyTextView.setText("Frequency: " + medication.getFrequency());

        if (medication.getInstructions() != null && !medication.getInstructions().isEmpty()) {
            holder.instructionsTextView.setText("Instructions: " + medication.getInstructions());
            holder.instructionsTextView.setVisibility(View.VISIBLE);
        } else {
            holder.instructionsTextView.setVisibility(View.GONE);
        }

        if (medication.getPrescriber() != null && !medication.getPrescriber().isEmpty()) {
            holder.prescriberTextView.setText("Prescriber: " + medication.getPrescriber());
            holder.prescriberTextView.setVisibility(View.VISIBLE);
        } else {
            holder.prescriberTextView.setVisibility(View.GONE);
        }

        // Edit button click
        holder.editButton.setOnClickListener(v -> {
            // Launch edit activity
            android.content.Context context = v.getContext();
            android.content.Intent intent = new Intent(context, MedicationDetailActivity.class);
            intent.putExtra("medication", (Parcelable) medication);
            intent.putExtra("medicationId", medication.getId());
            context.startActivity(intent);
        });

        // Delete button click
        holder.deleteButton.setOnClickListener(v -> {
            if (medication.getId() != null) {
                databaseReference.child(medication.getId()).removeValue()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(v.getContext(), "Medication deleted",
                                    Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(v.getContext(), "Failed to delete",
                                    Toast.LENGTH_SHORT).show();
                        });
            }
        });

        // Whole item click
        holder.itemView.setOnClickListener(v -> {
            // Show details dialog or expand view
            showMedicationDetails(v.getContext(), medication);
        });
    }

    private void showMedicationDetails(android.content.Context context, Medication medication) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(medication.getName());

        StringBuilder details = new StringBuilder();
        details.append("Dosage: ").append(medication.getDosage()).append("\n\n");
        details.append("Frequency: ").append(medication.getFrequency()).append("\n\n");

        if (medication.getInstructions() != null && !medication.getInstructions().isEmpty()) {
            details.append("Instructions: ").append(medication.getInstructions()).append("\n\n");
        }

        if (medication.getPrescriber() != null && !medication.getPrescriber().isEmpty()) {
            details.append("Prescriber: ").append(medication.getPrescriber()).append("\n\n");
        }

        if (medication.getStartDate() != null && !medication.getStartDate().isEmpty()) {
            details.append("Start Date: ").append(medication.getStartDate()).append("\n\n");
        }

        if (medication.getEndDate() != null && !medication.getEndDate().isEmpty()) {
            details.append("End Date: ").append(medication.getEndDate());
        }

        builder.setMessage(details.toString());
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    @Override
    public int getItemCount() {
        return medicationList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, dosageTextView, frequencyTextView,
                instructionsTextView, prescriberTextView;
        Button editButton, deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.medicationNameTextView);
            dosageTextView = itemView.findViewById(R.id.medicationDosageTextView);
            frequencyTextView = itemView.findViewById(R.id.medicationFrequencyTextView);
            instructionsTextView = itemView.findViewById(R.id.medicationInstructionsTextView);
            prescriberTextView = itemView.findViewById(R.id.medicationPrescriberTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}