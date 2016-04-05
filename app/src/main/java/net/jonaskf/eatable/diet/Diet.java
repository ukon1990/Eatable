package net.jonaskf.eatable.diet;

import net.jonaskf.eatable.product.Allergen;
import net.jonaskf.eatable.product.Source;
import net.jonaskf.eatable.product.Type;

import java.util.HashMap;

/**
 * Created by jonas on 26.02.2016.
 * A class for holding diet objects.
 */
public class Diet {
    /*
     * The any element in either of the lists in the object is a item that the user
     * or anyone with the diet should not consume.
     */
    private String diet;
    private HashMap<String, Source> source = new HashMap<>();
    private HashMap<String, Type> type = new HashMap<>();
    private HashMap<String, Allergen> allergen = new HashMap<>();

    //Public lists
    public static HashMap<String, Source> allSources = new HashMap<>();
    public static HashMap<String, Type> allTypes = new HashMap<>();
    public static HashMap<String, Allergen> allAllergens = new HashMap<>();

    //A list for getting and inserting data to the list of diets
    public static HashMap<String, Diet> list = new HashMap<>();

    public Diet(String diet, HashMap<String, Source> source, HashMap<String, Type> type, HashMap<String, Allergen> allergen) {
        this.diet = diet;
        this.source = source;
        this.type = type;
        this.allergen = allergen;

        //Adding to all sources list
        for(String key : source.keySet())
            if(!allSources.containsKey(key))
                allSources.put(key, source.get(key));
        //Adding to all types
        for(String key : type.keySet())
            if(!allTypes.containsKey(key))
                allTypes.put(key, type.get(key));
        //Adding to all allergens
        for(String key : allergen.keySet())
            if(!allAllergens.containsKey(key))
                allAllergens.put(key, allergen.get(key));

    }

    public static void getAllDiets(){
        //Temp version of the method

    }


    public String getDiet() {
        return diet;
    }

    public HashMap<String, Source> getSource() {
        return source;
    }

    public HashMap<String, Type> getType() {
        return type;
    }

    public HashMap<String, Allergen> getAllergen() {
        return allergen;
    }
}
