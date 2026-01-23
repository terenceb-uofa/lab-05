package com.example.updatedlistycity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {
    interface AddCityDialogListener {
        void addCity(City city);
        void editCity(City city, int index);
    }
    private AddCityDialogListener listener;

    public static AddCityFragment newInstance(City city, int index) {
        Bundle args = new Bundle();
        args.putSerializable("city", city);
        args.putInt("index", index); // Pass the position in the list
        AddCityFragment fragment = new AddCityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view =
                LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Check if we are editing an existing city
        City existingCity = (getArguments() != null) ? (City) getArguments().getSerializable("city") : null;
        int index = (getArguments() != null) ? getArguments().getInt("index", -1) : -1;

        String title = "Add City";
        if (existingCity != null) {
            title = "Edit City";
            editCityName.setText(existingCity.getName());
            editProvinceName.setText(existingCity.getProvince());
        }

        return builder
                .setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel", null)
                .setPositiveButton(existingCity != null ? "Update" : "Add", (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();
                    City newCity = new City(cityName, provinceName);
                    if (index != -1) {
                        listener.editCity(newCity, index); // REPLACE mode
                    } else {
                        listener.addCity(newCity); // ADD mode
                    }

                })
                .create();
    }
}