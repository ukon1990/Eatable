package net.jonaskf.eatable.gui;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import net.jonaskf.eatable.R;
import net.jonaskf.eatable.adapter.DietAdapter;
import net.jonaskf.eatable.diet.Diet;
import net.jonaskf.eatable.global.Vars;
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
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddDietFragment extends Fragment {

    private View view;
    private SearchView searchView;
    private ListView listView;
    private DietAdapter adapter;
    private List<Diet> resultList = new ArrayList<>();

    public AddDietFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_diet, container, false);

        //Search support
        searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                dietSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                dietSearch(newText);
                return false;
            }
        });


        //The list
        listView = (ListView) view.findViewById(R.id.diet_search_list);
        return view;
    }

    private void dietSearch(String query){
        DownloadDiet dl = new DownloadDiet();
        dl.execute(Vars.GET_DIETS + Vars.Q_SEARCH + query);
    }
    public void dietSearchResult(){
        adapter = new DietAdapter(getActivity(), android.R.layout.simple_list_item_1);

        listView.setAdapter(adapter);
        adapter.addAll(resultList);
    }

    private class DownloadDiet extends AsyncTask<String, Integer, JSONArray> {
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
            for(int i = 0; i < jArr.length(); i++) {
                try {
                    resultList.add(
                            new Diet(
                                    ((JSONObject) jArr.get(i)).getString("dietID"),
                                    new HashMap<String, Source>(),
                                    new HashMap<String, Type>(),
                                    new HashMap<String, Allergen>()
                            ) );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Log.d("test", resultList.get(i).getName()+ " size -> " + resultList.size());
            }
            dietSearchResult();
        }
    }
}
