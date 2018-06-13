package genius.paad.com.weather;

import android.app.DialogFragment;
import android.app.Fragment;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.content.Context.WIFI_SERVICE;


public class MyWarningFragment extends DialogFragment {

    TextView text;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment,
                container, false);
        text = (TextView) view.findViewById(R.id.textView1);

        text.setText(getResources().getString(R.string.warning));



        return view;
    }



}

