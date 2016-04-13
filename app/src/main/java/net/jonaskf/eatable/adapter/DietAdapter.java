package net.jonaskf.eatable.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import net.jonaskf.eatable.R;
import net.jonaskf.eatable.diet.Diet;

/**
 * Created by jonas on 10.04.16.
 */
public class DietAdapter  extends ArrayAdapter<Diet> {
    //private ImageButton imgBtn;

    public DietAdapter (Context context, int resource){
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

        final Diet diet = getItem(pos);



        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("test", diet.getDiet() + " - ");
                Log.d("test", v.getId()+ " - " );
                addItem(diet, imgBtn);
            }
        });

        btnIcon(diet, imgBtn);
        if(nameTW != null)
            nameTW.setText(diet.getDiet());
        //if(companyTW != null)
        //companyTW.setText(product.getCompany);
        Log.d("test", diet.getDiet()+"View running");
        return view;
    }

    public void addItem(Diet diet, ImageButton imgBtn){
        if(!Diet.list.containsKey(diet.getId())){
            Diet.list.put(diet.getId(), diet);
        }else{
            Diet.list.remove(diet.getId());
        }
        btnIcon(diet, imgBtn);
    }

    private void btnIcon(Diet diet, ImageButton imgBtn){
        if(Diet.list.containsKey(diet.getId())) {
            imgBtn.setImageResource(android.R.drawable.ic_menu_delete);
            imgBtn.setBackgroundColor(Color.RED);
        }
        else{
            imgBtn.setImageResource(android.R.drawable.ic_menu_add);
            imgBtn.setBackgroundColor(Color.GREEN);
        }
    }
}
