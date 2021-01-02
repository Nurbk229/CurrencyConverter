package com.nurbk.ps.finalproject.Utlis;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;

import com.nurbk.ps.finalproject.R;
import com.nurbk.ps.finalproject.ui.activity.MainActivity;

public class NotificationUtils {

    private static final String MAIN_CHANNEL_ID = "main_channel_id";

    public static void createMainNotificationChannel(Context context) {
        Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notification);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = context.getString(R.string.main_channel);
            String channelDescription = context.getString(R.string.main_channel_description);
            NotificationChannel notificationChannel =
                    new NotificationChannel(MAIN_CHANNEL_ID, channelName,
                            NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(channelDescription);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.setShowBadge(true);
            notificationChannel.setVibrationPattern(new long[]{0, 500, 700, 900, 700, 500, 0});
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            notificationChannel.setSound(soundUri, audioAttributes);
            NotificationManager notificationManager =
                    context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


    public static void showBasicNotification(Context context, String title,
                                             String body) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

        Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notification);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, MAIN_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_money_exchange);
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setTicker(title);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setVibrate(new long[]{0, 500, 700, 900, 700, 500, 0});
        builder.setSound(soundUri);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, builder.build());

    }


}
