package net.jonaskf.eatable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import net.jonaskf.eatable.R;
import net.jonaskf.eatable.global.Lists;
import net.jonaskf.eatable.global.Vars;
import net.jonaskf.eatable.product.Allergen;

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
            view = viewInflater.inflate(R.layout.list_row, null);

        }

        final ImageButton imgBtn = (ImageButton) view.findViewById(R.id.diet_list_row_btn);
        TextView nameTW = (TextView) view.findViewById(R.id.diet_row_name);

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
        if(!Lists.customAllergenList.containsKey(allergen.getId())){
            Lists.customAllergenList.put(allergen.getId(), allergen);
        }else{
            Lists.customAllergenList.remove(allergen.getId());
        }
        btnIcon();
    }

    private void btnIcon(){
        if(Lists.customAllergenList.containsKey(clickedAllergen.getId())) {
            clickedBtn.setImageResource(android.R.drawable.ic_menu_delete);
            clickedBtn.setBackgroundColor(Vars.MILD_RED);
        }
        else{
            clickedBtn.setImageResource(android.R.drawable.ic_menu_add);
            clickedBtn.setBackgroundColor(Vars.MILD_GREEN);
        }
    }
    private void btnIcon(Allergen allergen, ImageButton imgBtn){
        if(Lists.customAllergenList.containsKey(allergen.getId())) {
            imgBtn.setImageResource(android.R.drawable.ic_menu_delete);
            imgBtn.setBackgroundColor(Vars.MILD_RED);
        }
        else{
            imgBtn.setImageResource(android.R.drawable.ic_menu_add);
            imgBtn.setBackgroundColor(Vars.MILD_GREEN);
        }
    }
}
