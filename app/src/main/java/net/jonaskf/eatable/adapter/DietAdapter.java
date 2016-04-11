package net.jonaskf.eatable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.jonaskf.eatable.R;
import net.jonaskf.eatable.diet.Diet;
import net.jonaskf.eatable.product.Product;

/**
 * Created by jonas on 10.04.16.
 */
public class DietAdapter  extends ArrayAdapter<Diet> {
    public DietAdapter (Context context, int resource){
        super(context, resource);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        View view = convertView;
        if(view == null){
            LayoutInflater viewInflater = LayoutInflater.from(getContext());
            view = viewInflater.inflate(R.layout.product_list_row, null);
        }

        TextView nameTW = (TextView) view.findViewById(R.id.product_row_name);
        TextView companyTW = (TextView) view.findViewById(R.id.company_row_name);

        Diet diet = getItem(pos);
        if(nameTW != null)
            nameTW.setText(diet.getDiet());
        //if(companyTW != null)
        //companyTW.setText(product.getCompany);
        //Log.d("test", product.getName());
        return view;
    }
}
