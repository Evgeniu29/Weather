package genius.paad.com.weather;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ForecastActivity extends Activity {

    ArrayList<ForecastWeather> forecastWeathers = new ArrayList<ForecastWeather>(24);

    RecyclerView recyclerView;
    WeatherAdapter adapter;
    String reformattedStr;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forecastactivity);

        recyclerView = (RecyclerView) findViewById(R.id.list);


        String townName = getIntent().getExtras().get("one").toString();

            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);

            adapter = new WeatherAdapter(this, forecastWeathers);

            recyclerView.setAdapter(adapter);

            RecyclerViewMargin decoration = new RecyclerViewMargin(10, 24);
            recyclerView.addItemDecoration(decoration);

            downloadTask task = new downloadTask();

            task.execute("http://api.openweathermap.org/data/2.5/forecast?q="
                    + townName + "&units=metric&appid" +
                    "&appid=fea8931edad2b0eb4688fdf3ac9d7008");



    }

    public class downloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = 0;
                try {
                    data = reader.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();
                }

                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            try {
                if (!forecastWeathers.isEmpty()) {
                    forecastWeathers.clear();
                }

                JSONObject jsonObject = new JSONObject(result);
                JSONArray listArray = jsonObject.getJSONArray("list");

                int listArrayCount = listArray.length();
                for (int i = 0; i < 24; i++) {
                    ForecastWeather weather = new ForecastWeather();
                    JSONObject resultObject = listArray.getJSONObject(i);
                    JSONObject main = resultObject.getJSONObject("main");
                    weather.setPressure(main.getString("pressure"));
                    weather.setHumidity(main.getString("humidity"));
                    JSONObject wind = resultObject.getJSONObject("wind");
                    weather.setWind(wind.getString("speed"));

                    weather.setAverageTemperature(
                            main.getString("temp"));
                    weather.setMaxTemperature(
                            main.getString("temp_max"));
                    weather.setMinTemperature(
                            main.getString("temp_min"));

                    JSONArray weatherArray = resultObject.getJSONArray("weather");
                    JSONObject weatherObject = weatherArray.getJSONObject(0);
                    weather.setWeatherDescription(getWeatherInfo(weatherObject.getString("description")));
                    weather.setImage(weatherObject.getString("icon"));


                    String date = resultObject.getString("dt_txt");

                    String formattedDate = date.substring(0,10);



                   weather.setDate(formattedDate);

                   String time = date.substring(11,19);


                   weather.setTime(time);


                    forecastWeathers.add(weather);
                }


            } catch (JSONException e) {

                e.printStackTrace();


            }



            adapter.notifyItemRangeChanged(0, forecastWeathers.size());

        }
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

        if (description.contains("mist") && description.contains("fog") && description.contains("haze")) {

            description = getResources().getString(R.string.mist);

        }

        return description;

    }


}


