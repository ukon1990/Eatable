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
import net.jonaskf.eatable.global.Vars;
import net.jonaskf.eatable.product.Ingredient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by jonas on 10.04.16.
 */
public class IngredientAdapter extends ArrayAdapter<Ingredient> {
    private ImageButton clickedBtn;
    private Ingredient clickedIngredient;

    public IngredientAdapter (Context context, int resource){
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
        //TextView companyTW = (TextView) view.findViewById(R.id.company_row_name);

        final Ingredient ingredient = getItem(pos);
        Log.d("test", "name " + ingredient.getName() + " has id " + ingredient.getId());

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(ingredient, imgBtn);
            }
        });

        btnIcon(ingredient, imgBtn);
        if(nameTW != null)
            nameTW.setText(ingredient.getName());
        return view;
    }

    public void addItem(Ingredient ingredient, ImageButton imgBtn){
        clickedBtn = imgBtn;
        clickedIngredient = ingredient;
        if(!Ingredient.list.containsKey(ingredient.getId())){
            DownloadIngredient dl = new DownloadIngredient();
            dl.execute(Vars.GET_INGREDIENTS + Vars.Q_ID + ingredient.getId());
        }else{
            Ingredient.list.remove(ingredient.getId());
        }
        Log.d("test", "Clicked val -> " + ingredient.getId() + "(" + ingredient.getName() +") "+ clickedIngredient.getId() + "(" + clickedIngredient.getName() +")");
        btnIcon();
    }

    private void btnIcon(){
        if(Ingredient.list.containsKey(clickedIngredient.getId())) {
            clickedBtn.setImageResource(android.R.drawable.ic_menu_delete);
            clickedBtn.setBackgroundColor(Vars.MILD_RED);
        }
        else{
            clickedBtn.setImageResource(android.R.drawable.ic_menu_add);
            clickedBtn.setBackgroundColor(Vars.MILD_GREEN);
        }
    }
    private void btnIcon(Ingredient ingredient, ImageButton imgBtn){
        if(Ingredient.list.containsKey(ingredient.getId())) {
            imgBtn.setImageResource(android.R.drawable.ic_menu_delete);
            imgBtn.setBackgroundColor(Vars.MILD_RED);
        }
        else{
            imgBtn.setImageResource(android.R.drawable.ic_menu_add);
            imgBtn.setBackgroundColor(Vars.MILD_GREEN);
        }
    }


    private class DownloadIngredient extends AsyncTask<String, Integer, JSONArray> {
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
            //populating
            JSONArray jArr = result;
            Log.d("test", "Size of jarr: " + jArr.length());
            for(int i = 0; i < jArr.length(); i++) {
                try {
                    Ingredient.list.put(
                            ((JSONObject) jArr.get(i)).getString("ingredientID"),
                            new Ingredient(
                                    ((JSONObject) jArr.get(i)).getString("ingredientID"),
                                    ((JSONObject) jArr.get(i)).getString("name"),
                                    ((JSONObject) jArr.get(i)).getString("comment"),
                                    ((JSONObject) jArr.get(i)).getString("sourceID"),
                                    ((JSONObject) jArr.get(i)).getString("typeID"),
                                    ((JSONObject) jArr.get(i)).getString("allergenid")
                            ));
                    Log.d("test", "The size of the ingredients is: " + Ingredient.list.size() + ". This was " + ((JSONObject) jArr.get(i)).getString("name") + " - " + ((JSONObject) jArr.get(i)).getString("ingredientID"));
                    btnIcon();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
