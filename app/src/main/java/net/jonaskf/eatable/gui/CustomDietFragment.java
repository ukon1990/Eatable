package net.jonaskf.eatable.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import net.jonaskf.eatable.AddToCustomDietFragment;
import net.jonaskf.eatable.R;
import net.jonaskf.eatable.adapter.AllergenAdapter;
import net.jonaskf.eatable.adapter.DietAdapter;
import net.jonaskf.eatable.adapter.SourceAdapter;
import net.jonaskf.eatable.adapter.TypeAdapter;
import net.jonaskf.eatable.diet.Diet;
import net.jonaskf.eatable.global.Lists;
import net.jonaskf.eatable.global.Vars;
import net.jonaskf.eatable.product.Allergen;
import net.jonaskf.eatable.product.Source;
import net.jonaskf.eatable.product.Type;

import java.util.HashMap;


public class CustomDietFragment extends Fragment{

    private View view;
    private Spinner spinner;
    private ListView listView;
    private EditText editText;
    private SourceAdapter sourceAdapter;
    private AllergenAdapter allergenAdapter;
    private TypeAdapter typeAdapter;

    private String dietName;
    public static int selectedType = 0;

    public CustomDietFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_custom_diet, container, false);
        //Changing actionbar title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.custom_diet_title);

        //The list
        listView = (ListView) view.findViewById(R.id.custom_diet_list_view);
        editText = (EditText) view.findViewById(R.id.define_diet_name);
        //Creating a combobox/dropdown menu for selecting
        spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.custom_diet_spinner, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);


        //Lsteners
        view.findViewById(R.id.add_to_diet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddToCustomDietFragment(), Vars._ADD_TO_CUSTOM_DIET_FRAGMENT).addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.create_diet_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //All custom diets have a negative numerical value as key
                Log.d("","");
                Diet.list.put(
                        "-" + String.valueOf(Diet.list.size()),
                        new Diet(
                                "-" + String.valueOf(Diet.list.size()),
                                dietName,
                                (HashMap<String, Source>) Lists.customSourceList.clone(),
                                (HashMap<String, Type>) Lists.customTypeList.clone(),
                                (HashMap<String, Allergen>) Lists.customAllergenList.clone()
                        )
                );
                //Updating the "all" lists
                Diet.updateLists();
                //Cleaning up the lists so that we can add a new diet later on
                Lists.customAllergenList.clear();
                Lists.customTypeList.clear();
                Lists.customSourceList.clear();
                //Moving over to the my diet fragment
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyDietFragment(), Vars._MY_DIETS_FRAGMENT).addToBackStack(null).commit();
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                dietName = editText.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dietName = editText.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                dietName = editText.getText().toString();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    selectedType = position;
                    showAllergens();
                }else if(position == 1) {
                    selectedType = position;
                    showSources();
                }else if(position == 2){
                    selectedType = position;
                    showTypes();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }


    private void showAllergens(){
        allergenAdapter = new AllergenAdapter(getActivity(), android.R.layout.simple_list_item_1);

        listView.setAdapter(allergenAdapter);
        allergenAdapter.addAll(Lists.customAllergenList.values());
    }
    private void showSources(){
        sourceAdapter = new SourceAdapter(getActivity(), android.R.layout.simple_list_item_1);

        listView.setAdapter(sourceAdapter);
        sourceAdapter.addAll(Lists.customSourceList.values());
    }
    private void showTypes(){
        typeAdapter = new TypeAdapter(getActivity(), android.R.layout.simple_list_item_1);
        listView.setAdapter(typeAdapter);
        typeAdapter.addAll(Lists.customTypeList.values());
    }
}
