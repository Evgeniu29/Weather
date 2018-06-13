package genius.paad.com.weather;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyOfflineFragment extends DialogFragment {
    TextView text;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment,
                container, false);
        text = (TextView) view.findViewById(R.id.textView1);

        text.setText(getResources().getString(R.string.offline));

        return view;
    }



}


