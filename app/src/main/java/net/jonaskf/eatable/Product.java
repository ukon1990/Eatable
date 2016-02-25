package net.jonaskf.eatable;

import java.util.HashMap;

/**
 * Created by jonas on 22.02.2016.
 */
public class Product {
    private String id;
    private String name;
    private HashMap<String, Ingredient> ingredients = new HashMap<>();

    public Product(String id, String name, HashMap<String, Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(HashMap<String, Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
