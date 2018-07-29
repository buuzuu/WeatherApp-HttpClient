package Data;

import android.app.Activity;
import android.content.SharedPreferences;

import java.security.PublicKey;

public class CityPreferences {
    SharedPreferences preferences;

    public CityPreferences(Activity activity) {
        preferences=activity.getPreferences(Activity.MODE_PRIVATE);

    }
    public String getCity(){

        return preferences.getString("city","Mohali");
    }

    public void setCity(String city){

        preferences.edit().putString("city",city).commit();

    }
}
