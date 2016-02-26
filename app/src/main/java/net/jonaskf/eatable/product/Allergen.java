package net.jonaskf.eatable.product;

import java.util.HashMap;

/**
 * Created by jonas on 25.02.2016.
 */
public class Allergen {
    private String allergen;
    public static HashMap<Integer, Allergen> list = new HashMap<>();

    public Allergen(String allergen) {
        this.allergen = allergen;
    }

    public String getAllergen() {
        return allergen;
    }


}
