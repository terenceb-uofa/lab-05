package com.example.updatedlistycity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements AddCityFragment.AddCityDialogListener {

    private EditText cityInput;
    private Button deleteButton;
    private Button addButton;
    private Button confirmButton;
    private ListView cityList;

    ArrayAdapter<City> cityAdapter;
    ArrayList<City> dataList;
    LinearLayout inputContainer;

    int selectedIndex= -1; // The index of the selected city (-1 is the "no select" state)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get elements
        initViews();
        //Setup the patched array adapter
        setupAdapter();
        //Input Listeners
        setupListeners();


    }




    private void initViews() {
        //Assign all local view variables their appropriate view

        //Layout
        cityList = findViewById(R.id.city_list);
        inputContainer = findViewById(R.id.city_field_container);

        //Buttons
        deleteButton = findViewById(R.id.delete_city);
        addButton = findViewById(R.id.add_city);
        confirmButton = findViewById(R.id.city_input_confirm);

        //Input field
        cityInput = findViewById(R.id.city_input);
    }


    private  void setupAdapter(){
        String[] cities = { "Edmonton", "Vancouver", "Toronto" };
        String[] provinces = { "AB", "BC", "ON" };

        dataList = new ArrayList<City>();
        for (int i = 0; i < cities.length; i++) {
            dataList.add(new City(cities[i], provinces[i]));
        }


        //Patching the array adapter class on assignment
        cityAdapter = new ArrayAdapter<City>(this, R.layout.content, R.id.city_text , dataList) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the default view object
                View view = super.getView(position, convertView, parent);

                //Patch logic for updating highlights
                if (position == selectedIndex) {
                    view.setBackgroundColor(android.graphics.Color.LTGRAY);
                } else {
                    view.setBackgroundColor(android.graphics.Color.TRANSPARENT);
                }

                City city = getItem(position);
                TextView cityName = view.findViewById(R.id.city_text);
                TextView provinceName = view.findViewById(R.id.province_text);
                cityName.setText(city.getName());
                provinceName.setText(city.getProvince());

                return view;
            }
        };

        cityList.setAdapter(cityAdapter);
    }

    private void setupListeners(){
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Set the new index to the position in the list of the element
                selectedIndex = position;
                cityAdapter.notifyDataSetChanged(); //re-render cities (selection proc)
                City selectedCity = dataList.get(position);
                AddCityFragment.newInstance(selectedCity, position).show(getSupportFragmentManager(), "Edit City");
            }
        });
        deleteButton.setOnClickListener(v -> {
            if (selectedIndex != -1) {
                Object cityName =  cityList.getItemAtPosition(selectedIndex).toString();
                Toast.makeText(this, String.format("%s was deleted!", cityName ), Toast.LENGTH_SHORT).show();

                dataList.remove(selectedIndex);
                selectedIndex = -1;              // reset to no selection
                cityAdapter.notifyDataSetChanged(); // re-render cities
            }
        });
        addButton.setOnClickListener(v -> {
            // Toggle visibility
            new AddCityFragment().show(getSupportFragmentManager(), "Add City");
        });
        confirmButton.setOnClickListener(v -> {
            // get city name input
            String cityName = cityInput.getText().toString();

            //validate name (early exit)
            if (cityName.isBlank()) {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                return;
            }
            //valid name



            //dataList.add(cityName);            // add city to the list
            cityAdapter.notifyDataSetChanged(); // notify adapter for re-render
            cityInput.setText("");
            String successMessage =  String.format("%s was added!", cityName);
            Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();


        });
    }

    @Override
    public void addCity(City city) {
        cityAdapter.add(city);
        cityAdapter.notifyDataSetChanged();
    }

    @Override
    public void editCity(City city, int index) {
        //set to replace an existing city
        dataList.set(index, city);
        cityAdapter.notifyDataSetChanged();
    }
}

