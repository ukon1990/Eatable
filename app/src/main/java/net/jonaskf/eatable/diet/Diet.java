package net.jonaskf.eatable.diet;

import net.jonaskf.eatable.product.Allergen;
import net.jonaskf.eatable.product.Source;
import net.jonaskf.eatable.product.Type;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by jonas on 26.02.2016.
 * A class for holding diet objects.
 */
public class Diet implements Serializable{
    private static final long serialVersionUID = -324168761687654684L;
    /*
     * The any element in either of the lists in the object is a item that the user
     * or anyone with the diet should not consume.
     */
    private String id;
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

    public Diet(String id, String diet, HashMap<String, Source> source, HashMap<String, Type> type, HashMap<String, Allergen> allergen) {
        this.id = id;
        this.diet = diet;
        this.source = source;
        this.type = type;
        this.allergen = allergen;
    }

    public static void updateLists(){
        allAllergens.clear();
        allSources.clear();
        allTypes.clear();
        for(String key : list.keySet()){
            //Allergens
            for(String allergen : list.get(key).getAllergen().keySet()){
                if(!allAllergens.containsKey(allergen))
                    allAllergens.put(allergen, list.get(key).getAllergen().get(allergen));
            }
            //Sources
            for(String source : list.get(key).getSource().keySet()){
                if(!allTypes.containsKey(source))
                    allSources.put(source, list.get(key).getSource().get(source));
            }
            //Types
            for(String type : list.get(key).getType().keySet()){
                if(!allTypes.containsKey(type))
                    allTypes.put(type, list.get(key).getType().get(type));
            }
        }
    }


    public String getId(){
        return id;
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
