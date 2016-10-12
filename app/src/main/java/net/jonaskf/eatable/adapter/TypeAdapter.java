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
import net.jonaskf.eatable.product.Type;

/**
 * Created by jonas on 27.04.16.
 */
public class TypeAdapter extends ArrayAdapter<Type> {
    private ImageButton clickedBtn;
    private Type clickedType;

    public TypeAdapter (Context context, int resource){
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

        final Type type = getItem(pos);

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(type, imgBtn);
            }
        });

        btnIcon(type, imgBtn);
        if(nameTW != null)
            nameTW.setText(type.getIngredientType());
        return view;
    }

    public void addItem(Type type, ImageButton imgBtn){
        clickedBtn = imgBtn;
        clickedType = type;
        if(!Lists.customTypeList.containsKey(type.getId())){
            Lists.customTypeList.put(type.getId(), type);
        }else{
            Lists.customTypeList.remove(type.getId());
        }
        btnIcon();
    }

    private void btnIcon(){
        if(Lists.customTypeList.containsKey(clickedType.getId())) {
            clickedBtn.setImageResource(android.R.drawable.ic_menu_delete);
            clickedBtn.setBackgroundColor(Vars.MILD_RED);
        }
        else{
            clickedBtn.setImageResource(android.R.drawable.ic_menu_add);
            clickedBtn.setBackgroundColor(Vars.MILD_GREEN);
        }
    }
    private void btnIcon(Type type, ImageButton imgBtn){
        if(Lists.customTypeList.containsKey(type.getId())) {
            imgBtn.setImageResource(android.R.drawable.ic_menu_delete);
            imgBtn.setBackgroundColor(Vars.MILD_RED);
        }
        else{
            imgBtn.setImageResource(android.R.drawable.ic_menu_add);
            imgBtn.setBackgroundColor(Vars.MILD_GREEN);
        }
    }
}
