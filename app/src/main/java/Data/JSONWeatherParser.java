package Data;

import com.example.hritik.weather.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.Place;
import model.Weather;

public class JSONWeatherParser {

    public static Weather getWeather(String data){
        // create json object from data
        Weather weather=new Weather();


        try {
            JSONObject jsonObject=new JSONObject(data);
            Place place=new Place();
            JSONObject cordObj= Utils.getObject("coord",jsonObject);
            place.setLat(Utils.getFloat("lat",cordObj));
            place.setLon(Utils.getFloat("lon",cordObj));
            // get the sys object

            JSONObject sysObj=Utils.getObject("sys",jsonObject);
            place.setCountry(Utils.getString("country",sysObj));
            place.setLastUpdate(Utils.getInt("dt",jsonObject));
            place.setSunset(Utils.getInt("sunset",sysObj));
            place.setSunrise(Utils.getInt("sunrise",sysObj));
            place.setCity(Utils.getString("name",jsonObject));
            weather.place=place;

            //Get the weather
            JSONArray jsonArray=jsonObject.getJSONArray("weather");
             JSONObject weatherObj=jsonArray.getJSONObject(0);
             weather.currentCondition.setWeatherID(Utils.getInt("id",weatherObj));
             weather.currentCondition.setDescription(Utils.getString("description",weatherObj));
             weather.currentCondition.setCondition(Utils.getString("main",weatherObj));
             weather.currentCondition.setIcon(Utils.getString("icon",weatherObj));

             JSONObject  mainObj=Utils.getObject("main",jsonObject);
             weather.currentCondition.setHumidity(Utils.getInt("humidity",mainObj));
             weather.currentCondition.setTemperature(Utils.getDouble("temp",mainObj));
             weather.currentCondition.setPressure(Utils.getInt("pressure",mainObj));
             weather.currentCondition.setMinTemp(Utils.getFloat("temp_min",mainObj));
             weather.currentCondition.setMaxTemp(Utils.getFloat("temp_max",mainObj));







             JSONObject windObj=Utils.getObject("wind",jsonObject);
             weather.wind.setSpeed(Utils.getFloat("speed",windObj));
            weather.wind.setDeg(Utils.getFloat("deg",windObj));
            JSONObject cloudsObj=Utils.getObject("clouds",jsonObject);
            weather.clouds.setPrecipitation(Utils.getInt("all",cloudsObj));

            return weather;
        } catch (JSONException e) {
            e.printStackTrace();

             return null;
        }

       // return null;
    }




}
