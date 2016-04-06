package net.jonaskf.eatable.product;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by jonas on 22.02.2016.
 */
public class Ingredient {
    private String name;
    private String comment;
    private String sourceID;
    private String typeID;
    private String allergenID;



    public Ingredient(String name, String comment, String sourceID, String typeID, String allergenID) {
        this.name = name;
        this.comment = comment;
        this.sourceID = sourceID;
        this.typeID = typeID;
        this.allergenID = allergenID;
    }

    public String getAllergenID() {
        return allergenID;
    }
    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getSourceID() {
        return sourceID;
    }

    public String getTypeID() {
        return typeID;
    }

    public static HashMap<String, Ingredient> addIngredients(JSONObject product){
        HashMap<String, Ingredient> ingredients = new HashMap<>();
        try{
            for(int i = 0; i < product.getJSONArray("ingredients").length(); i++){
                JSONObject obj = ((JSONObject)product.getJSONArray("ingredients").get(i));
                ingredients.put(
                        obj.getString("ingredientID"),
                        new Ingredient(
                                obj.getString("name"),
                                obj.getString("comment"),
                                obj.getString("sourceID"),
                                obj.getString("typeID"),
                                obj.getString("allergenid")
                        )
                );
                Log.d("Ingredient: ", obj.toString());
            }
        }catch (JSONException e){e.printStackTrace();}
        return ingredients;
    }
}
