package edu.csulb.android.medicare.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import edu.csulb.android.medicare.R;

public class MedicineNameFragment extends Fragment {
    private static final String[] MEDICINES = new String[] {
            "Aspirin", "Crocin", "Saridon", "Zintac", "Benedryl"
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicine_name, container, false);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, MEDICINES);
        AutoCompleteTextView cardview_name_text = (AutoCompleteTextView) view.findViewById(R.id.cardview_name_text);
        cardview_name_text.setAdapter(adapter);
        return view;
    }

    public void setText(String item) {

    }
}
