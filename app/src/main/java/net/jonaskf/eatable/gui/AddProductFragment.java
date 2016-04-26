package net.jonaskf.eatable.gui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import net.jonaskf.eatable.R;
import net.jonaskf.eatable.adapter.IngredientAdapter;
import net.jonaskf.eatable.global.Vars;
import net.jonaskf.eatable.product.Ingredient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddProductFragment extends Fragment {
    private View view;
    private SearchView searchView;
    private ListView listView;
    private IngredientAdapter adapter;
    private Button addIngredient;
    private Button addProduct;
    private TextView eanCode;
    private EditText editText;

    private String productName;
    private String comment ="";

    public AddProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_product, container, false);
        //Changing actionbar title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.add_product_fragment_title);
        //Buttons
        addIngredient = (Button) view.findViewById(R.id.add_ingredient_btn);
        addProduct = (Button) view.findViewById(R.id.add_product);
        //List
        listView = (ListView) view.findViewById(R.id.ingredient_list);
        //Textview
        eanCode = (TextView) view.findViewById(R.id.product_ean);
        eanCode.setText(Vars.ean);

        //EditText
        editText = (EditText) view.findViewById(R.id.product_name_et);
        editText.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                productName = editText.getText().toString();
                Log.d("test", "name: " + productName);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                productName = editText.getText().toString();
                Log.d("test", "name: " + productName);
            }

            @Override
            public void afterTextChanged(Editable s) {
                productName = editText.getText().toString();
                Log.d("test", "name: " + productName);
            }
        });
        //Click listeners
        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddIngredientFragment(), Vars._ADD_INGREDIENT_FRAGMENT).addToBackStack(null).commit();
            }
        });
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Logic for adding ingredient to list
                DownloadIngredient dl = new DownloadIngredient();
                dl.execute("");
            }
        });

        //Populating the list with current ingredients
        showMyDiets();
        return view;
    }

    private void showMyDiets(){
        adapter = new IngredientAdapter(getActivity(), android.R.layout.simple_list_item_1);

        listView.setAdapter(adapter);
        adapter.addAll(Ingredient.list.values());
    }



    private class DownloadIngredient extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... urls) {
            URLConnection uConn;
            try{
                String reply ="";
                String date = new SimpleDateFormat("dd/MM-yy").format(Calendar.getInstance().getTime());
                String producerID = "1";//TODO -> Allow the user to select!
                Log.d("test", "Date: "+date);
                /**
                 * Adding the product to DB
                 */
                String statement = "INSERT INTO product VALUES (" + Vars.ean + ",\""+ productName +"\",\""+ comment + "\",\"" + date + "\",\"" + producerID + "\");";
                //Getting that data
                URL url = new URL(Vars.INSERT_INTO + Vars.Q_KEY + Vars.API_KEY + "&" + Vars.Q_INSERT + statement.replaceAll(" ","%20"));
                uConn = url.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(uConn.getInputStream(), "UTF-8")
                );
                Log.d("test", Vars.INSERT_INTO + Vars.Q_KEY + Vars.API_KEY + "&" + Vars.Q_INSERT + statement.replaceAll(" ","%20"));
                reply += productName + " " + in.readLine() + "\n";
                in.close();

                /**
                 * Adding the ingredients to DB
                 */
                //Looping throught each ingredient to add it into the product in the DB
                for(String key : Ingredient.list.keySet()){
                    statement = "INSERT INTO product_has_ingredient VALUES (" + Vars.ean + ","+ key +");";
                    //Getting that data
                    url = new URL(Vars.INSERT_INTO + Vars.Q_KEY + Vars.API_KEY + "&" + Vars.Q_INSERT + statement.replaceAll(" ","%20"));
                    uConn = url.openConnection();
                    in = new BufferedReader(
                            new InputStreamReader(uConn.getInputStream(), "UTF-8")
                    );
                    reply += Ingredient.list.get(key).getName() + " " + in.readLine() + "\n";
                    Log.d("test", Vars.INSERT_INTO + Vars.Q_KEY + Vars.API_KEY + "&" + Vars.Q_INSERT + statement.replaceAll(" ","%20"));
                    in.close();
                }
                return reply;
            }catch(IOException ex){
                ex.printStackTrace();
                Log.d("Download", "Failed! :(");
            }
            return null;
        }

        protected void onPostExecute(String reply){
            new AlertDialog.Builder(getContext())
                    .setTitle(Vars.ean)//R.string.product_does_not_exist_title)
                    .setMessage(reply)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            Ingredient.list.clear();
                            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment(), Vars._SEARCH_FRAGMENT).addToBackStack(null).commit();
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
    }
}
