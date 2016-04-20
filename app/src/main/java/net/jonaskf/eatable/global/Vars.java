package net.jonaskf.eatable.global;

import android.graphics.Color;

/**
 * Created by jonas on 06.04.16.
 */
public class Vars {
    //Ean
    public static String ean = "";

    //Fragment tags
    public static final String _SEARCH_FRAGMENT = "search fragment";
    public static final String _SCAN_FRAGMENT = "scan fragment";
    public static final String _RESULT_FRAGMENT = "result fragment";
    public static final String _ADD_PRODUCT_FRAGMENT = "add product fragment";
    public static final String _MY_DIETS_FRAGMENT = "my diets fragment";
    public static final String _ADD_DIET_FRAGMENT = "add diet fragment";

    //URLs
    public static final String GET_PRODUCTS ="http://frigg.hiof.no/android_v165/api/GetProducts.php?";
    public static final String GET_ALLERGENS ="http://frigg.hiof.no/android_v165/api/GetAllergens.php?";
    public static final String GET_SOURCES = "http://frigg.hiof.no/android_v165/api/GetSources.php?";
    public static final String GET_TYPES = "http://frigg.hiof.no/android_v165/api/GetTypes.php?";
    public static final String GET_DIETS ="http://frigg.hiof.no/android_v165/api/GetDiets.php?";
    public static final String GET_INGREDIENTS ="http://frigg.hiof.no/android_v165/api/GetIngredients.php?";

    //Query
    public static final String Q_SEARCH = "search=";
    public static final String Q_EAN = "ean=";
    public static final String Q_ID = "id=";

    //User prefs
    public static final String PREFS_SAVE_PATH = "myDiet.dat";

    //Static colors
    public static final int MILD_RED = Color.rgb(248, 79, 82);
    public static final int MILD_GREEN = Color.rgb(52, 198, 65);
}
