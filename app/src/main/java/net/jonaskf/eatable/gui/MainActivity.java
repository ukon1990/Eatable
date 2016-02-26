package net.jonaskf.eatable.gui;

import android.content.Intent;
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

import net.jonaskf.eatable.R;
import net.jonaskf.eatable.product.Allergen;
import net.jonaskf.eatable.product.Source;
import net.jonaskf.eatable.product.Type;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * GetAllergens.php - http://frigg.hiof.no/android_v165/GetAllergens.php
     * GetSoruces.php - http://frigg.hiof.no/android_v165/GetSources.php
     * GetTypes.php - http://frigg.hiof.no/android_v165/GetTypes.php
     */
    //Fragment tags
    private final String _search_fragment = "search fragment";
    private final String _scan_fragment = "scan fragment";
    private final String _result_fragment = "result fragment";

    public static String ean = "5000112595543";//TODO: Husk å sett denne tilbake til N/A ellnst.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Starting the search fragment
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new ScanFragment(), _scan_fragment).commit();

        final IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        //Floating scan button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initiating scan if pressed
                intentIntegrator.initiateScan();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //TODO: HashMap<String, Product> hm = GetFromAPI.getStations(GetFromAPI.testArrayInput);
        /*
        for(String key : hm.keySet()){
            Log.d("Product", hm.get(key).getName() + " has " + hm.get(key).getIngredients().size() + " ingredients.");
        }*/

        /**
         * Adding ingredientsTypes, Allergens and product sources.
         */
        getAllAllergens();
        getAllSources();
        getAllTypes();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScanFragment(), _scan_fragment).addToBackStack(null).commit();
        } else if (id == R.id.nav_manual) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment(), _search_fragment).addToBackStack(null).commit();
        } else if (id == R.id.nav_settings) {
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment(), _search_fragment).addToBackStack(null).commit();
            Log.d("onNavigation", ean);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ResultFragment(), _result_fragment).addToBackStack(null).commit();



            //startActivityForResult(new Intent(this, SettingsActivity.class), 1);
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
                //TODO: Add feilmelding
                Log.d("Cancelled from fragment", "Cancelled from fragment");
            } else {
                //TODO: Add try catch
                ean = result.getContents();
                Log.d("onActivityResult", ean);
                try{
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ResultFragment(), _result_fragment).addToBackStack(null).commit();
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
        dl.execute("http://frigg.hiof.no/android_v165/GetAllergens.php");
    }
    public void getAllSources(){
        DownloadSources dl = new DownloadSources();
        dl.execute("http://frigg.hiof.no/android_v165/GetSources.php");
    }
    public void getAllTypes(){
        DownloadTypes dl = new DownloadTypes();
        dl.execute("http://frigg.hiof.no/android_v165/GetTypes.php");
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
            for(int i = 0; i < jArr.length(); i++)
                try {
                    Log.d("init", ((JSONObject) jArr.get(i)).getString("allergen"));
                    Allergen.list.put(
                            ((JSONObject) jArr.get(i)).getInt("allergenid"),
                            new Allergen(((JSONObject) jArr.get(i)).getString("allergen"))
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
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
                    Log.d("init", ((JSONObject) jArr.get(i)).getString("source"));
                    Source.list.put(
                            ((JSONObject) jArr.get(i)).getInt("sourceID"),
                            new Source(((JSONObject) jArr.get(i)).getString("source"))
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
            for(int i = 0; i < jArr.length(); i++)
                try {
                    Log.d("init", ((JSONObject) jArr.get(i)).getString("ingredientType"));
                    Type.list.put(
                            ((JSONObject) jArr.get(i)).getInt("typeID"),
                            new Type(((JSONObject) jArr.get(i)).getString("ingredientType"))
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }
}
