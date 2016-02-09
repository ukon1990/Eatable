package net.jonaskf.eatable;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment {


    public ResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((TextView) inflater.inflate(R.layout.fragment_result, container, false).findViewById(R.id.textView)).setText("Ean : " + MainActivity.ean + " (Length: "+ MainActivity.ean.length() +")");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

}
