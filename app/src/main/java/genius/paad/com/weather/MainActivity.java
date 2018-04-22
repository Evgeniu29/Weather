package genius.paad.com.weather;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Handler handler = new Handler();


    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;
    private TextView mNameView;
    SqliteDatabase db;
    String text;


    ImageView image;
    String weatherUrl;
    String template = "http://api.openweathermap.org/data/2.5/weather?q";
    String key = "fea8931edad2b0eb4688fdf3ac9d7008";
    String imageUrl = "http://openweathermap.org/img/w/";
    ArrayList<Weather> weatherList = new ArrayList<Weather>();
    public String townName;
    static Timer timer;
    String metric = "metric&appid";
    String code;
    Integer count = 1;
    String grad;

    ArrayList<Weather> weathers = new ArrayList<Weather>();



    private static final String TAG = "myLogs";
    private int progressStatus = 0;
    String sunrise, sunset;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    PlaceAutocompleteFragment places;
    ProgressBar simpleProgressBar;
    private Spinner spinner;
    Button delete;
    private static final String[] paths = {"Celsius", "Kelvin", "Farenheit"};
    private static final String[] remove = {"Remove all weathers"};

    ArrayAdapter<String> adapter;
    TextView cityText, description, average, max, min, wind, pressure, humidity;
    private GoogleApiClient client;
    EditText offlineEditText;
    public String cityLocale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new SqliteDatabase(getApplicationContext());

        cityText = (TextView) findViewById(R.id.cityText);

        average = (TextView) findViewById(R.id.average);

        image = (ImageView) findViewById(R.id.image);

        description = (TextView) findViewById(R.id.description);

        min = (TextView) findViewById(R.id.min);

        max = (TextView) findViewById(R.id.max);

        wind = (TextView) findViewById(R.id.wind);
        pressure = (TextView) findViewById(R.id.pressure);
        humidity = (TextView) findViewById(R.id.humidity);

        offlineEditText = (EditText) findViewById((R.id.offlibeEditText));


        simpleProgressBar = (ProgressBar) findViewById(R.id.progress);

        delete = (Button) findViewById(R.id.delete);

      delete.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              db.deleteAllWeathers();
          }
      });

        spinner = (Spinner) findViewById(R.id.spinner);

        adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        client = new GoogleApiClient.Builder(this).

                addApi(AppIndex.API).

                build();

        PlaceAutocompleteFragment places = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        ((EditText) places.getView().

                findViewById(R.id.place_autocomplete_search_input)).

                setTextSize(40.0f);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();

        places.setFilter(typeFilter);
        places.setOnPlaceSelectedListener(new

                                                  PlaceSelectionListener() {
                                                      @Override
                                                      public void onPlaceSelected(Place place) {
                                                          LatLng coordinates = place.getLatLng();
                                                          Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                                                          List<Address> addresses; // Only retrieve 1 address
                                                          addresses = null;
                                                          try {
                                                              addresses = geocoder.getFromLocation(
                                                                      coordinates.latitude,
                                                                      coordinates.longitude, 1);
                                                          } catch (IOException e) {
                                                              e.printStackTrace();
                                                          }

                                                          try {
                                                              Address address = addresses.get(0);
                                                              code = address.getCountryCode();
                                                              townName = address.getLocality();


                                                          } catch (NullPointerException e) {
                                                              return;
                                                          } catch (Exception e) {
                                                              return;
                                                          }


                                                          connect();
                                                      }


                                                      @Override
                                                      public void onError(Status status) {

                                                          Toast.makeText(getApplicationContext(), status.toString(), Toast.LENGTH_SHORT).show();

                                                      }
                                                  });


        offlineEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {

                String text = mEdit.toString();

                Weather findWeather = db.findWeather(text);
                if (!findWeather.getTownName().isEmpty()) {
                    instantiateWeather(findWeather);

                }

                return;

            }


            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


    }


    public void onItemSelected(AdapterView adapter, View v, int position, long id) {



        switch (position) {
            case 0:
                metric = "metric&appid";

                connect();


                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                metric = "&appid";

                connect();


                break;
            case 2:
                metric = "imperial&appid";

                connect();

                break;

        }


    }

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


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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


            Weather weather = parseData(resultJson);

            ArrayList<Weather> list = db.listWeathers();

            db.addWeather(weather);

            instantiateWeather(weather);


        }
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }




    class MyTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            for (; count <= params[0]; count++) {
                try {
                    Thread.sleep(10);
                    publishProgress(count + 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Progress update";
        }


        @Override
        protected void onPostExecute(String result) {
            simpleProgressBar.setVisibility(View.GONE);

        }


        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            simpleProgressBar.setProgress(values[0]);
        }
    }

    public void connect() {
        if (isOnline()) {

            timer = new Timer();

            timer.scheduleAtFixedRate(new TimerTask() {

                                          @Override
                                          public void run() {


                                              Bundle savedInstanceState = getIntent().getExtras();

                                              if (savedInstanceState != null) {
                                                  townName = savedInstanceState.getString("townName");

                                              }

                                              if (townName != null) {
                                                  weatherUrl = template + "=" + townName + "&units=" + metric + "=" + key;
                                                  new ParseTask(weatherUrl).execute();

                                              } else {


                                              }
                                          }

                                      },
                    0,
                    18000000);
        } else {

        }


    }

    public void instantiateWeather(Weather weather) {

        cityText.setText(weather.getTownName());


        switch (metric) {
            case "metric&appid":

                grad = "ºC";


                break;
            case "&appid":

                grad = "ºK";

                break;
            case "imperial&appid":

                grad = "ºF";
                break;

            default:

                grad = "ºC";

                break;
        }
        description.setText(weather.getWeatherDescription());


        average.setText(Math.round(Double.valueOf(weather.getAverageTemperature())) + " " + grad);


        max.setText("max" + " " + Math.round(Double.valueOf(weather.getMaxTemperature())) + " " + grad);


        min.setText("min" + " " + Math.round(Double.valueOf(weather.getMinTemperature())) + " " + grad);


        wind.setText("Wind speed" + " " + weather.getWind() + " " + "m/s");

        pressure.setText("Pressure" + " " + weather.getPressure() + " " + "mm");

        humidity.setText("Humidity" + " " + weather.getHumidity() + " " + "%");


        Picasso.with(getApplicationContext())
                .load(imageUrl + weather.getImage() + ".png")
                .into(image);

        getWeatherPics( sunrise,  sunset, weather);



        Log.i(TAG, "данные обновлены");


        count = 0;
        simpleProgressBar.setVisibility(View.VISIBLE);
        simpleProgressBar.setProgress(0);

        new MyTask().execute(100);

    }

    public Weather parseData(String resultJson) {

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

            String pressure = mainObj.optString("pressure").toString();

            weather.setPressure(pressure);

            String humidity = mainObj.optString("humidity").toString();

            weather.setHumidity(humidity);


            JSONObject windObj = jObj.getJSONObject("wind");

            String wind = windObj.optString("speed").toString();

            weather.setWind(wind);

            JSONObject sysObj = jObj.getJSONObject("sys");


            sunrise =   sysObj.getString("sunrise");

             sunset =    sysObj.getString("sunset");


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return weather;
    }

    void getWeatherPics(String sunrise, String sunset, Weather weather){

        Double _sunrise = Double.parseDouble(sunrise.trim());
        Long sunriseTime = _sunrise.longValue();

        Double _sunset = Double.parseDouble(sunset.trim());
        Long sunsetTime = _sunset.longValue();

        long javasunsetTime = sunsetTime * 1000L;

        long javasunriseTime = sunriseTime * 1000L;

        long now = System.currentTimeMillis();

        if ((Long)javasunsetTime == null && (Long)javasunriseTime == null ) {
            return;
        }

        if(now > javasunriseTime && now < javasunsetTime)
        {
            //set your background here
            analizeWeatherDay(weather.getWeatherDescription());
        }
        else
        {
            //set your background here
            analizeWeatherNight(weather.getWeatherDescription());
        }
    }

    void analizeWeatherDay(String weatherDescription) {

        cityText.setTextColor(Color.parseColor("#000000"));

        description.setTextColor(Color.parseColor("#000000"));


        average.setTextColor(Color.parseColor("#000000"));


        max.setTextColor(Color.parseColor("#000000"));


        min.setTextColor(Color.parseColor("#000000"));


        wind.setTextColor(Color.parseColor("#000000"));

        pressure.setTextColor(Color.parseColor("#000000"));

        humidity.setTextColor(Color.parseColor("#000000"));

        ImageView fone = (ImageView) findViewById(R.id.fone);
        Resources res = getResources();

       if (weatherDescription.contains("clear") ){

            Drawable drawable = res.getDrawable(R.drawable.sun);
           if(Build.VERSION.SDK_INT >= 16){
              fone.setBackground(drawable);
           }else{
               fone.setBackgroundDrawable(drawable);
           }




        }
          if (weatherDescription.contains("drizzle")) {


              Drawable drawable = res.getDrawable(R.drawable.rain);
              if(Build.VERSION.SDK_INT >= 16){
                  fone.setBackground(drawable);
              }else{
                  fone.setBackgroundDrawable(drawable);
              }



          }

        if (weatherDescription.contains("rain")) {

            Drawable drawable = res.getDrawable(R.drawable.rain);
            if(Build.VERSION.SDK_INT >= 16){
                fone.setBackground(drawable);
            }else{
                fone.setBackgroundDrawable(drawable);
            }

        }

        if (weatherDescription.contains("clouds")) {

            Drawable drawable = res.getDrawable(R.drawable.cloud);
            if(Build.VERSION.SDK_INT >= 16){
                fone.setBackground(drawable);
            }else{
                fone.setBackgroundDrawable(drawable);
            }

        }

        if (weatherDescription.contains("snow")) {

            Drawable drawable = res.getDrawable(R.drawable.snow);
            if(Build.VERSION.SDK_INT >= 16){
                fone.setBackground(drawable);
            }else{
                fone.setBackgroundDrawable(drawable);
            }

        }




    }


    void analizeWeatherNight(String weatherDescription) {

        cityText.setTextColor(Color.parseColor("#FFFFFF"));

        description.setTextColor(Color.parseColor("#FFFFFF"));


        average.setTextColor(Color.parseColor("#FFFFFF"));


        max.setTextColor(Color.parseColor("#FFFFFF"));


        min.setTextColor(Color.parseColor("#FFFFFF"));


        wind.setTextColor(Color.parseColor("#FFFFFF"));

        pressure.setTextColor(Color.parseColor("#FFFFFF"));

        humidity.setTextColor(Color.parseColor("#FFFFFF"));

        ImageView fone = (ImageView) findViewById(R.id.fone);
        Resources res = getResources();

        if (weatherDescription.contains("clear") ){

            Drawable drawable = res.getDrawable(R.drawable.night_clear);
            if(Build.VERSION.SDK_INT >= 16){
                fone.setBackground(drawable);
            }else{
                fone.setBackgroundDrawable(drawable);
            }




        }
        if (weatherDescription.contains("drizzle")) {


            Drawable drawable = res.getDrawable(R.drawable.night_rain);
            if(Build.VERSION.SDK_INT >= 16){
                fone.setBackground(drawable);
            }else{
                fone.setBackgroundDrawable(drawable);
            }



        }

        if (weatherDescription.contains("rain")) {

            Drawable drawable = res.getDrawable(R.drawable.night_rain);
            if(Build.VERSION.SDK_INT >= 16){
                fone.setBackground(drawable);
            }else{
                fone.setBackgroundDrawable(drawable);
            }

        }

        if (weatherDescription.contains("clouds")) {

            Drawable drawable = res.getDrawable(R.drawable.night_cloud);
            if(Build.VERSION.SDK_INT >= 16){
                fone.setBackground(drawable);
            }else{
                fone.setBackgroundDrawable(drawable);
            }

        }

        if (weatherDescription.contains("snow")) {

            Drawable drawable = res.getDrawable(R.drawable.night_snow);
            if(Build.VERSION.SDK_INT >= 16){
                fone.setBackground(drawable);
            }else{
                fone.setBackgroundDrawable(drawable);
            }

        }








    }


}








