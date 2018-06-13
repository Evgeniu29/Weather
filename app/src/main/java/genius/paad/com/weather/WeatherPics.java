package genius.paad.com.weather;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;

public class WeatherPics {

    MainActivity mainActivity;

    Weather weather;


    WeatherPics(MainActivity mainActivity, Weather weather ){

        this.mainActivity = mainActivity;

        this.weather = weather;
    }

    void getWeatherPics(Weather weather) {

        Long sunriseTime = weather.getSunrise();

        Long sunsetTime = weather.getSunset();

        long javasunsetTime = sunsetTime * 1000L;

        long javasunriseTime = sunriseTime * 1000L;

        long now = System.currentTimeMillis();

        if ((Long) javasunsetTime == null && (Long) javasunriseTime == null) {
            return;
        }

        if (now > javasunriseTime && now < javasunsetTime) {
            //set your background here
            analizeWeatherDay(weather.getWeatherDescription());
        } else {
            //set your background here
            analizeWeatherNight(weather.getWeatherDescription());
        }
    }

    void analizeWeatherDay(String weatherDescription) {

        setDayColor();

        Resources res = mainActivity.getResources();

        if (weatherDescription.contains("clear")) {

            Drawable drawable = res.getDrawable(R.drawable.sun);
            setCompability(this.mainActivity.fone, drawable);


        }
        if (weatherDescription.contains("drizzle")) {


            Drawable drawable = res.getDrawable(R.drawable.rain);
            setCompability(this.mainActivity.fone, drawable);


        }

        if (weatherDescription.contains("rain")) {

            Drawable drawable = res.getDrawable(R.drawable.rain);
            setCompability(this.mainActivity.fone, drawable);

        }

        if (weatherDescription.contains("clouds")) {

            Drawable drawable = res.getDrawable(R.drawable.cloud);
            setCompability(this.mainActivity.fone, drawable);

        }

        if (weatherDescription.contains("snow")) {

            Drawable drawable = res.getDrawable(R.drawable.snow);
            setCompability(this.mainActivity.fone, drawable);

        }

        if (weatherDescription.contains("thunderstorm")) {

            Drawable drawable = res.getDrawable(R.drawable.thunderstorm);
            setCompability(this.mainActivity.fone, drawable);

        }

       if (weatherDescription.contains("mist")){
            Drawable drawable = res.getDrawable(R.drawable.mist);
            setCompability(this.mainActivity.fone, drawable);

        }

        if (weatherDescription.contains("fog")){
            Drawable drawable = res.getDrawable(R.drawable.mist);
            setCompability(this.mainActivity.fone, drawable);

        }

        if (weatherDescription.contains("haze")){
            Drawable drawable = res.getDrawable(R.drawable.mist);
            setCompability(this.mainActivity.fone, drawable);

        }




    }


    void analizeWeatherNight(String weatherDescription) {

        setNightColor();

        Resources res = this.mainActivity.getResources();

        if (weatherDescription.contains("clear")) {

            Drawable drawable = res.getDrawable(R.drawable.night_clear);
            setCompability(this.mainActivity.fone, drawable);


        }
        if (weatherDescription.contains("drizzle")) {


            Drawable drawable = res.getDrawable(R.drawable.night_rain);
            setCompability(this.mainActivity.fone, drawable);

        }

        if (weatherDescription.contains("rain")) {

            Drawable drawable = res.getDrawable(R.drawable.night_rain);
            setCompability(this.mainActivity.fone, drawable);

        }

        if (weatherDescription.contains("clouds")) {

            Drawable drawable = res.getDrawable(R.drawable.night_cloud);
            setCompability(this.mainActivity.fone, drawable);

        }

        if (weatherDescription.contains("snow")) {

            Drawable drawable = res.getDrawable(R.drawable.night_snow);
            setCompability(this.mainActivity.fone, drawable);

        }

        if (weatherDescription.contains("thunderstorm")) {

            Drawable drawable = res.getDrawable(R.drawable.night_thunderstorm);
            setCompability(this.mainActivity.fone, drawable);

        }


        if (weatherDescription.contains("mist")){
            Drawable drawable = res.getDrawable(R.drawable.mist);
            setCompability(this.mainActivity.fone, drawable);

        }

        if (weatherDescription.contains("fog")){
            Drawable drawable = res.getDrawable(R.drawable.mist);
            setCompability(this.mainActivity.fone, drawable);

        }

        if (weatherDescription.contains("haze")){
            Drawable drawable = res.getDrawable(R.drawable.mist);
            setCompability(this.mainActivity.fone, drawable);

        }



    }


    private void setDayColor() {
        this.mainActivity.cityText.setTextColor(Color.parseColor("#000000"));

        this.mainActivity.description.setTextColor(Color.parseColor("#000000"));


        this.mainActivity.average.setTextColor(Color.parseColor("#000000"));


        this.mainActivity.max.setTextColor(Color.parseColor("#000000"));


        this.mainActivity.min.setTextColor(Color.parseColor("#000000"));


        this.mainActivity.wind.setTextColor(Color.parseColor("#000000"));

        this.mainActivity.pressure.setTextColor(Color.parseColor("#000000"));

        this.mainActivity.humidity.setTextColor(Color.parseColor("#000000"));
    }

    private void setNightColor() {
        this.mainActivity.cityText.setTextColor(Color.parseColor("#FFFFFF"));

        this.mainActivity.description.setTextColor(Color.parseColor("#FFFFFF"));


        this.mainActivity.average.setTextColor(Color.parseColor("#FFFFFF"));


        this.mainActivity.max.setTextColor(Color.parseColor("#FFFFFF"));


        this.mainActivity.min.setTextColor(Color.parseColor("#FFFFFF"));


        this.mainActivity.wind.setTextColor(Color.parseColor("#FFFFFF"));

        this.mainActivity.pressure.setTextColor(Color.parseColor("#FFFFFF"));

        this.mainActivity.humidity.setTextColor(Color.parseColor("#FFFFFF"));

    }

    private void setCompability(ImageView fone, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 16) {
            fone.setBackground(drawable);
        } else {
            fone.setBackground(drawable);
        }
    }
}
