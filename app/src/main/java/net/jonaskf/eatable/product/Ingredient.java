package net.jonaskf.eatable.product;

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

    public Ingredient(String name, String comment, String sourceID, String typeID) {
        this.name = name;
        this.comment = comment;
        this.sourceID = sourceID;
        this.typeID = typeID;
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

    public HashMap<Integer, Ingredient> addIngredients(JSONArray array){
        HashMap<Integer, Ingredient> ingredients = new HashMap<>();
        for(int i = 0; i < array.length(); i++){
            /*try{
                JSONObject obj = ((JSONObject)array[i]);
                ingredients.put(
                        1,
                        new Ingredient(

                        )
                );
            }catch (JSONException e){e.printStackTrace();}*/
        }
        return ingredients;
    }
}
