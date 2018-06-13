package genius.paad.com.weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static java.sql.Types.INTEGER;

/**
 * Created by son on 21.03.2018.
 */

public class SqliteDatabase extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "weather";
    private static final String TABLE_weathers = "weathers";

    private static final String COLUMN_ID = "id";

    private static final String COLUMN_TOWNNAME = "townName";

    private static final String COLUMN_image = "image";

    private static final String COLUMN_averageTemperature = "averageTemperature";

    private static final String COLUMN_weatherDescription = "weatherDescription";

    private static final String COLUMN_maxTemperature = "maxTemperature";

    private static final String COLUMN_minTemperature = "minTemperature";

    private static final String COLUMN_wind = "wind";

    private static final String COLUMN_pressure = "pressure";

    private static final String COLUMN_humidity = "humidity";

    private static final String COLUMN_sunset = "sunset";

    private static final String COLUMN_sunrise = "sunrise";


    public SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE    TABLE " + TABLE_weathers + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TOWNNAME + " TEXT," + COLUMN_image + " TEXT," +
                COLUMN_averageTemperature + " TEXT," +
                COLUMN_weatherDescription + " TEXT," + COLUMN_maxTemperature + " TEXT," + COLUMN_minTemperature + " TEXT," + COLUMN_wind + " TEXT," + COLUMN_pressure + " TEXT," + COLUMN_humidity + " TEXT,"   + COLUMN_sunset + " INTEGER,"+ COLUMN_sunrise + " INTEGER"


        +")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_weathers);
        onCreate(db);
    }

    public ArrayList<Weather> listWeathers() {
        String sql = "select * from " + TABLE_weathers;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Weather> weathers = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);

                String townName = cursor.getString(1);
                String image = cursor.getString(2);

                String averageTemperature = cursor.getString(3);

                String weatherDescription = cursor.getString(4);

                String maxTemperature = cursor.getString(5);

                String minTemperature = cursor.getString(6);

                String wind = cursor.getString(7);

                String pressure = cursor.getString(8);

                String humidity = cursor.getString(9);

                long sunset = cursor.getLong(10);

                long sunrise = cursor.getLong(11);


                weathers.add(new Weather(townName, image, averageTemperature, weatherDescription, maxTemperature, minTemperature, wind, pressure, humidity,  sunset,  sunrise));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return weathers;
    }

    public void addWeather(Weather weather) {

        Weather findWeather = findWeather(weather.getTownName());

        if (findWeather.getTownName().isEmpty()) {

            ContentValues values = new ContentValues();

            values.put(COLUMN_TOWNNAME, weather.getTownName());
            values.put(COLUMN_image, weather.getImage());
            values.put(COLUMN_averageTemperature, weather.getAverageTemperature());
            values.put(COLUMN_weatherDescription, weather.getWeatherDescription());
            values.put(COLUMN_maxTemperature, weather.getMaxTemperature());
            values.put(COLUMN_minTemperature, weather.getMinTemperature());
            values.put(COLUMN_wind, weather.getWind());
            values.put(COLUMN_pressure, weather.getPressure());
            values.put(COLUMN_humidity, weather.getHumidity());
            values.put(COLUMN_sunset, weather.getSunset());
            values.put(COLUMN_sunrise, weather.getSunrise());



            SQLiteDatabase db = this.getWritableDatabase();

            db.insert(TABLE_weathers, null, values);


            return;
        }


        updateWeather(weather);


    }

    public void updateWeather(Weather weather) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TOWNNAME, weather.getTownName());
        values.put(COLUMN_image, weather.getImage());
        values.put(COLUMN_averageTemperature, weather.getAverageTemperature());
        values.put(COLUMN_weatherDescription, weather.getWeatherDescription());
        values.put(COLUMN_maxTemperature, weather.getMaxTemperature());
        values.put(COLUMN_minTemperature, weather.getMinTemperature());
        values.put(COLUMN_wind, weather.getWind());
        values.put(COLUMN_pressure, weather.getPressure());
        values.put(COLUMN_humidity, weather.getHumidity());
        values.put(COLUMN_sunset, weather.getSunset());
        values.put(COLUMN_sunrise, weather.getSunrise());

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_weathers, values, COLUMN_TOWNNAME + "    = ?", new String[]{String.valueOf(weather.getTownName())});
    }

    public Weather findWeather(String townname) {

        SQLiteDatabase db = this.getWritableDatabase();


        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_weathers + " WHERE " + COLUMN_TOWNNAME + " like '" + townname + "'", null);

        Weather weather = null;

        Log.d("Count", String.valueOf(cursor.getCount()));

        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            String townName = cursor.getString(1);
            String image = cursor.getString(2);

            String averageTemperature = cursor.getString(3);

            String weatherDescription = cursor.getString(4);

            String maxTemperature = cursor.getString(5);

            String minTemperature = cursor.getString(6);

            String wind = cursor.getString(7);

            String pressure = cursor.getString(8);

            String humidity = cursor.getString(9);

            long sunset = cursor.getLong(10);

            long sunrise = cursor.getLong(11);




            weather = new Weather(townName, image, averageTemperature, weatherDescription, maxTemperature, minTemperature, wind, pressure, humidity,    sunset,  sunrise);
        } else {
            return new Weather();
        }

        cursor.close();
        return weather;
    }

    public void deleteWeather(String townName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_weathers, COLUMN_TOWNNAME + "    = ?", new String[]{String.valueOf(townName)});
    }


    public void deleteAllWeathers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_weathers);

    }
}

