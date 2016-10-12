package net.jonaskf.eatable.product;

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
    private String lastUpdated;
    private String producerID;

    private HashMap<String, Ingredient> ingredients = new HashMap<>();

    //product list
    public static HashMap<String, Product> list = new HashMap<>();

    public Product(String id, String name, String comment, HashMap<String, Ingredient> ingredients, String lastUpdated, String producerID) {
        this.id = id;
        this.name = name;
        this.comment = comment;
        this.ingredients = ingredients;
        this.lastUpdated = lastUpdated;
        this.producerID = producerID;
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

    public String getLastUpdated(){
        return lastUpdated;
    }

    public String getProducerID(){
        return producerID;
    }

    public HashMap<String, Ingredient> getIngredients() {
        return ingredients;
    }

    public static void addProduct(JSONObject obj){
        HashMap<String, Ingredient> ingredients = Ingredient.addIngredients(obj);
        try{
            list.put(
                    obj.getString("ean"),
                    new Product(
                        obj.getString("ean"),
                        obj.getString("productName"),
                        "",
                        ingredients,
                        obj.getString("lastUpdated"),
                        obj.getString("producerID")
                    )
            );
        }catch(JSONException e){e.printStackTrace();}
    }
}
