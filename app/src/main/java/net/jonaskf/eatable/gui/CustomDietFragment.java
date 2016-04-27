package net.jonaskf.eatable.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import net.jonaskf.eatable.R;
import net.jonaskf.eatable.adapter.DietAdapter;
import net.jonaskf.eatable.diet.Diet;
import net.jonaskf.eatable.global.Vars;


public class CustomDietFragment extends Fragment{

    private View view;
    private Spinner spinner;
    private ListView listView;
    private DietAdapter dietAdapter;
    public CustomDietFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_custom_diet, container, false);
        //Changing actionbar title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.my_diets_title);

        //The list
        listView = (ListView) view.findViewById(R.id.custom_diet_list_view);
        //showMyDiets();
        spinner = (Spinner) view.findViewById(R.id.spinner); //custom_diet_spinner


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.custom_diet_spinner, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the spinnerAdapter to the spinner
        spinner.setAdapter(spinnerAdapter);


        //On click listener for in-fragment method
        view.findViewById(R.id.add_to_diet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddDietFragment(), Vars._ADD_DIET_FRAGMENT).addToBackStack(null).commit();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("click", "Item selected!");
                if(position == 0)
                    showAllergens();
                else if(position == 1)
                    showAllergens();
                else if(position == 2)
                    showAllergens();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("click", "Nothign selected!");
            }
        });
        return view;
    }


    private void showAllergens(){
        dietAdapter = new DietAdapter(getActivity(), android.R.layout.simple_list_item_1);

        listView.setAdapter(dietAdapter);
        dietAdapter.addAll(Diet.list.values());
    }
    private void showSources(){
        dietAdapter = new DietAdapter(getActivity(), android.R.layout.simple_list_item_1);

        listView.setAdapter(dietAdapter);
        dietAdapter.addAll(Diet.list.values());
    }
}
