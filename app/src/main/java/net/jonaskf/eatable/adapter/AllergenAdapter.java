package net.jonaskf.eatable.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import net.jonaskf.eatable.R;
import net.jonaskf.eatable.diet.Diet;
import net.jonaskf.eatable.global.Vars;
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
import java.util.HashMap;

/**
 * Created by jonas on 27.04.2016.
 */
public class AllergenAdapter  extends ArrayAdapter<Allergen> {
    private ImageButton clickedBtn;
    private Allergen clickedAllergen;


    public AllergenAdapter (Context context, int resource){
        super(context, resource);
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup parent){
        View view = convertView;


        if(view == null){
            LayoutInflater viewInflater = LayoutInflater.from(getContext());
            view = viewInflater.inflate(R.layout.diet_list_row, null);

        }

        final ImageButton imgBtn = (ImageButton) view.findViewById(R.id.diet_list_row_btn);
        TextView nameTW = (TextView) view.findViewById(R.id.diet_row_name);
        //TextView companyTW = (TextView) view.findViewById(R.id.company_row_name);

        final Allergen allergen = getItem(pos);



        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(allergen, imgBtn);
            }
        });

        btnIcon(allergen, imgBtn);
        if(nameTW != null)
            nameTW.setText(allergen.getAllergen());
        return view;
    }

    public void addItem(Allergen allergen, ImageButton imgBtn){
        clickedBtn = imgBtn;
        clickedAllergen = allergen;
        if(!Allergen.list.containsKey(allergen.getId())){
            DownloadAllergen dl = new DownloadAllergen();
            dl.execute(Vars.GET_ALLERGENS + Vars.Q_ID + allergen.getId());
        }else{
            Allergen.list.remove(allergen.getId());
        }
        btnIcon();
    }

    private void btnIcon(){
        if(Allergen.list.containsKey(clickedAllergen.getId())) {
            clickedBtn.setImageResource(android.R.drawable.ic_menu_delete);
            clickedBtn.setBackgroundColor(Vars.MILD_RED);
        }
        else{
            clickedBtn.setImageResource(android.R.drawable.ic_menu_add);
            clickedBtn.setBackgroundColor(Vars.MILD_GREEN);
        }
    }
    private void btnIcon(Allergen allergen, ImageButton imgBtn){
        if(Allergen.list.containsKey(allergen.getId())) {
            imgBtn.setImageResource(android.R.drawable.ic_menu_delete);
            imgBtn.setBackgroundColor(Vars.MILD_RED);
        }
        else{
            imgBtn.setImageResource(android.R.drawable.ic_menu_add);
            imgBtn.setBackgroundColor(Vars.MILD_GREEN);
        }
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
            Log.d("allergen","Hei -> " + Allergen.list.get("5").getAllergen());
        }
    }
}
