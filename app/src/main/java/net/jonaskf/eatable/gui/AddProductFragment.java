package net.jonaskf.eatable.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import net.jonaskf.eatable.R;
import net.jonaskf.eatable.adapter.IngredientAdapter;
import net.jonaskf.eatable.global.Vars;
import net.jonaskf.eatable.product.Ingredient;

import java.net.MalformedURLException;
import java.net.URL;


public class AddProductFragment extends Fragment {
    private View view;
    private SearchView searchView;
    private ListView listView;
    private IngredientAdapter adapter;
    private Button addIngredient;
    private Button addProduct;

    public AddProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_product, container, false);
        //Changing actionbar title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.add_product_fragment_title);
        //Buttons
        addIngredient = (Button) view.findViewById(R.id.add_ingredient_btn);
        addProduct = (Button) view.findViewById(R.id.add_product);

        //List
        listView = (ListView) view.findViewById(R.id.ingredient_list);
        //Click listeners

        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddIngredientFragment(), Vars._ADD_INGREDIENT_FRAGMENT).addToBackStack(null).commit();
            }
        });
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Logic for adding ingredient to list
                String statement = "";
                try {
                    URL query = new URL(Vars.INSERT_INTO + Vars.Q_KEY + Vars.API_KEY + "&" + Vars.Q_INSERT + statement);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        //Populating the list with current ingredients
        showMyDiets();
        return view;
    }

    private void showMyDiets(){
        adapter = new IngredientAdapter(getActivity(), android.R.layout.simple_list_item_1);

        listView.setAdapter(adapter);
        adapter.addAll(Ingredient.list.values());
    }
}
