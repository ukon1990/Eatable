package net.jonaskf.eatable.gui;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.jonaskf.eatable.R;
import net.jonaskf.eatable.product.Allergen;
import net.jonaskf.eatable.product.Source;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment {


    public ResultFragment() {
        // Required empty public constructor
    }

    private View view;
    private TextView productTitleTextView; //For product name
    private TextView productIngredients; //For the product ingredients
    private TextView productAllergens;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_result,container, false);

        Log.d("Test", MainActivity.ean);
        productTitleTextView = ((TextView) view.findViewById(R.id.product_title));
        productIngredients = ((TextView) view.findViewById(R.id.ingredient_list));
        productAllergens = ((TextView) view.findViewById(R.id.product_acceptance));
        //((TextView) inflater.inflate(R.layout.fragment_result, container, false).findViewById(R.id.textView)).setText("Random");


        getProductData(MainActivity.ean);
        return view;

    }

    public void getProductData (String ean){
        DownloadFileTask download = new DownloadFileTask();
        String url = "http://frigg.hiof.no/android_v165/GetProducts.php?ean=" + ean;
        download.execute(url);
    }

    private void getJsonObject(JSONObject jsonObject){
        String eanCode ="";
        String productName ="";
        String ingredients ="";
        String allergenText = "";
        JSONArray ingredientList;

        HashMap<Integer, String> allergens = new HashMap<>();
        try {
            eanCode = jsonObject.getString("ean");
            productName = jsonObject.getString("productName");
            ingredientList = jsonObject.getJSONArray("ingredients");

            for(int i = 0; i < ingredientList.length(); i++){
                if(i == 0){
                    //Adding the first ingredient
                    ingredients += ((JSONObject)ingredientList.get(i)).getString("name").toUpperCase().subSequence(0,1)
                                    + ((JSONObject)ingredientList.get(i)).getString("name").substring(1);
                }else if(i == ingredientList.length()-1){
                    //Adding the last
                   ingredients += " og " + ((JSONObject)ingredientList.get(i)).getString("name") + ".";
                }else{
                    ingredients += ", " + ((JSONObject)ingredientList.get(i)).getString("name");
                }

                if(
                        !allergens.containsKey(((JSONObject) ingredientList.get(i)).getInt("allergenid"))
                                && ((JSONObject)ingredientList.get(i)).getInt("allergenid") != 0
                ){
                    allergens.put(
                            ((JSONObject)ingredientList.get(i)).getInt("allergenid"),
                            Allergen.list.get(((JSONObject)ingredientList.get(i)).getInt("allergenid")).getAllergen()

                    );
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Listing up all the ingredients
        int c = allergens.size();
        for(Integer i : allergens.keySet()){
            if(c == allergens.size()){
                allergenText += allergens.get(i).toUpperCase().substring(0,1) + allergens.get(i).substring(1);
                Log.d("test", c + "/" + allergens.size());
            }else if(c == 1){
                allergenText += " og " + allergens.get(i) + ".";
                Log.d("test", c + "/" + allergens.size());
            }else {
                allergenText += ", " + allergens.get(i);
                Log.d("test", c + "/" + allergens.size());
            }
            c--;
        }
        productTitleTextView.setText(productName + "\n" + eanCode);
        productIngredients.setText(ingredients);
        if(allergens.size() > 0){
            productAllergens.setText(allergenText);
        }
        Log.d("product", eanCode + "(" + productName + ")");
        allergens.clear();

    }

    public void updateText(String text){
        TextView textView = (TextView) getActivity().findViewById(R.id.textView);
        textView.setText(text);
    }
    //Async to allow download on a thread other than main thread
    private class DownloadFileTask extends AsyncTask<String, Integer, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... urls) {
            URLConnection uConn;
            try{
                Log.d("Download", "Started!");
                //Getting that data
                String result ="";
                URL url = new URL(urls[0]);
                uConn = url.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(uConn.getInputStream(), "UTF-8")
                );

                //Setting result variable
                //String obj ="";
                JSONObject obj = new JSONObject();
                try {
                    obj= new JSONObject(in.readLine());
                    Log.d("Download", "Not failed");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                in.close();
                Log.d("Download", "Completed! ");
                return obj;
            }catch(IOException ex){
                ex.printStackTrace();
                Log.d("Download", "Failed! :(");
            }
            return null;
        }

        protected void onPostExecute(JSONObject result){
            //TODO: Open file
            getJsonObject(result);
        }
    }
}
