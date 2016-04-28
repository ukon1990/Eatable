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
import net.jonaskf.eatable.product.Source;

/**
 * Created by jonas on 27.04.16.
 */
public class SourceAdapter extends ArrayAdapter<Source> {
    private ImageButton clickedBtn;
    private Source clickedSource;


    public SourceAdapter (Context context, int resource){
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

        final Source source = getItem(pos);



        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(source, imgBtn);
            }
        });

        btnIcon(source, imgBtn);
        if(nameTW != null)
            nameTW.setText(source.getSource());
        return view;
    }

    public void addItem(Source source, ImageButton imgBtn){
        clickedBtn = imgBtn;
        clickedSource = source;
        if(!Lists.customSourceList.containsKey(source.getId())){
            Lists.customSourceList.put(source.getId(), source);
        }else{
            Lists.customSourceList.remove(source.getId());
        }
        btnIcon();
    }

    private void btnIcon(){
        if(Lists.customSourceList.containsKey(clickedSource.getId())) {
            clickedBtn.setImageResource(android.R.drawable.ic_menu_delete);
            clickedBtn.setBackgroundColor(Vars.MILD_RED);
        }
        else{
            clickedBtn.setImageResource(android.R.drawable.ic_menu_add);
            clickedBtn.setBackgroundColor(Vars.MILD_GREEN);
        }
    }
    private void btnIcon(Source source, ImageButton imgBtn){
        if(Lists.customSourceList.containsKey(source.getId())) {
            imgBtn.setImageResource(android.R.drawable.ic_menu_delete);
            imgBtn.setBackgroundColor(Vars.MILD_RED);
        }
        else{
            imgBtn.setImageResource(android.R.drawable.ic_menu_add);
            imgBtn.setBackgroundColor(Vars.MILD_GREEN);
        }
    }
}
