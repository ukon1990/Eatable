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
 * Created by jonas on 10.04.16.
 */
public class DietAdapter  extends ArrayAdapter<Diet> {
    private ImageButton clickedBtn;
    private Diet clickedDiet;

    public DietAdapter (Context context, int resource){
        super(context, resource);
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup parent){
        View view = convertView;

        if(view == null){
            LayoutInflater viewInflater = LayoutInflater.from(getContext());
            view = viewInflater.inflate(R.layout.list_row, null);
        }

        final ImageButton imgBtn = (ImageButton) view.findViewById(R.id.diet_list_row_btn);
        TextView nameTW = (TextView) view.findViewById(R.id.diet_row_name);

        final Diet diet = getItem(pos);

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(diet, imgBtn);
            }
        });

        btnIcon(diet, imgBtn);
        if(nameTW != null)
            nameTW.setText(diet.getDiet());
        return view;
    }

    /*
     * A method for adding or removing the item that is clicked
     */
    public void addItem(Diet diet, ImageButton imgBtn){
        clickedBtn = imgBtn;
        clickedDiet = diet;
        try{
            if(!Diet.list.containsKey(diet.getId())){
                DownloadDiet dl = new DownloadDiet();
                dl.execute(Vars.GET_DIETS + Vars.Q_ID + diet.getId());
            }else{
                Diet.list.remove(diet.getId());
            }
        }catch(Exception e){e.printStackTrace();}
        btnIcon();
    }

    /*
     * A method for changing the icon and color of the imagebutton when clicked
     */
    private void btnIcon(){
        if(Diet.list.containsKey(clickedDiet.getId())) {
            clickedBtn.setImageResource(android.R.drawable.ic_menu_delete);
            clickedBtn.setBackgroundColor(Vars.MILD_RED);
        }
        else{
            clickedBtn.setImageResource(android.R.drawable.ic_menu_add);
            clickedBtn.setBackgroundColor(Vars.MILD_GREEN);
        }
    }
    private void btnIcon(Diet diet, ImageButton imgBtn){
        if(Diet.list.containsKey(diet.getId())) {
            imgBtn.setImageResource(android.R.drawable.ic_menu_delete);
            imgBtn.setBackgroundColor(Vars.MILD_RED);
        }
        else{
            imgBtn.setImageResource(android.R.drawable.ic_menu_add);
            imgBtn.setBackgroundColor(Vars.MILD_GREEN);
        }
    }


    private class DownloadDiet extends AsyncTask<String, Integer, JSONArray> {
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
                } catch (Exception e) {
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
            //populating
            JSONArray jArr = result;
            Log.d("adddiet", "The array: "+result.toString());
            for(int i = 0; i < jArr.length(); i++) {
                try {
                    //Source
                    HashMap<String, Source> sources = new HashMap<>();
                    try{
                        JSONArray jSources = ((JSONObject) jArr.get(i)).getJSONArray("sources");
                        for(int s = 0; s < jSources.length(); s++){
                            sources.put(
                                    ((JSONObject)jSources.get(s)).getString("sourceID"),
                                    Source.list.get(((JSONObject)jSources.get(s)).getString("sourceID"))
                            );
                        }
                    }catch(JSONException ex){}
                    //Type
                    HashMap<String, Type> types = new HashMap<>();
                    try{
                        JSONArray jTypes = ((JSONObject) jArr.get(i)).getJSONArray("allergens");
                        for(int s = 0; s < jTypes.length(); s++){
                            types.put(
                                    ((JSONObject)jTypes.get(s)).getString("typeID"),
                                    Type.list.get(((JSONObject)jTypes.get(s)).getString("typeID"))
                            );
                        }
                    }catch (JSONException ex){}
                    //Allergen
                    HashMap<String, Allergen> allergens = new HashMap<>();
                    try{
                        JSONArray jAllergens = ((JSONObject) jArr.get(i)).getJSONArray("allergens");
                        for(int s = 0; s < jAllergens.length(); s++){
                            allergens.put(
                                    ((JSONObject)jAllergens.get(s)).getString("allergenid"),
                                    Allergen.list.get(((JSONObject)jAllergens.get(s)).getString("allergenid"))
                            );
                        }
                    }catch(JSONException ex){}

                    Diet.list.put(
                            ((JSONObject) jArr.get(i)).getString("dietID"),
                            new Diet(
                                    ((JSONObject) jArr.get(i)).getString("dietID"),
                                    ((JSONObject) jArr.get(i)).getString("diet"),
                                    sources,
                                    types,
                                    allergens
                            ));
                    //Updating the lists in the Diet class
                    Diet.updateLists();
                    btnIcon();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
