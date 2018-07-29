package Data;

import com.example.hritik.weather.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherHttpClient {

    public String getWeatherData(String place){


        HttpURLConnection httpURLConnection=null;
        InputStream inputStream=null;
        try {
            httpURLConnection=(HttpURLConnection)(new URL(Utils.BASE_URL_LEFT+place+Utils.BASE_URL_RIGHT)).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();

            ///Read the response

            StringBuffer stringBuffer=new StringBuffer();
            inputStream=httpURLConnection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));

            String line=null;

            while ((line=bufferedReader.readLine()) !=null){

                stringBuffer.append(line +"\r\n");

            }
            inputStream.close();
            httpURLConnection.disconnect();

            return stringBuffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }



}
