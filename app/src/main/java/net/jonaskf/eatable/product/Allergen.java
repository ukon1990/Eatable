package net.jonaskf.eatable.product;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by jonas on 25.02.2016.
 */
public class Allergen implements Serializable {
    private static final long serialVersionUID = -324168761687654685L;

    private String id;
    private String allergen;
    public static HashMap<String, Allergen> list = new HashMap<>();

    public Allergen(String id, String allergen) {
        this.id = id;
        this.allergen = allergen;
    }

    public String getId(){
        return id;
    }
    public String getAllergen() {
        return allergen;
    }


}
