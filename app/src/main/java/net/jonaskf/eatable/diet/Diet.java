package net.jonaskf.eatable.diet;

import android.util.Log;

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
    private HashMap<Integer, Source> source = new HashMap<>();
    private HashMap<Integer, Type> type = new HashMap<>();
    private HashMap<Integer, Allergen> allergen = new HashMap<>();

    //Public lists
    public static HashMap<Integer, Source> allSources = new HashMap<>();
    public static HashMap<Integer, Type> allTypes = new HashMap<>();
    public static HashMap<Integer, Allergen> allAllergens = new HashMap<>();

    //A list for getting and inserting data to the list of diets
    public static HashMap<String, Diet> list = new HashMap<>();

    public Diet(String diet, HashMap<Integer, Source> source, HashMap<Integer, Type> type, HashMap<Integer, Allergen> allergen) {
        this.diet = diet;
        this.source = source;
        this.type = type;
        this.allergen = allergen;

        //Adding to all sources list
        for(Integer key : source.keySet())
            if(!allSources.containsKey(key))
                allSources.put(key, source.get(key));
        //Adding to all types
        for(Integer key : type.keySet())
            if(!allTypes.containsKey(key))
                allTypes.put(key, type.get(key));
        //Adding to all allergens
        for(Integer key : this.allergen.keySet())
            if(!allAllergens.containsKey(key))
                allAllergens.put(key, this.allergen.get(key));

        Log.d("test", "Allergens: " + allergen.size() + "/" + allAllergens.size());
    }

    public static void getAllDiets(){
        //Temp version of the method

    }


    public String getDiet() {
        return diet;
    }

    public HashMap<Integer, Source> getSource() {
        return source;
    }

    public HashMap<Integer, Type> getType() {
        return type;
    }

    public HashMap<Integer, Allergen> getAllergen() {
        return allergen;
    }
}
