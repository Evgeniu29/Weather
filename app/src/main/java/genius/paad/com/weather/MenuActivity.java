package genius.paad.com.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by son on 01.06.2016.
 */
public class MenuActivity extends AppCompatActivity {

    ArrayList<String> townList = new ArrayList<String>();

    ListView TownList;

    String townName;

    ImageView gear;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.townlist);

        townList = new ArrayList<String>();

        TownList = (ListView) findViewById(R.id.townList);

        View myLayout = findViewById(R.id.gearLayout); // root View id from that link

        gear = (ImageView) myLayout.findViewById(R.id.gear);

        gear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        getTownNames();

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, townList);
        // Set The Adapter
        TownList.setAdapter(arrayAdapter);

        // register onClickListener to handle click events on each item
        TownList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {


                if (MainActivity.timer != null) {
                    MainActivity.timer.cancel();
                    MainActivity.timer.purge();
                    MainActivity.timer = null;
                }

                townName = townList.get(position).toString();
                Intent myIntent = new Intent(MenuActivity.this, MainActivity.class);
                myIntent.putExtra("townName", townName);

                startActivity(myIntent);

            }
        });
    }

    void getTownNames() {
        townList.clear();
        townList.add("London");
        townList.add("Pekin");
        townList.add("Donetsk");


    }
}



