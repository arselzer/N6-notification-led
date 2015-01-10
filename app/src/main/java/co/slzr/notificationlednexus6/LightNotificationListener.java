package co.slzr.notificationlednexus6;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by as on 08/01/15.
 */
public class LightNotificationListener extends NotificationListenerService {
    String TAG = getClass().getSimpleName();

    LightController lightController;

    Thread lightThread;

    int delay = 600;
    int smallDelay = 2;

    boolean lightOn = false;

    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d(TAG, "notification posted");
        lightOn = true;

        startOrStopLight();
    }

    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.d(TAG, "notification removed");

        lightOn = false;

        startOrStopLight();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        lightController = new LightController();
    }

    void startOrStopLight() {
        StatusBarNotification[] notifications = getActiveNotifications();
        Log.d("", "notifications: " + notifications.length);

        if (lightOn) {
            if (lightThread == null) {
                startLightThread();
                // only start the thread once!
            }
        }
        else {
            lightController.setBrightness(LightController.LED_BLUE, 0);
            lightThread.interrupt();
            lightThread = null;
        }
    }

    void startLightThread() {
        lightThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        //Log.d("LigthThread", "one flash");
                        Thread.sleep(delay);

                        for (int i = 0; i < 512; i++) {
                            lightController.setBrightness(LightController.LED_BLUE, (i % 255));
                            Thread.sleep(smallDelay);
                        }

                        lightController.setBrightness(LightController.LED_BLUE, 0);
                    }
                } catch (InterruptedException e) {
                    stopSelf();
                }x
            }
        });

        lightThread.start();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        lightController.setBrightness(LightController.LED_BLUE, 0);
        super.onDestroy();
    }
}
