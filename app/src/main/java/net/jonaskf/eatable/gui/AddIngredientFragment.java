package net.jonaskf.eatable.gui;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import net.jonaskf.eatable.R;
import net.jonaskf.eatable.adapter.IngredientAdapter;
import net.jonaskf.eatable.global.Vars;
import net.jonaskf.eatable.product.Ingredient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddIngredientFragment extends Fragment {
    private View view;
    private SearchView searchView;
    private ListView listView;
    private IngredientAdapter adapter;
    private List<Ingredient> resultList = new ArrayList<>();

    public AddIngredientFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_ingredient, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.add_product_ingredient_fragment);

        //Search support
        searchView = (SearchView) view.findViewById(R.id.ingredient_searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                ingredientSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ingredientSearch(newText);
                return false;
            }
        });

        //The list
        listView = (ListView) view.findViewById(R.id.ingredient_search_list);

        //Making an empty query to populate the list with everything
        ingredientSearch("");

        for(String key : Ingredient.list.keySet()){
            Log.d("test", "Content of ingredient list: " + Ingredient.list.get(key).getName());
        }
        return view;
    }

    private void ingredientSearch(String query){
        resultList.clear();
        DownloadIngredient dl = new DownloadIngredient();
        dl.execute(Vars.GET_INGREDIENTS + Vars.Q_SEARCH + query);
    }
    public void dietSearchResult(){
        adapter = new IngredientAdapter(getActivity(), android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        adapter.addAll(resultList);
    }

    private class DownloadIngredient extends AsyncTask<String, Integer, JSONArray> {
        @Override
        protected JSONArray doInBackground(String... urls) {
            URLConnection uConn;
            try{
                //Getting that data
                URL url = new URL(urls[0]);
                uConn = url.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(uConn.getInputStream(), "UTF-8")
                );

                JSONArray obj = new JSONArray();
                try {
                    obj= new JSONArray(in.readLine());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                in.close();
                return obj;
            }catch(IOException ex){
                ex.printStackTrace();
                Log.d("Download", "Failed! :(");
            }
            return null;
        }

        protected void onPostExecute(JSONArray result){
            resultList.clear();
            //populating
            JSONArray jArr = result;
            Log.d("adddiet", "The array: "+result.toString());
            for(int i = 0; i < jArr.length(); i++) {
                try {
                    resultList.add(
                            new Ingredient(
                                    ((JSONObject) jArr.get(i)).getString("ingredientID"),
                                    ((JSONObject) jArr.get(i)).getString("name"),
                                    null,
                                    null,
                                    null,
                                    null
                            ) );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            dietSearchResult();
        }
    }
}
