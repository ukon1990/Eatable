package net.jonaskf.eatable.product;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by jonas on 25.02.2016.
 */
//Class for ingredient type
public class Type implements Serializable {
    private static final long serialVersionUID = -324168761687654687L;

    private String id;
    private String ingredientType;
    public static HashMap<String, Type> list = new HashMap<>();

    public Type(String id,String ingredientType) {
        this.id = id;
        this.ingredientType = ingredientType;
    }

    public String getId(){return id;}

    public String getIngredientType() {
        return ingredientType;
    }
}