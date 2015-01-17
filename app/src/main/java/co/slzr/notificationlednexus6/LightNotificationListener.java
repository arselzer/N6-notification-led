package co.slzr.notificationlednexus6;

import android.app.KeyguardManager;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by as on 08/01/15.
 */
public class LightNotificationListener extends NotificationListenerService {
    String TAG = getClass().getSimpleName();

    SharedPreferences prefs;

    KeyguardManager keyguardManager;
    PowerManager powerManager;

    PowerManager.WakeLock lockscreenWakelock;

    LightController lightController;

    Thread lightThread;

    int ledOff = 600;
    int ledOn = 400;
    int flashes = 0; // counter
    int lockscreenWlTimeout = 6;
    boolean maxFlashEnabled = false;
    int maxFlashes = 6;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        prefs = getSharedPreferences("settings", 0);

        ledOff = prefs.getInt("led_off", ledOff);
        ledOn = prefs.getInt("led_on", ledOn);

        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

        lockscreenWakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "N6-LED-Wl");

        ShutdownReceiver shutdownReceiver = new ShutdownReceiver();
        registerReceiver(shutdownReceiver, new IntentFilter(Intent.ACTION_SHUTDOWN));

        lightController = new LightController();
    }

    public class ShutdownReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            lightController.setBrightness(LightController.LED_BLUE, 0);
            Log.d("", "shutdwon");
        }
    }

    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        Log.d(TAG, "notification posted");

        boolean isInLockscreen = keyguardManager.inKeyguardRestrictedInputMode();

        if (isInLockscreen) {
            lockscreenWakelock.acquire();
        }

        /* If the notification can't be cleared á la Android Wear or USB debugging,
            there shouldn't be a light signal.
        */
        if (sbn.isClearable() && lightThread == null) {
            startLightThread(); // only start the thread once!
        }
    }

    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.d(TAG, "notification removed");

        lightController.setBrightness(LightController.LED_BLUE, 0);
        lightThread.interrupt();
        lightThread = null;

        // So the phone doesn't stay awake while in the pocket if someone doesn't look at it
        if (lockscreenWakelock.isHeld()) {
            lockscreenWakelock.release();
        }
    }

    void startLightThread() {
        flashes = 0;
        lightThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.currentThread().isInterrupted()) {

                        Log.d("", "flashes: " + flashes);
                        if (maxFlashEnabled && flashes >= maxFlashes) {
                            Thread.currentThread().interrupt();
                            break;
                        }

                        if (flashes >= lockscreenWlTimeout) {
                            if (lockscreenWakelock.isHeld()) {
                                lockscreenWakelock.release();
                            }
                        }

                        Thread.sleep(ledOff);

                        lightController.setBrightness(LightController.LED_BLUE, 255);
                        Thread.sleep(ledOn);
                        lightController.setBrightness(LightController.LED_GREEN, 255);
                        Thread.sleep(ledOn);
                        lightController.setBrightness(LightController.LED_BLUE, 0);
                        lightController.setBrightness(LightController.LED_GREEN, 0);

                        flashes++;
                    }
                } catch (InterruptedException e) {
                    // thread.interrupt() called because the notification was removed.
                    lightController.setBrightness(LightController.LED_BLUE, 0);
                    lightController.setBrightness(LightController.LED_GREEN, 0);
                    Thread.currentThread().interrupt();
                }
            }
        });

        lightThread.start();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        lightController.setBrightness(LightController.LED_BLUE, 0);
        lightController.setBrightness(LightController.LED_GREEN, 0);
        super.onDestroy();
    }
}
