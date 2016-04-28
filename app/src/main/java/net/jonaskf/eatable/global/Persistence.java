package net.jonaskf.eatable.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import net.jonaskf.eatable.R;
import net.jonaskf.eatable.diet.Diet;
import net.jonaskf.eatable.gui.MyDietFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamCorruptedException;

/**
 * Created by jonas on 13.04.16.
 */
public class Persistence {
    /**
     * Diet saving and loading class
     */
    public static boolean loadUserPrefs(Context context){
        //SharedPreferences settings = getSharedPreferences(Vars.MY_DIET_PREFS, 0);
        try {
            FileInputStream fis = context.openFileInput(Vars.PREFS_SAVE_PATH);
            ObjectInputStream in = new ObjectInputStream(fis);
            Log.d("test", "Size? " + String.valueOf(in.available()));
            Object o;
            while((o = in.readObject()) != null)
                if(o instanceof Diet)
                    Diet.list.put( ((Diet) o).getId(), (Diet) o );
            Diet.updateLists();
            in.close();
            fis.close();
        } catch (FileNotFoundException e) {
            //If the file does not exsist, the user probably have no registered diets.
            return false;
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void saveUserPrefs(Context context){
        try {
            //TODO: Noe feil med lagring? Blir lagret med nulls?
            FileOutputStream fos = context.openFileOutput(Vars.PREFS_SAVE_PATH, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            for(String key : Diet.list.keySet()){
                out.writeObject(Diet.list.get(key));
            }
            out.close();
            fos.close();
            Log.d("persistance", "Saved user prefs");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
