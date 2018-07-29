package com.example.hritik.weather;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

import Data.CityPreferences;
import Data.JSONWeatherParser;
import Data.WeatherHttpClient;
import model.Weather;

public class MainActivity extends AppCompatActivity {

    private ImageView iconView;
    private TextView cityName,temp,description,humidity,pressure,wind,sunrise,sunset,updated;
    Weather weather=new Weather();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();

        CityPreferences cityPreferences=new CityPreferences(MainActivity.this);
        renderWeatherData(cityPreferences.getCity());
       // renderWeatherData("Mohali");
    }




    public void renderWeatherData(String city){
        WeatherTask weatherTask=new WeatherTask();
        weatherTask.execute(new String[]{city});


    }

    public class DownloadImageAsyncTask extends AsyncTask<String ,Void,Bitmap>{

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            iconView.setImageBitmap(bitmap);


        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            return downloadImage(strings[0]);
        }

        private Bitmap downloadImage(String code){
            final DefaultHttpClient client=new DefaultHttpClient();
            final HttpGet httpGet=new HttpGet(Utils.ICON_URL + code +".png");

            try {
                HttpResponse response=client.execute(httpGet);
                final int statusCode=response.getStatusLine().getStatusCode();

                if (statusCode!= HttpStatus.SC_OK){
                    Log.e("DownloadImage","Error--"+statusCode);
                    return null;

                }
                final HttpEntity entity=response.getEntity();
                if (entity!=null){
                    InputStream inputStream=null;
                    inputStream=entity.getContent();
                    // decode contents from the stream
                     final Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                     return bitmap;


                }



            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }
    }

    private class WeatherTask extends AsyncTask<String,Void, Weather>{

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            DateFormat dateFormat=DateFormat.getTimeInstance();
            String sunRise=dateFormat.format(new Date(  weather.place.getSunrise()));
            String sunsetTime=dateFormat.format(new Date(weather.place.getSunset()));
            String updateDate=dateFormat.format(new Date(weather.place.getLastUpdate()));

            DecimalFormat decimalFormat=new DecimalFormat("#.#");
            String tempFormat=decimalFormat.format(weather.currentCondition.getTemperature());


            cityName.setText(weather.place.getCity()+"," + weather.place.getCountry());
            temp.setText(""+tempFormat+"°C");
            humidity.setText("Humidity: " +weather.currentCondition.getHumidity()+"%");
            pressure.setText("Pressure: " +weather.currentCondition.getPressure()+"hPa");
            wind.setText("Wind: "+weather.wind.getSpeed()+"mps");
            sunrise.setText("Sunrise: "+sunRise);
            sunset.setText("Sunset: "+sunsetTime);
            updated.setText("Last Updated: "+updateDate);
            description.setText("Condition: "+weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescription() + ")");



        }

        @Override
        protected Weather doInBackground(String... strings) {

            String data=( (new WeatherHttpClient()).getWeatherData(strings[0]));
            weather.iconData=weather.currentCondition.getIcon();
            weather=JSONWeatherParser.getWeather(data);
            Log.v("Data :",weather.currentCondition.getDescription());

            new DownloadImageAsyncTask().execute(weather.iconData);

            return weather;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if (id==R.id.search_Id){

           // Toast.makeText(MainActivity.this,"Change City",Toast.LENGTH_SHORT).show();
            showInputDialog();
        }


        return super.onOptionsItemSelected(item);
    }


    private void setupUI() {
        iconView=(ImageView)findViewById(R.id.thumbNailIcon);
        cityName=(TextView)findViewById(R.id.cityText);
        temp=(TextView)findViewById(R.id.tempText);
        description=(TextView)findViewById(R.id.cloudText);
        humidity=(TextView)findViewById(R.id.humidityText);
        pressure=(TextView)findViewById(R.id.pressureText);
        wind=(TextView)findViewById(R.id.windText);
        sunrise=(TextView)findViewById(R.id.riseText);
        sunset=(TextView)findViewById(R.id.setText);
        updated=(TextView)findViewById(R.id.updateText);

    }
    private void showInputDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Change City");
        final EditText cityInput =new EditText(MainActivity.this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setHint("eg:- London");
        builder.setView(cityInput);builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            CityPreferences cityPreferences=new CityPreferences(MainActivity.this);
            cityPreferences.setCity(cityInput.getText().toString());
            String newCity=cityPreferences.getCity();

            renderWeatherData(newCity);



            }
        });

        builder.show();


    }

}
