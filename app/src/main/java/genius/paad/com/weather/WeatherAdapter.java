package genius.paad.com.weather;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static genius.paad.com.weather.R.*;
import static genius.paad.com.weather.R.drawable.*;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {



    private List<ForecastWeather> items;

    String imageUrl = "http://openweathermap.org/img/w/";

    Context context;





    public WeatherAdapter(Context context, List<ForecastWeather> items) {
        this.context = context;
        this.items = items;

    }


    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(layout.item, parent, false);

        return new ViewHolder(v);

    }


    @Override public void onBindViewHolder(ViewHolder holder, int position) {

        String grad = "ºC";

        ForecastWeather weather = items.get(position);

        Picasso.with(holder.image.getContext()).load(imageUrl + weather.getImage() + ".png")
                .into(holder.image);

        holder.description.setText(weather.getWeatherDescription());


        holder.average.setText(Math.round(Double.valueOf(weather.getAverageTemperature())) + " " + context.getResources().getString(string.grad) );


        holder.max.setText(context.getResources().getString(string.max)+ " " +  Math.round(Double.valueOf(weather.getMaxTemperature())) + " " + context.getResources().getString(string.grad));


        holder.min.setText(context.getResources().getString(string.min)+ " " + Math.round(Double.valueOf(weather.getMinTemperature())) + " " + context.getResources().getString(string.grad));


        holder.wind.setText(context.getResources().getString(string.wind) + " "  + weather.getWind()+ " " + context.getResources().getString(string.ms));

        holder.pressure.setText(context.getResources().getString(string.pressure) + " " + weather.getPressure()+ " " + context.getResources().getString(string.mmHg));

        holder.humidity.setText(context.getResources().getString(string.humidity) + " " +  weather.getHumidity() + " " +  context.getResources().getString(string.percent));

        holder.date.setText(weather.getDate());

        holder.time.setText(weather.getTime());

        holder.fone.setBackground(context.getResources().getDrawable(drawable.background));




        holder.itemView.setTag(weather);


    }



    @Override public int getItemCount() {

        return items.size();

    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image, fone;

        TextView description, average, max, min, wind, pressure, humidity, date, time;


        public ViewHolder(View itemView) {

            super(itemView);

            image = (ImageView) itemView.findViewById(id.image);

            fone = (ImageView) itemView.findViewById(id.fone);

            average = (TextView) itemView.findViewById(id.average);

            image = (ImageView) itemView.findViewById(id.image);

            description = (TextView) itemView.findViewById(id.description);

            min = (TextView) itemView.findViewById(id.min);

            max = (TextView) itemView.findViewById(id.max);

            wind = (TextView) itemView.findViewById(id.wind);
            pressure = (TextView) itemView.findViewById(id.pressure);
            humidity = (TextView) itemView.findViewById(id.humidity);

            date = (TextView) itemView.findViewById(id.date);

            time = (TextView) itemView.findViewById(id.time);

        }

    }

}