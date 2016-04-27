package net.jonaskf.eatable;


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
import android.widget.Spinner;

import net.jonaskf.eatable.adapter.AllergenAdapter;
import net.jonaskf.eatable.adapter.SourceAdapter;
import net.jonaskf.eatable.adapter.TypeAdapter;
import net.jonaskf.eatable.global.Vars;
import net.jonaskf.eatable.gui.CustomDietFragment;
import net.jonaskf.eatable.product.Allergen;
import net.jonaskf.eatable.product.Source;
import net.jonaskf.eatable.product.Type;

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
public class AddToCustomDietFragment extends Fragment {

    private View view;
    private Spinner spinner;
    private ListView listView;
    private SourceAdapter sourceAdapter;
    private AllergenAdapter allergenAdapter;
    private TypeAdapter typeAdapter;

    //Query result lists
    private List<Allergen> allergens = new ArrayList<>();
    private List<Source> sources = new ArrayList<>();
    private List<Type> types = new ArrayList<>();

    public AddToCustomDietFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_to_custom_diet, container, false);

        //The list
        listView = (ListView) view.findViewById(R.id.custom_diet_list_view);

        //Defining what adapter to use and changing actionbar title
        if(CustomDietFragment.selectedType == 0) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.add_allergen_to_custom_diet);
            allergenSearch("");
        }else if(CustomDietFragment.selectedType == 1) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.add_source_to_custom_diet);
            sourceSearch("");
        }else if(CustomDietFragment.selectedType == 2){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.add_type_to_custom_diet);
            typeSearch("");
        }

        //Listeners
        ((SearchView)view.findViewById(R.id.searchViewCustom)).setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(CustomDietFragment.selectedType == 0) {
                    allergenSearch(query);
                }else if(CustomDietFragment.selectedType == 1) {
                    sourceSearch(query);
                }else if(CustomDietFragment.selectedType == 2){
                    typeSearch(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(CustomDietFragment.selectedType == 0) {
                    allergenSearch(newText);
                }else if(CustomDietFragment.selectedType == 1) {
                    sourceSearch(newText);
                }else if(CustomDietFragment.selectedType == 2){
                    typeSearch(newText);
                }
                return false;
            }
        });
        return view;
    }

    //Downloading content
    private void allergenSearch(String query){
        DownloadAllergen dl = new DownloadAllergen();
        dl.execute(Vars.GET_ALLERGENS + Vars.Q_SEARCH + query);
    }
    private void sourceSearch(String query){
        DownloadSources dl = new DownloadSources();
        dl.execute(Vars.GET_SOURCES + Vars.Q_SEARCH + query);
    }
    private void typeSearch(String query){
        DownloadTypes dl = new DownloadTypes();
        dl.execute(Vars.GET_TYPES + Vars.Q_SEARCH + query);
    }


    //Populating the list
    private void listAllergens(){
        allergenAdapter = new AllergenAdapter(getActivity(), android.R.layout.simple_list_item_1);
        listView.setAdapter(allergenAdapter);
        allergenAdapter.addAll(allergens);
    }
    private void listSources(){
        sourceAdapter = new SourceAdapter(getActivity(), android.R.layout.simple_list_item_1);

        listView.setAdapter(sourceAdapter);
        sourceAdapter.addAll(sources);
    }
    private void listTypes(){
        typeAdapter = new TypeAdapter(getActivity(), android.R.layout.simple_list_item_1);
        listView.setAdapter(typeAdapter);
        typeAdapter.addAll(types);
    }


    //Allergens
    private class DownloadAllergen extends AsyncTask<String, Integer, JSONArray> {
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
            allergens.clear();
            //populating
            JSONArray jArr = result;
            for(int i = 0; i < jArr.length(); i++) {
                try {
                    allergens.add(
                            new Allergen(
                                    ((JSONObject) jArr.get(i)).getString("allergenid"),
                                    ((JSONObject) jArr.get(i)).getString("allergen")
                            )
                    );
                } catch (JSONException e) {
                    Log.d("allergen", "failed - " + result.toString());
                    e.printStackTrace();
                }
            }
            listAllergens();
        }
    }
    //Sources
    private class DownloadSources extends AsyncTask<String, Integer, JSONArray> {
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
            sources.clear();
            //populating
            JSONArray jArr = result;
            for(int i = 0; i < jArr.length(); i++){
                try {
                    sources.add(
                            new Source(
                                    ((JSONObject) jArr.get(i)).getString("sourceID"),
                                    ((JSONObject) jArr.get(i)).getString("source"))
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            listSources();
        }
    }

    //Types
    private class DownloadTypes extends AsyncTask<String, Integer, JSONArray> {
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
            types.clear();
            //populating
            JSONArray jArr = result;
            for(int i = 0; i < jArr.length(); i++){
                try {
                    Log.d("test", result.toString());
                    types.add(
                            new Type(
                                    ((JSONObject) jArr.get(i)).getString("typeID"),
                                    ((JSONObject) jArr.get(i)).getString("ingredientType"))
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            listTypes();
        }
    }
}