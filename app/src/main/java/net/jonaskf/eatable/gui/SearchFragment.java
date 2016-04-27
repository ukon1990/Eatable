package net.jonaskf.eatable.gui;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import net.jonaskf.eatable.R;
import net.jonaskf.eatable.adapter.ProductAdapter;
import net.jonaskf.eatable.global.Vars;
import net.jonaskf.eatable.product.Allergen;
import net.jonaskf.eatable.product.Producer;
import net.jonaskf.eatable.product.Product;

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
 * Søkefunksjonaliteten er basert på følgende guide:
 * http://developer.android.com/guide/topics/search/search-dialog.html
 */
public class SearchFragment extends Fragment {
    private View view;
    private ProductAdapter adapter;
    private ListView listView;
    private SearchView searchView;
    private List<Product> resultList = new ArrayList<>();

    public SearchFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);
        //Changing actionbar title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.search_fragment_title);

        //Search
        searchView = (SearchView) view.findViewById(R.id.product_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                productSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productSearch(newText);
                return false;
            }
        });
        //The list
        listView = (ListView) view.findViewById(R.id.product_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Vars.ean = resultList.get(position).getId();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ResultFragment(), Vars._RESULT_FRAGMENT).addToBackStack(null).commit();
            }
        });

        getAllProducers();
        return view;
    }

    public void getAllProducers(){
        DownloadProducers dl = new DownloadProducers();
        dl.execute(Vars.GET_PRODUCERS);
    }
    /**
     * Searching the db
     */
    private void productSearch(String query){
        DownloadProduct dl = new DownloadProduct();
        dl.execute(Vars.GET_PRODUCTS + Vars.Q_SEARCH + query);
    }
    public void productSearchResult(){
        adapter = new ProductAdapter(getActivity(), android.R.layout.simple_list_item_1);

        for(int i = 0; i<resultList.size()-1; i++){
            Log.d("test", "Prod: " + resultList.get(i).getName());
        }

        listView.setAdapter(adapter);
        adapter.addAll(resultList);
    }
    private class DownloadProduct extends AsyncTask<String, Integer, JSONArray> {
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
                            new Product(
                                ((JSONObject) jArr.get(i)).getString("productID"),
                                ((JSONObject) jArr.get(i)).getString("productName"),
                                null,
                                null,
                                ((JSONObject) jArr.get(i)).getString("last_updated"),
                                ((JSONObject) jArr.get(i)).getString("producerID")
                        ) );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            productSearchResult();
        }
    }

    //Producers
    private class DownloadProducers extends AsyncTask<String, Integer, JSONArray> {
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
            Producer.list.clear();
            //populating the producer list
            JSONArray jArr = result;
            for(int i = 0; i < jArr.length(); i++) {
                try {
                    Producer.addProducer((JSONObject) jArr.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            productSearch("");
        }
    }
}
