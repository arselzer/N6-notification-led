package co.slzr.notificationlednexus6;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


public class MainActivity extends ActionBarActivity {

    Button enableButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("settings", 0);

        if (!prefs.getBoolean("appOpenedFirstTime", false)) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

            dialogBuilder.setMessage(R.string.settings_dialog_info);
            dialogBuilder.setPositiveButton(R.string.settings_dialog_enable, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                }
            });
            dialogBuilder.setNegativeButton(R.string.settings_dialog_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            AlertDialog dialog = dialogBuilder.create();

            dialog.show();
        }

        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("appOpenedFirstTime", true);
        editor.commit();

        final LightController lc = new LightController();

        Button enableButton = (Button) findViewById(R.id.enable_settings);

        ToggleButton redToggle = (ToggleButton) findViewById(R.id.red_toggle);
        redToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int brightness = 0;
                if (isChecked)
                    brightness = 255;

                lc.setBrightness(LightController.LED_RED, brightness);
            }
        });

        ToggleButton greenToggle = (ToggleButton) findViewById(R.id.green_toggle);
        greenToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int brightness = 0;
                if (isChecked)
                    brightness = 255;

                lc.setBrightness(LightController.LED_GREEN, brightness);
            }
        });

        ToggleButton blueToggle = (ToggleButton) findViewById(R.id.blue_toggle);
        blueToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int brightness = 0;
                if (isChecked)
                    brightness = 255;

                lc.setBrightness(LightController.LED_BLUE, brightness);
            }
        });

        ToggleButton chargingToggle = (ToggleButton) findViewById(R.id.charging_toggle);
        chargingToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int brightness = 0;
                if (isChecked)
                    brightness = 255;

                lc.setBrightness(LightController.LED_CHARGING, brightness);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
