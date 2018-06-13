package genius.paad.com.weather;


import android.app.Activity;

import android.app.Fragment;

import android.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;

import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.Actions;
import com.squareup.picasso.Picasso;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
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

    TextView cityText, description, average, max, min, wind, pressure, humidity;
    private GoogleApiClient client;
    EditText offlineEditText;
    public String cityLocale;
    WifiManager wm;
    ImageView fone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        db = new SqliteDatabase(getApplicationContext());

        cityText = (TextView) findViewById(R.id.cityText);

        average = (TextView) findViewById(R.id.average);

        image = (ImageView) findViewById(R.id.image);

        fone = (ImageView) findViewById(R.id.fone);

        description = (TextView) findViewById(R.id.description);

        min = (TextView) findViewById(R.id.min);

        max = (TextView) findViewById(R.id.max);

        wind = (TextView) findViewById(R.id.wind);
        pressure = (TextView) findViewById(R.id.pressure);
        humidity = (TextView) findViewById(R.id.humidity);

        offlineEditText = (EditText) findViewById((R.id.offlibeEditText));

        offlineEditText.setHint(getResources().getString(R.string.find));

        offlineEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.remove:
                                db.deleteAllWeathers();
                                return true;

                            default:
                                return false;
                        }

                    }

                });

                return true;

            }
        });

        simpleProgressBar = (ProgressBar) findViewById(R.id.progress);


        PlaceAutocompleteFragment places = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        ((EditText) places.getView().

                findViewById(R.id.place_autocomplete_search_input)).

                setTextSize(18.0f);

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
                                                              connect();


                                                          } catch (NullPointerException e) {
                                                              return;
                                                          } catch (Exception e) {
                                                              return;
                                                          }

                                                      }


                                                      @Override
                                                      public void onError(Status status) {

                                                          Toast.makeText(getApplicationContext(), status.toString(), Toast.LENGTH_SHORT).show();

                                                      }
                                                  });

        offlineEditText.addTextChangedListener(new

                                                       TextWatcher() {
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

        Button forecast = (Button)findViewById(R.id.forecast);

        forecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,  ForecastActivity.class);

                if (townName!=null) {

                    intent.putExtra("one", townName);

                    startActivity(intent);
                }
                else {

                }

            }
        });


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


            if (resultJson.isEmpty() || resultJson == null) {

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                MyWarningFragment dialogFragment = new MyWarningFragment();
                dialogFragment.show(ft, "dialog");

                return;


            } else {


                Weather weather = parseData(resultJson);

                db.addWeather(weather);

                instantiateWeather(weather);

            }


        }
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
        if (wm.isWifiEnabled()) {

            timer = new Timer();

            timer.scheduleAtFixedRate(new TimerTask() {

                                          @Override
                                          public void run() {

                                              if (townName != null) {
                                                  weatherUrl = template + "=" + townName + "&units=" + metric + "=" + key;
                                                  new ParseTask(weatherUrl).execute();

                                              }


                                          }

                                      },
                    0,
                    18000000);
        } else {

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            MyOfflineFragment dialogFragment = new MyOfflineFragment();

            dialogFragment.show(ft, "dialog");


        }


    }

    public void instantiateWeather(Weather weather) {

        cityText.setText(weather.getTownName());

        description.setText(getWeatherInfo(weather.getWeatherDescription()));


        average.setText(Math.round(Double.valueOf(weather.getAverageTemperature())) + " " + getResources().getString(R.string.grad));


        max.setText(getResources().getString(R.string.max) + " " + Math.round(Double.valueOf(weather.getMaxTemperature())) + " " + getResources().getString(R.string.grad));


        min.setText(getResources().getString(R.string.min) + " " + Math.round(Double.valueOf(weather.getMinTemperature())) + " " + getResources().getString(R.string.grad));


        wind.setText(getResources().getString(R.string.wind) + " " + weather.getWind() + " " + getResources().getString(R.string.ms));

        pressure.setText(getResources().getString(R.string.pressure) + " " + weather.getPressure() + " " + getString(R.string.mmHg));

        humidity.setText(getResources().getString(R.string.humidity) + " " + weather.getHumidity() + " " + getString(R.string.percent));


        Picasso.with(getApplicationContext())
                .load(imageUrl + weather.getImage() + ".png")
                .into(image);

        WeatherPics weatherPics = new WeatherPics(this, weather);

        weatherPics.getWeatherPics(weather);

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


            sunrise = sysObj.getString("sunrise");

            weather.setSunrise(Long.valueOf(sunrise));

            sunset = sysObj.getString("sunset");

            weather.setSunset(Long.valueOf(sunset));


        } catch (JSONException e) {
            e.printStackTrace();


        }

        return weather;
    }

    public com.google.firebase.appindexing.Action getAction() {
        return Actions.newView("Main Page", "http://host/path");
    }


    protected void onStart() {
        super.onStart();
   /* If you’re logging an action on an item that has already been added to the index,
   you don’t have to add the following update line. See
   https://firebase.google.com/docs/app-indexing/android/personal-content#update-the-index for
   adding content to the index */
        final Task<Void> update = FirebaseAppIndex.getInstance().update();
        FirebaseUserActions.getInstance().start(getAction());
    }

    @Override
    protected void onStop() {
        FirebaseUserActions.getInstance().end(getAction());
        super.onStop();
    }






    public String getWeatherInfo(String description) {

        if (description.contains("clear sky")) {

            description = getResources().getString(R.string.clear_sky);

        }


        if (description.contains("few clouds")) {

            description = getResources().getString(R.string.few_clouds);

        }

        if (description.contains("scattered clouds")) {

            description = getResources().getString(R.string.scattered_clouds);

        }

        if (description.contains("broken clouds")) {

            description = getResources().getString(R.string.broken_clouds);

        }

        if (description.contains("mist")) {

            description = getResources().getString(R.string.mist);

        }

        if (description.contains("shower rain")) {

            description = getResources().getString(R.string.shower_rain);

        }

        if (description.contains("rain")) {

            description = getResources().getString(R.string.rain);

        }

        if (description.contains("thunderstorm")) {

            description = getResources().getString(R.string.thunderstorm);

        }

        if (description.contains("snow")) {

            description = getResources().getString(R.string.snow);

        }

        if (description.contains("haze")) {

            description = getResources().getString(R.string.mist);

        }

        if (description.contains("mist")) {

            description = getResources().getString(R.string.mist);

        }

        return description;

    }

}








