package net.jonaskf.eatable;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jonas on 17.02.2016.
 */
public class GetFromAPI {

    public static String testArrayInput = "[{\"productID\":\"4044889002737\",\"productName\":\"Choco cookie - vegan -\",\"name\":\"hasseln\\u00f8tt paste\",\"sourceID\":\"15\",\"typeID\":\"7\"},{\"productID\":\"4044889002737\",\"productName\":\"Choco cookie - vegan -\",\"name\":\"vaniljeekstrakt\",\"sourceID\":\"16\",\"typeID\":\"8\"},{\"productID\":\"4044889002737\",\"productName\":\"Choco cookie - vegan -\",\"name\":\"palmeolje (RSPO cet)\",\"sourceID\":\"17\",\"typeID\":\"8\"},{\"productID\":\"4044889002737\",\"productName\":\"Choco cookie - vegan -\",\"name\":\"sukkerroemelasse\",\"sourceID\":\"8\",\"typeID\":\"3\"},{\"productID\":\"4044889002737\",\"productName\":\"Choco cookie - vegan -\",\"name\":\"r\\u00f8rsukker\",\"sourceID\":\"8\",\"typeID\":\"3\"},{\"productID\":\"4044889002737\",\"productName\":\"Choco cookie - vegan -\",\"name\":\"kakaosm\\u00f8r\",\"sourceID\":\"11\",\"typeID\":\"4\"},{\"productID\":\"4044889002737\",\"productName\":\"Choco cookie - vegan -\",\"name\":\"kakaomasse\",\"sourceID\":\"11\",\"typeID\":\"4\"},{\"productID\":\"4044889002737\",\"productName\":\"Choco cookie - vegan -\",\"name\":\"rismelk pulver\",\"sourceID\":\"12\",\"typeID\":\"3\"},{\"productID\":\"4044889002737\",\"productName\":\"Choco cookie - vegan -\",\"name\":\"hvetemel\",\"sourceID\":\"13\",\"typeID\":\"5\"},{\"productID\":\"4044889002737\",\"productName\":\"Choco cookie - vegan -\",\"name\":\"kakopulver\",\"sourceID\":\"11\",\"typeID\":\"3\"},{\"productID\":\"4044889002737\",\"productName\":\"Choco cookie - vegan -\",\"name\":\"havsalt\",\"sourceID\":\"14\",\"typeID\":\"6\"},{\"productID\":\"5000112595543\",\"productName\":\"Coca cola\",\"name\":\"e150d\",\"sourceID\":\"8\",\"typeID\":\"1\"},{\"productID\":\"5000112595543\",\"productName\":\"Coca cola\",\"name\":\"e338\",\"sourceID\":\"9\",\"typeID\":\"1\"},{\"productID\":\"5000112595543\",\"productName\":\"Coca cola\",\"name\":\"koffein\",\"sourceID\":\"10\",\"typeID\":\"2\"}]";

    public JSONObject getResultObject (String ean){
        JSONObject obj = new JSONObject();
        DownloadFileTask download = new DownloadFileTask();
        String url = "http://frigg.hiof.no/android_v165/api.php?ean=" + ean;
        download.execute(url);
        return obj;
    }

    public static  HashMap<String, Product> getStations(String json){
        HashMap<String, Product> products = new HashMap<>();
        JSONArray jsonArray;
        try{
            jsonArray= new JSONArray(json);
            for(int i= 0; i < jsonArray.length() - 1; i++){
                JSONObject obj = new JSONObject(jsonArray.get(i).toString());
                HashMap<String, Ingredient> ingredients = new HashMap<>();

                //adding products to the product list and assembling results from API query
                if(!products.containsKey(obj.getString("productID"))){
                    ingredients.put(
                            String.valueOf(ingredients.size()),
                            new Ingredient(
                                    String.valueOf(ingredients.size()),
                                    obj.getString("name"),
                                    obj.getString("sourceID"),
                                    obj.getString("typeID")
                            )
                    );
                    products.put(
                            obj.getString("productID"),
                            new Product(
                                    obj.getString("productID"),
                                    obj.getString("productName"),
                                    (HashMap<String, Ingredient>)ingredients.clone()
                            )
                    );
                }else{
                    //Replacing the old product with a new one with the extra ingredients
                    Product product = products.get(obj.getString("productID"));
                    HashMap<String, Ingredient> _ingredients = product.getIngredients();
                    _ingredients.put(
                            String.valueOf(ingredients.size()),
                            new Ingredient(
                                    String.valueOf(ingredients.size()),
                                    obj.getString("name"),
                                    obj.getString("sourceID"),
                                    obj.getString("typeID")
                            ));
                    products.remove(obj.getString("productID"));
                    products.put(
                            product.getId(),
                            new Product(
                                    product.getId(),
                                    product.getName(),
                                    (HashMap<String, Ingredient>)_ingredients.clone()
                            )
                    );

                    //Cleaning the hashmap
                    _ingredients.clear();

                }
                ingredients.clear();
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return products;
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
                /*while(in.hasNext()){
                    String name = in.nextName();
                    if(name.equals("csmd")){
                        Log.d("Download", in.nextString());
                    }
                }*/
                in.close();
                //TODO: Save the json file as an actual file. So that we can store it and reopen it.
                Log.d("Download", "Completed! ");
                return obj;
            }catch(IOException ex){
                ex.printStackTrace();
                Log.d("Download", "Failed! :(");
            }
            return null;
        }
    }

}
