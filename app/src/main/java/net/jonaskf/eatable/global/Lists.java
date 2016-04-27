package net.jonaskf.eatable.global;

import net.jonaskf.eatable.product.Allergen;
import net.jonaskf.eatable.product.Source;
import net.jonaskf.eatable.product.Type;

import java.util.HashMap;

/**
 * Created by jonas on 06.04.16.
 */
public class Lists {
    //The list of all diets (DL'd upon init)
    public static HashMap<String, String> dietList = new HashMap<>();

    //For custom diet creation
    public static HashMap<String, Allergen> customAllergenList = new HashMap<>();
    public static HashMap<String, Source> customSourceList = new HashMap<>();
    public static HashMap<String, Type> customTypeList = new HashMap<>();
}
