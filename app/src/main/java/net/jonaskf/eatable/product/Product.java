package net.jonaskf.eatable.product;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by jonas on 22.02.2016.
 */
public class Product {
    private String id;
    private String name;
    private String comment;

    private HashMap<Integer, Ingredient> ingredients = new HashMap<>();

    //product list
    public static HashMap<String, Product> list = new HashMap<>();

    public Product(String id, String name, String comment, HashMap<Integer, Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.comment = comment;
        this.ingredients = ingredients;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getComment() {

        return comment;
    }

    public HashMap<Integer, Ingredient> getIngredients() {
        return ingredients;
    }

    public static void addProduct(JSONObject obj){
        HashMap<Integer, Ingredient> ingredients = Ingredient.addIngredients(obj);
        try{
            list.put(
                    obj.getString("ean"),
                    new Product(
                        obj.getString("ean"),
                        obj.getString("productName"),
                        "",//TODO: add in DB -> obj.getString("comment"),
                        ingredients
                    )
            );
        }catch(JSONException e){e.printStackTrace();}
    }
    public static void addProducts(JSONObject obj){

    }
}
