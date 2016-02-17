package net.jonaskf.eatable;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import org.json.JSONObject;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Fragment tags
    private final String _search_fragment = "search fragment";
    private final String _scan_fragment = "scan fragment";
    private final String _result_fragment = "result fragment";

    public static String ean = "N/A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String json = "";
        JSONObject obj = new JSONObject();

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
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ResultFragment(), _result_fragment).addToBackStack(null).commit();
            }
        }
    }
}
