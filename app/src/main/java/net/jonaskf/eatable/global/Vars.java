package net.jonaskf.eatable.global;

import android.graphics.Color;

/**
 * Created by jonas on 06.04.16.
 */
public class Vars {
    //Ean
    public static String ean = "2";//TODO: Sett til tom

    //Fragment tags
    public static final String _SEARCH_FRAGMENT = "search fragment";
    public static final String _RESULT_FRAGMENT = "result fragment";
    public static final String _ADD_PRODUCT_FRAGMENT = "add product fragment";
    public static final String _MY_DIETS_FRAGMENT = "my diets fragment";
    public static final String _ADD_DIET_FRAGMENT = "add diet fragment";
    public static final String _ADD_INGREDIENT_FRAGMENT = "add ingredient fragment";

    //URLs
    public static final String GET_PRODUCTS ="http://frigg.hiof.no/android_v165/api/GetProducts.php?";
    public static final String GET_PRODUCERS = "http://frigg.hiof.no/android_v165/api/GetProducer.php?";
    public static final String GET_ALLERGENS ="http://frigg.hiof.no/android_v165/api/GetAllergens.php?";
    public static final String GET_SOURCES = "http://frigg.hiof.no/android_v165/api/GetSources.php?";
    public static final String GET_TYPES = "http://frigg.hiof.no/android_v165/api/GetTypes.php?";
    public static final String GET_DIETS ="http://frigg.hiof.no/android_v165/api/GetDiets.php?";
    public static final String GET_INGREDIENTS ="http://frigg.hiof.no/android_v165/api/GetIngredients.php?";
    public static final String INSERT_INTO ="http://frigg.hiof.no/android_v165/api/AddStuff.php?";

    //Query
    public static final String Q_SEARCH = "search=";
    public static final String Q_EAN = "ean=";
    public static final String Q_ID = "id=";
    public static final String Q_KEY = "key=";
    public static final String Q_INSERT = "query_sql=";

    //User prefs
    public static final String PREFS_SAVE_PATH = "myDiet.dat";
    public static final String API_KEY = "546fdgh465gfhj546gfhj453gkh342464ghk3gh4k8jf3ghjk654hj6gk";

    //Static colors
    public static final int MILD_RED = Color.rgb(248, 79, 82);
    public static final int MILD_GREEN = Color.rgb(52, 198, 65);
}
