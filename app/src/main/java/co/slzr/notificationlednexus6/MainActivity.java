package co.slzr.notificationlednexus6;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    Button enableButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableButton = (Button) findViewById(R.id.enable_settings);

        //startService(new Intent(this, ColorfulNotificationService.class));

        /*
        if (BuildConfig.DEBUG) {
            final NotificationManager nf = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(8 * 1000);

                            NotificationCompat.Builder Builder = new NotificationCompat.Builder(getBaseContext())
                                    .setSmallIcon(R.drawable.abc_btn_radio_to_on_mtrl_000)
                                    .setContentTitle("My notification")
                                    .setContentText("Hello World!")
                                    .setTicker("Ticker blabla");

                            nf.notify(123, Builder.build());


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
        */

        enableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            }
        });

        Button blueLight = (Button) findViewById(R.id.blue_light);
        blueLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LightController().setBrightness(LightController.LED_BLUE, 0);
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
