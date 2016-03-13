package net.jonaskf.eatable.gui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.jonaskf.eatable.R;
import net.jonaskf.eatable.product.Allergen;
import net.jonaskf.eatable.product.Product;
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
import java.util.List;


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
        download.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
    }

    private void getProduct(JSONObject obj){
        String ean = MainActivity.ean;
        String allergenText = "";
        String ingredientText ="";
        HashMap<Integer, String> allergens = new HashMap<>();
        int i = 0;
        if(!Product.list.containsKey(ean)){
            Product.addProduct(obj);
            try{
                Log.d("Product", obj.toString());
            }catch(Exception e){e.printStackTrace();}
        }
        //Building ingredient string
        for(int key : Product.list.get(ean).getIngredients().keySet()){
            if(i == 0){
                //Adding the first ingredient
                ingredientText += Product.list.get(ean).getIngredients().get(key).getName().toUpperCase().subSequence(0,1)
                        + Product.list.get(ean).getIngredients().get(key).getName().substring(1);
            }else if(i == Product.list.get(ean).getIngredients().size()-1){
                //Adding the last
                ingredientText += " og " + Product.list.get(ean).getIngredients().get(key).getName() + ".";
            }else{
                ingredientText += ", " + Product.list.get(ean).getIngredients().get(key).getName();
            }
            i++;
            //Building list of found allergens in this product
            if(!allergens.containsKey(Integer.parseInt(Product.list.get(ean).getIngredients().get(key).getAllergenID()))){
                allergens.put(Integer.parseInt(Product.list.get(ean).getIngredients().get(key).getAllergenID()), Allergen.list.get(Integer.parseInt(Product.list.get(ean).getIngredients().get(key).getAllergenID())).getAllergen());
            }

        }
        //Building allergen string
        if(allergens.size() != 0){
            int c = allergens.size();
            for(Integer a : allergens.keySet()){
                if(a != 0){
                    if(c == allergens.size()){
                        allergenText += allergens.get(a).toUpperCase().substring(0,1) + allergens.get(a).substring(1) + (c == 1 ? ".":"");
                    }else if(c == 1){
                        allergenText += " og " + allergens.get(a) + ".";
                    }else {
                        allergenText += ", " + allergens.get(a);
                    }
                    c--;
                }
            }
            allergens.clear();
        }
        //Setting text label content
        productTitleTextView.setText(Product.list.get(ean).getName());
        if(allergenText.length()>0){
            productAllergens.setText(allergenText);
        }
        productIngredients.setText(ingredientText);
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
        //Listing up all the allergens
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
            Log.d("Download", "Start");
            URLConnection uConn;
            try{
                Log.d("Download", "Started! - " + urls);
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
                    String line;
                    //Log.d("Download", "Read: " + in.read() + " contets: " + in.readLine());
                    //TODO: Løs uten bruk av string om mulig (For veldig store lister)
                    while((line = in.readLine()) != null)
                        obj= new JSONObject(line);

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
            Log.d("Download", "End");
            return null;
        }

        protected void onPostExecute(JSONObject result){
            //Checking if there was a result or not
            if(!result.isNull("ean")){
                getProduct(result);
                //getJsonObject(result);
            }else{
                //Opening a dialog box if product don't exsist in DB
                new AlertDialog.Builder(getContext())
                        .setTitle(MainActivity.ean)//R.string.product_does_not_exist_title)
                        .setMessage(R.string.product_does_not_exist_msg)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                //Logic
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Logic
                            }
                        })
                        .setIcon(R.drawable.ic_menu_manage)
                        .show();
            }
        }
    }
}
