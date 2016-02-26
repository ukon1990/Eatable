package net.jonaskf.eatable.product;

import java.util.HashMap;

/**
 * Created by jonas on 25.02.2016.
 */
//Class for ingredient type
public class Type{
    private String ingredientType;
    public static HashMap<Integer, Type> list = new HashMap<>();

    public Type(String ingredientType) {
        this.ingredientType = ingredientType;
    }

    public String getIngredientType() {
        return ingredientType;
    }
}