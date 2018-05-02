package genius.paad.com.weather;


/**
 * Created by son on 31.05.2016.
 */
public class Weather {


    String townName = new String();


    String image = new String();


    String averageTemperature = new String();


    String weatherDescription = new String();


    String maxTemperature = new String();


    String minTemperature = new String();


    String wind = new String();


    String pressure = new String();

    String humidity = new String();

    Long sunrise;

    Long sunset;


    Weather(String townName, String image, String averageTemperature, String weatherDescription, String maxTemperature, String minTemperature, String wind, String pressure, String humidity,long sunset, long sunrise) {

        this.townName = townName;
        this.image = image;
        this.averageTemperature = averageTemperature;
        this.weatherDescription = weatherDescription;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
        this.wind = wind;
        this.pressure = pressure;
        this.humidity = humidity;
        this.sunset = sunset;
        this.sunrise = sunrise;


    }

    Weather() {
        this.townName = "";
        this.image = "";
        this.averageTemperature = "";
        this.weatherDescription = "";
        this.maxTemperature = "";
        this.minTemperature = "";
        this.wind = "";
        this.pressure = "";
        this.humidity = "";
        this.sunset = 0L;
        this.sunrise = 0L;



    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;

    }

    public String getAverageTemperature() {
        return averageTemperature;
    }

    public void setAverageTemperature(String averageTemperature) {
        this.averageTemperature = averageTemperature;

    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String numidity) {
        this.humidity = numidity;
    }

    public Long getSunrise() {
        return sunrise;
    }

    public void setSunrise(Long sunrise) {
        this.sunrise = sunrise;
    }



    public Long getSunset() {
        return sunset;
    }

    public void setSunset(Long sunset) {
        this.sunset = sunset;
    }




}