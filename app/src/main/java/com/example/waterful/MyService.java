package com.example.waterful;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MyService extends Service {
private static final String TAG = MyService.class.getSimpleName();
private Thread mThread;

private int mCount = 0;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if("startForeground".equals(intent.getAction())) {
            startForegroundService();
        }
//        else if(mThread == null) {
//            mThread = new Thread("ServiceThread") {
//                @Override
//                public void run() {
//                    for(int i=0; i<1000; i++) {
//                        try {
//                            mCount++;
//                            Thread.sleep(100);
//                        } catch (InterruptedException e) {
//                            break;
//                        }
//                        Log.d("My Service", "서비스 동작 중 " + mCount);
//                    }
//                }
//            };
//            mThread.start();
//        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void  startForegroundService() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.mipmap.ic_launcher_dolphin);
        builder.setContentTitle("Foreground 서비스");
        builder.setContentText("서비스 실행중");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        startForeground(1, builder.build());
    }
}
