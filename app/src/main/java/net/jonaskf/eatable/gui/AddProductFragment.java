package net.jonaskf.eatable.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import net.jonaskf.eatable.R;
import net.jonaskf.eatable.adapter.DietAdapter;
import net.jonaskf.eatable.adapter.IngredientAdapter;
import net.jonaskf.eatable.diet.Diet;
import net.jonaskf.eatable.global.Vars;
import net.jonaskf.eatable.product.Ingredient;


public class AddProductFragment extends Fragment {
    private View view;
    private SearchView searchView;
    private ListView listView;
    private IngredientAdapter adapter;

    public AddProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_diet, container, false);
        //Changing actionbar title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.my_diets_title);

        //The list
        listView = (ListView) view.findViewById(R.id.my_diet_list);
        showMyDiets();

        //On click listener for in-fragment method
        view.findViewById(R.id.add_diet_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddDietFragment(), Vars._ADD_DIET_FRAGMENT).addToBackStack(null).commit();
            }
        });
        return view;
    }

    private void showMyDiets(){
        adapter = new IngredientAdapter(getActivity(), android.R.layout.simple_list_item_1);

        listView.setAdapter(adapter);
        adapter.addAll(Ingredient.list.values());
    }

}
