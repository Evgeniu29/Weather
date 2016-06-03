package genius.paad.com.weather;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static genius.paad.com.weather.R.id.gear;

public class MainActivity extends AppCompatActivity {
    TextView cityText;
    TextView description;
    TextView average;
    TextView max;
    TextView min;

    ImageView image;
    String weatherUrl;
    String template = "http://api.openweathermap.org/data/2.5/weather?q";
    String key = "524901&APPID=fea8931edad2b0eb4688fdf3ac9d7008";
    String imageUrl = "http://openweathermap.org/img/w/";
    ArrayList<Weather> weatherList = new ArrayList<Weather>();
    RelativeLayout gearLayout;
    ImageView gear;
    String townName;
    static Timer timer;


    private static final String TAG = "myLogs";
    final static double kelvin = 273.15;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityText = (TextView) findViewById(R.id.cityText);
        description = (TextView) findViewById(R.id.description);
        average = (TextView) findViewById(R.id.average);
        max = (TextView) findViewById(R.id.max);
        min = (TextView) findViewById(R.id.min);
        image = (ImageView) findViewById(R.id.image);

        View myLayout = findViewById(R.id.gearLayout); // root View id from that link
        gear = (ImageView) myLayout.findViewById(R.id.gear);

        gear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(myIntent);

            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        if (isOnline()) {

            timer = new Timer();

            timer.scheduleAtFixedRate(new TimerTask() {

                                          @Override
                                          public void run() {


                                              Bundle savedInstanceState = getIntent().getExtras();
                                              String userName;

                                              if (savedInstanceState != null) {
                                                  townName = savedInstanceState.getString("townName");

                                              }

                                              if (townName != null) {
                                                  weatherUrl = template + "=" + townName + "=" + key;
                                                  new ParseTask(weatherUrl).execute();
                                              } else {
                                                  weatherUrl = "http://api.openweathermap.org/data/2.5/weather?q=Dnepropetrovsk=524901&APPID=fea8931edad2b0eb4688fdf3ac9d7008";
                                                  new ParseTask(weatherUrl).execute();
                                              }
                                          }

                                      },
                    0,
                    18000000);
        } else {
            showChangeLangDialog();

        }

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://genius.paad.com.weather/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://genius.paad.com.weather/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        public String link = "";
        ArrayList<Weather> JsonFilm;

        {
            JsonFilm = null;
        }

        ParseTask(String _link) {
            link = _link;
        }

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                URL url = new URL(link);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();


            } catch (Exception e) {
                e.printStackTrace();
            }


            return resultJson;


        }


        @Override
        protected void onPostExecute(String resultJson) {
            super.onPostExecute(resultJson);
            JSONObject dataJsonObj = null;

            Weather weather = new Weather();


            try {


                // We create out JSONObject from the data
                JSONObject jObj = new JSONObject(resultJson);

                // We start extracting the info

                String townName = jObj.optString("name").toString();

                weather.setTownName(townName);


                JSONArray jArr = jObj.getJSONArray("weather");

                // We use only the first value
                JSONObject JSONWeather = jArr.getJSONObject(0);

                String weatherDescription = JSONWeather.optString("description").toString();

                weather.setWeatherDescription(weatherDescription);

                String icon = JSONWeather.optString("icon").toString();

                weather.setImage(icon);

                JSONObject mainObj = jObj.getJSONObject("main");

                String averageTemperature = mainObj.optString("temp").toString();


                weather.setAverageTemperature(averageTemperature);


                String maxTemperature = mainObj.optString("temp_max").toString();

                weather.setMaxTemperature(maxTemperature);


                String minTemperature = mainObj.optString("temp_min").toString();

                weather.setMinTemperature(minTemperature);


            } catch (JSONException e1) {
                e1.printStackTrace();
            }


            cityText.setText(weather.getTownName());

            description.setText(weather.getWeatherDescription());


            double averageTemperature = Double.parseDouble(weather.getAverageTemperature()) - kelvin;

            int roundAverageTemperature = (int) Math.round(averageTemperature);

            average.setText(roundAverageTemperature + "" + (char) 0x00B0);

            double maxTemperature = Double.parseDouble(weather.getMaxTemperature()) - kelvin;

            int roundMaxTemperature = (int) Math.round(maxTemperature);

            max.setText(roundMaxTemperature + "" + (char) 0x00B0);

            double minTemperature = Double.parseDouble(weather.getMinTemperature()) - kelvin;

            int roundMinTemperature = (int) Math.round(minTemperature);


            min.setText(roundMinTemperature + "" + (char) 0x00B0);


            Picasso.with(getApplicationContext())
                    .load(imageUrl + weather.getImage() + ".png")
                    .into(image);

            Log.i(TAG, "данные обновлены");

        }

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView edt = (TextView) dialogView.findViewById(R.id.error);

        dialogBuilder.setTitle("Error");
        dialogBuilder.setMessage("No internet connection");
        AlertDialog b = dialogBuilder.create();
        b.show();
    }


}
