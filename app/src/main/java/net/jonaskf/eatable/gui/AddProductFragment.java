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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
                try {
                    //TODO: http://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
                    for(String key : Ingredient.list.keySet()){
                        //Looping throught each in gredient to add it into the product in the DB
                        String statement = "INSERT INTO product_has_ingredient VALUES (" + Vars.ean + ","+ key +")";
                        URL query = new URL(Vars.INSERT_INTO + Vars.Q_KEY + Vars.API_KEY + "&" + Vars.Q_INSERT + statement);
                        HttpURLConnection con = (HttpURLConnection) query.openConnection();
                        con.setRequestMethod("GET");
                        con.setRequestProperty("User-Agent", "Mozilla/5.0");
                        System.out.println("Query -> " + statement);
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(con.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        //print result
                        System.out.println(response.toString());

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
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
