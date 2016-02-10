package net.jonaskf.eatable;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

        View view = inflater.inflate(R.layout.fragment_result,container, false);

        Log.d("Test", MainActivity.ean);
        ((TextView) view.findViewById(R.id.textView)).setText("Ean : " + MainActivity.ean + " (Length: " + MainActivity.ean.length() + ")");
        //((TextView) inflater.inflate(R.layout.fragment_result, container, false).findViewById(R.id.textView)).setText("Random");



        // Inflate the layout for this fragment
        return view;

    }

    public void updateText(String text){
        TextView textView = (TextView) getActivity().findViewById(R.id.textView);
        textView.setText(text);
    }

}
