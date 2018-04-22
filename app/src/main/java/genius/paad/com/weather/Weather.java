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


    Weather(String townName, String image, String averageTemperature, String weatherDescription, String maxTemperature, String minTemperature, String wind, String pressure, String humidity) {

        this.townName = townName;
        this.image = image;
        this.averageTemperature = averageTemperature;
        this.weatherDescription = weatherDescription;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
        this.wind = wind;
        this.pressure = pressure;
        this.humidity = humidity;

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

}