package net.jonaskf.eatable.gui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.jonaskf.eatable.R;
import net.jonaskf.eatable.diet.Diet;
import net.jonaskf.eatable.global.Vars;
import net.jonaskf.eatable.product.Allergen;
import net.jonaskf.eatable.product.Ingredient;
import net.jonaskf.eatable.product.Producer;
import net.jonaskf.eatable.product.Product;
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
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment {


    public ResultFragment() {
        // Required empty public constructor
    }

    private View view;
    private TextView productIngredients; //For the product ingredients
    private TextView productAllergens;
    private TextView producer;
    private TextView lastUpdated;
    private ImageView eatableIcon;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_result,container, false);

        //Initiatin some default text
        productIngredients = ((TextView) view.findViewById(R.id.ingredient_list));
        productAllergens = ((TextView) view.findViewById(R.id.product_acceptance));
        producer = (TextView) view.findViewById(R.id.producer_nametag);
        lastUpdated = (TextView) view.findViewById(R.id.last_updated_tag);
        eatableIcon = ((ImageView) view.findViewById(R.id.is_eatable_icon));
        //Changing actionbar title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.result_fragment_title);

        //Updating the users diet data list in case it's not updated yet
        Diet.updateLists();
        //Updating fragment contents
        getProductData(Vars.ean);
        return view;

    }

    public void getProductData (String ean){
        DownloadFileTask download = new DownloadFileTask();
        String url = Vars.GET_PRODUCTS + Vars.Q_EAN + ean;
        download.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
    }

    /**
     * This method is responsible for displaying the downloaded data to the user.
     * The ingredients are to be listed in a string, and if any allergens, sources
     * or types the user don't wish to consume are present, they are displayed as well.
     */
    private void getProduct(JSONObject obj){
        boolean isEatable = true;
        String ean = Vars.ean;
        String allergenText = "";
        String sourceText = "";
        String typeText = "";
        String ingredientText ="";
        HashMap<String, String> allergens = new HashMap<>();
        HashMap<String, String> sources = new HashMap<>();
        HashMap<String, String> types = new HashMap<>();
        int i = 0;
        if(!Product.list.containsKey(ean)){
            Product.addProduct(obj);
            try{
                Log.d("Product", obj.toString());
            }catch(Exception e){e.printStackTrace();}
        }
        //Building ingredient string
        for(String key : Product.list.get(ean).getIngredients().keySet()){
            boolean isEatableIngredient = true;
            String badStuff = "";
            String name = Product.list.get(ean).getIngredients().get(key).getName();;
            /**
             * Allergens
             */
            //Building list of found allergens in this product if it is something the user can't eat.
            String allergenID = Product.list.get(ean).getIngredients().get(key).getAllergenID();

            if(Diet.allAllergens.containsKey(allergenID) && !allergens.containsKey(allergenID)) {
                allergens.put(allergenID, Allergen.list.get(allergenID).getAllergen());
                badStuff += badStuff.length() > 0 ? ", " + Allergen.list.get(allergenID).getAllergen() : Allergen.list.get(allergenID).getAllergen();
                isEatableIngredient = false;
            }
            else if(!allergenID.equals("0") && Diet.allAllergens.containsKey(allergenID) && isEatableIngredient)
                //Checking if the product have an allergen that isn't the one with and ID of 0 (None).
                isEatableIngredient = true;
            else
                isEatableIngredient = true;

            //Changing the text if the item contains something the user should not eat
            if(isEatableIngredient && Diet.allAllergens.containsKey(allergenID))
                badStuff += Allergen.list.get(allergenID).getAllergen();
            //Checking if the product is marked as eatable or not
            if(!isEatableIngredient && isEatable)
                isEatable = false;

            /**
             * Sources
             */
            //Building list of found allergens in this product if it is something the user can't eat.
            String sourceID = Product.list.get(ean).getIngredients().get(key).getSourceID();
            if(Diet.allSources.containsKey(sourceID) && !sources.containsKey(sourceID)) {
                sources.put(sourceID, Source.list.get(sourceID).getSource());
                badStuff += badStuff.length() > 0 ? ", " + Source.list.get(sourceID).getSource() : Source.list.get(sourceID).getSource();
                isEatableIngredient = false;
            }
            else if(Diet.allSources.containsKey(allergenID) && isEatableIngredient)
                //Checking if the product have an allergen that isn't the one with and ID of 0 (None).
                isEatableIngredient = true;

            //Changing the text if the item contains something the user should not eat
            if(isEatableIngredient && Diet.allSources.containsKey(sourceID))
                badStuff += Source.list.get(sourceID).getSource();
            //Checking if the product is marked as eatable or not
            if(!isEatableIngredient && isEatable)
                isEatable = false;
            /**
             * Types
             */
            //Building list of found allergens in this product if it is something the user can't eat.
            String typeID = Product.list.get(ean).getIngredients().get(key).getTypeID();
            if(Diet.allTypes.containsKey(typeID) && !sources.containsKey(typeID)) {
                types.put(typeID, Type.list.get(typeID).getIngredientType());
                badStuff += badStuff.length() > 0 ? ", " + Type.list.get(typeID).getIngredientType() : Type.list.get(typeID).getIngredientType();
                isEatableIngredient = false;
            }
            else if(Diet.allTypes.containsKey(typeID) && isEatableIngredient)
                //Checking if the product have an allergen that isn't the one with and ID of 0 (None).
                isEatableIngredient = true;

            //Changing the text if the item contains something the user should not eat
            if(isEatableIngredient && Diet.allTypes.containsKey(typeID))
                badStuff += Type.list.get(typeID).getIngredientType();
            //Checking if the product is marked as eatable or not
            if(!isEatableIngredient && isEatable)
                isEatable = false;

            /**
             * Adding and formatting a string of ingredients
             */
            if(!isEatableIngredient)
                name = "<font color='#f15348'>" + name + "("+ badStuff +")</font>";
            if(i == 0){
                //Adding the first ingredient
                ingredientText += name.toUpperCase().subSequence(0,1)
                        + name.substring(1);
            }else if(i == Product.list.get(ean).getIngredients().size()-1){
                //Adding the last
                ingredientText += " og " + name + ".";
            }else{
                ingredientText += ", " + name;
            }
            i++;

        }
        //Building allergen string
        if(allergens.size() != 0){
            allergenText += "<strong>"+getString(R.string.allergens) + "</strong>: ";
            int c = allergens.size();
            for(String a : allergens.keySet()){
                if(allergens.size() > 0){
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
        //Building source string
        if(sources.size() != 0){
            allergenText += "<br />" + "<strong>" + getString(R.string.sources) + "</strong>: ";
            int c = sources.size();
            for(String a : sources.keySet()){
                if(sources.size() > 0){
                    if(c == sources.size()){
                        allergenText += sources.get(a).toUpperCase().substring(0,1) + sources.get(a).substring(1) + (c == 1 ? ".":"");
                    }else if(c == 1){
                        allergenText += " og " + sources.get(a) + ".";
                    }else {
                        allergenText += ", " + sources.get(a);
                    }
                    c--;
                }
            }
            sources.clear();
        }
        //Building types string
        if(types.size() != 0){
            allergenText += "<br />" + "<strong>" + getString(R.string.types) + "</strong>: ";
            int c = types.size();
            Log.d("d", "types -> " + types.size());
            for(String a : types.keySet()){
                if(types.size() > 0){
                    if(c == types.size()){
                        allergenText += types.get(a).toUpperCase().substring(0,1) + types.get(a).substring(1) + (c == 1 ? ".":"");
                    }else if(c == 1){
                        allergenText += " og " + types.get(a) + ".";
                    }else {
                        allergenText += ", " + types.get(a);
                    }
                    c--;
                }
            }
            sources.clear();
        }
        //Setting text label & actionbar content
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(Product.list.get(ean).getName());

        producer.setText(
                //Setting the producer name to always begin with an uppercase letter
                Html.fromHtml(
                        "<strong>" + getString(R.string.produced_by) + "</strong>: " +
                        Producer.list.get(Product.list.get(ean).getProducerID()).getName().toUpperCase().substring(0,1) +
                        Producer.list.get(Product.list.get(ean).getProducerID()).getName().substring(1)));
        lastUpdated.setText(Html.fromHtml("<strong>" + getString(R.string.last_updated) + "</strong>: "+ Product.list.get(ean).getLastUpdated()));
        if(allergenText.length()>0)
            productAllergens.setText(Html.fromHtml(allergenText));
        productIngredients.setText(Html.fromHtml(ingredientText));

        //Changing eatable img
        if(isEatable)
            eatableIcon.setImageResource(R.drawable.iseatable);
        else
            eatableIcon.setImageResource(R.drawable.uneatable);
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
                    String output;
                    while((output = in.readLine()) != null)
                        obj= new JSONObject(output);
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
            }else if(Vars.ean.length() <= 0){
                //Opening a dialog box if product don't exsist in DB
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.choose_a_product_first_title)//R.string.product_does_not_exist_title)
                        .setMessage(R.string.choose_a_product_first)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment(), Vars._SEARCH_FRAGMENT).addToBackStack(null).commit();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Logic
                            }
                        })
                        .setIcon(R.drawable.logo)
                        .show();
            }else{
                //Opening a dialog box if product don't exsist in DB
                new AlertDialog.Builder(getContext())
                        .setTitle(Vars.ean)//R.string.product_does_not_exist_title)
                        .setMessage(R.string.product_does_not_exist_msg)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                Ingredient.list.clear();
                                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddProductFragment(), Vars._ADD_PRODUCT_FRAGMENT).addToBackStack(null).commit();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Logic
                            }
                        })
                        .setIcon(R.drawable.logo)
                        .show();
            }
        }
    }
}
