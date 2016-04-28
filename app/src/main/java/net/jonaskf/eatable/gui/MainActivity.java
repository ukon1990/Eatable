package net.jonaskf.eatable.gui;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import net.jonaskf.eatable.EatableWidget;
import net.jonaskf.eatable.R;
import net.jonaskf.eatable.adapter.ProductAdapter;
import net.jonaskf.eatable.diet.Diet;
import net.jonaskf.eatable.global.Lists;
import net.jonaskf.eatable.global.Persistence;
import net.jonaskf.eatable.global.Vars;
import net.jonaskf.eatable.product.Allergen;
import net.jonaskf.eatable.product.Producer;
import net.jonaskf.eatable.product.Source;
import net.jonaskf.eatable.product.Type;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private IntentIntegrator intentIntegrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //Starting the scan page fragment
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new SearchFragment(), Vars._SEARCH_FRAGMENT).commit();
        //Loading user prefs
        loadUserPrefs();

        intentIntegrator = new IntentIntegrator(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        /**
         * Adding ingredientsTypes, Allergens and product sources.
         */
        getAllAllergens();
        getAllSources();
        getAllTypes();

        /*
         * Checking for actions from the widget if it were used to init the mainactivity
         */
        if(EatableWidget.widget_action.equals(Vars._SCAN_PRODUCT)){
            //Starting the barcode scan
            intentIntegrator.setOrientationLocked(true).initiateScan();
        }else if(EatableWidget.widget_action.equals(Vars._SEARCH_FRAGMENT)){
            //Starting the search fragment (incase the app already is in some other fragment)
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new SearchFragment(), Vars._SEARCH_FRAGMENT).commit();
        }
    }

    public void scanProduct(View view){
        //Initiating scan if pressed
        intentIntegrator.setOrientationLocked(true).initiateScan();
    }
    public void loadUserPrefs(){
        //Loading user prefs if they exsist, if not opening the my diets window.
        if(!Persistence.loadUserPrefs(this))
            new AlertDialog.Builder(this)
                    .setTitle(R.string.new_user_title)//R.string.product_does_not_exist_title)
                    .setMessage(R.string.welcome_message)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyDietFragment(), Vars._MY_DIETS_FRAGMENT).addToBackStack(null).commit();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Logic
                        }
                    })
                    .setIcon(R.drawable.logo)
                    .show();
    }

    @Override
    protected void onStop(){
        super.onStop();
        //Saving user prefs anytime the users screen goes black or the user "moves" the app to the background
        Persistence.saveUserPrefs(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Options menu actions
        if (id == R.id.action_scan) {
            scanProduct(null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        //LayoutInflater lf = getAppCompatActivity().getLayoutInflater();

        int id = item.getItemId();

        if (id == R.id.nav_scan) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment(), Vars._SEARCH_FRAGMENT).addToBackStack(null).commit();
        } else if(id == R.id.nav_show_result){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ResultFragment(), Vars._RESULT_FRAGMENT).addToBackStack(null).commit();
        }else if (id == R.id.diet_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyDietFragment(), Vars._MY_DIETS_FRAGMENT).addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("Cancelled from fragment", "Cancelled from fragment");
            } else {
                Vars.ean = result.getContents();
                Log.d("onActivityResult", Vars.ean);
                try{
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ResultFragment(), Vars._RESULT_FRAGMENT).addToBackStack(null).commitAllowingStateLoss();
                }catch(Exception e){
                    Log.d("scan result", "failed :(", e);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Methods for downloading source, ingredient type and allergens
     */
    public void getAllAllergens(){
        DownloadAllergen dl = new DownloadAllergen();
        dl.execute(Vars.GET_ALLERGENS);
    }
    public void getAllSources(){
        DownloadSources dl = new DownloadSources();
        dl.execute(Vars.GET_SOURCES);
    }
    public void getAllTypes(){
        DownloadTypes dl = new DownloadTypes();
        dl.execute(Vars.GET_TYPES);
    }

    //Allergens
    private class DownloadAllergen extends AsyncTask<String, Integer, JSONArray> {
        @Override
        protected JSONArray doInBackground(String... urls) {
            URLConnection uConn;
            try{
                //Getting that data
                URL url = new URL(urls[0]);
                uConn = url.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(uConn.getInputStream(), "UTF-8")
                );

                JSONArray obj = new JSONArray();
                try {
                    obj= new JSONArray(in.readLine());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                in.close();
                return obj;
            }catch(IOException ex){
                ex.printStackTrace();
                Log.d("Download", "Failed! :(");
            }
            return null;
        }

        protected void onPostExecute(JSONArray result){
            Allergen.list.clear();
            //populating
            JSONArray jArr = result;
            for(int i = 0; i < jArr.length(); i++) {
                try {
                    Allergen.list.put(
                            ((JSONObject) jArr.get(i)).getString("allergenid"),
                            new Allergen(
                                    ((JSONObject) jArr.get(i)).getString("allergenid"),
                                    ((JSONObject) jArr.get(i)).getString("allergen")
                            )
                    );
                } catch (JSONException e) {
                    Log.d("allergen", "failed - " + result.toString());
                    e.printStackTrace();
                }
            }
        }
    }
    //Sources
    private class DownloadSources extends AsyncTask<String, Integer, JSONArray> {
        @Override
        protected JSONArray doInBackground(String... urls) {
            URLConnection uConn;
            try{
                //Getting that data
                URL url = new URL(urls[0]);
                uConn = url.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(uConn.getInputStream(), "UTF-8")
                );

                JSONArray obj = new JSONArray();
                try {
                    obj= new JSONArray(in.readLine());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                in.close();
                return obj;
            }catch(IOException ex){
                ex.printStackTrace();
                Log.d("Download", "Failed! :(");
            }
            return null;
        }

        protected void onPostExecute(JSONArray result){
            Source.list.clear();
            //populating
            JSONArray jArr = result;
            for(int i = 0; i < jArr.length(); i++)
                try {
                    Source.list.put(
                            ((JSONObject) jArr.get(i)).getString("sourceID"),
                            new Source(
                                    ((JSONObject) jArr.get(i)).getString("sourceID"),
                                    ((JSONObject) jArr.get(i)).getString("source"))
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }

    //Types
    private class DownloadTypes extends AsyncTask<String, Integer, JSONArray> {
        @Override
        protected JSONArray doInBackground(String... urls) {
            URLConnection uConn;
            try{
                //Getting that data
                URL url = new URL(urls[0]);
                uConn = url.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(uConn.getInputStream(), "UTF-8")
                );

                JSONArray obj = new JSONArray();
                try {
                    obj= new JSONArray(in.readLine());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                in.close();
                return obj;
            }catch(IOException ex){
                ex.printStackTrace();
                Log.d("Download", "Failed! :(");
            }
            return null;
        }

        protected void onPostExecute(JSONArray result){
            Type.list.clear();
            //populating
            JSONArray jArr = result;
            for(int i = 0; i < jArr.length(); i++){
                try {
                    Log.d("test", result.toString());
                    Type.list.put(
                            ((JSONObject) jArr.get(i)).getString("typeID"),
                            new Type(
                                    ((JSONObject) jArr.get(i)).getString("typeID"),
                                    ((JSONObject) jArr.get(i)).getString("ingredientType"))
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
