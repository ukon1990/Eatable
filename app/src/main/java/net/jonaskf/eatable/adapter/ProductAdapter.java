package net.jonaskf.eatable.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.jonaskf.eatable.R;
import net.jonaskf.eatable.product.Producer;
import net.jonaskf.eatable.product.Product;

/**
 * Created by jonas on 10.04.16.
 */
public class ProductAdapter extends ArrayAdapter<Product>{
    public ProductAdapter (Context context, int resource){
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

        Product product = getItem(pos);

        //Just to make sure that the company name starts with a uppercase letter
        String companyName = Producer.list.get(product.getProducerID()).getName().toUpperCase().subSequence(0,1) +
                             Producer.list.get(product.getProducerID()).getName().substring(1);
        if(nameTW != null)
            nameTW.setText(product.getName());
        if(companyTW != null)
            companyTW.setText(companyName);

        return view;
    }
}
