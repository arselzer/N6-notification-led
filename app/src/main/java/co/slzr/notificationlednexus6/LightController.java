package co.slzr.notificationlednexus6;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by as on 10/01/15.
 */
public class LightController {
    public static String LED_GREEN = "green";
    public static String LED_BLUE = "blue";
    public static String LED_RED = "red";
    public static String LED_CHARGING = "charging";

    Process su;
    DataOutputStream suInput;

    public LightController() {
        try {
            su = Runtime.getRuntime().exec("su");

            suInput = new DataOutputStream(su.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setBrightness(String led, int brightness) {
        //Log.d("setBrightness", brightness + "");
        String ledPath = "/sys/class/leds/" + led;
        String brightnessFile = ledPath + "/brightness";

        if (brightness < 256) {
            try {
                suInput.writeBytes("echo " + brightness + " > " + brightnessFile + "\n");
                suInput.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
