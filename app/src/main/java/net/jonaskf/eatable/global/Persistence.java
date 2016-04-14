package net.jonaskf.eatable.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import net.jonaskf.eatable.R;
import net.jonaskf.eatable.diet.Diet;
import net.jonaskf.eatable.gui.MyDietFragment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * Created by jonas on 13.04.16.
 */
public class Persistence {
    /**
     * Diet saving and loading class
     *
     * Adding temporary diet for the user for now
     * TODO: http://developer.android.com/guide/topics/data/data-storage.html
     * Key value sets: http://developer.android.com/training/basics/data-storage/shared-preferences.html

     try{
     Diet.list.put(
     "0",
     new Diet(
     "0",
     "Gluten allergi",
     new HashMap<String, Source>(),
     new HashMap<String, Type>(){{
     put("9", Type.list.get("9"));

     }},
     new HashMap<String, Allergen>(){{
     put("5", Allergen.list.get("5"));

     }}
     ));
     Diet.updateLists();
     }catch(Exception e){
     e.printStackTrace();
     }*/
    public static boolean loadUserPrefs(Context context){
        //SharedPreferences settings = getSharedPreferences(Vars.MY_DIET_PREFS, 0);
        try {
            FileInputStream fis = context.openFileInput(Vars.PREFS_SAVE_PATH);
            ObjectInputStream in = new ObjectInputStream(fis);

            Object o = null;
            while((o = in.readObject()) != null)
                if(o instanceof Diet)
                    Diet.list.put( ((Diet) o).getId(), (Diet) o );
            Diet.updateLists();
            in.close();
            fis.close();
        } catch (FileNotFoundException e) {
            //If the file does not exsist, the user probably have no registered diets.
            Log.d("test","File not found");
            return false;
        } catch (StreamCorruptedException e) {
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
        }
        return true;
    }

    public static void saveUserPrefs(Context context){
        try {
            FileOutputStream fos = context.openFileOutput(Vars.PREFS_SAVE_PATH, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            for(String key : Diet.list.keySet()){
                out.writeObject(Diet.list.get(key));
            }
            out.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
