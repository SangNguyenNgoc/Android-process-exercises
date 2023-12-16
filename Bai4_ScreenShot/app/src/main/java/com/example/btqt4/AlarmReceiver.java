package com.example.btqt4;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String ACTION_CAPTURE_SELFIE = "com.example.btqt4.CAPTURE_SELFIE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(ACTION_CAPTURE_SELFIE)) {
            // Chụp ảnh Selfie
//            captureSelfie(context);

            // Hiển thị thông báo
            showNotification(context, "Thông báo", "Đến giờ chụp ảnh!");
        }
    }

//    private void captureSelfie(Context context) {
//        // Bạn có thể bắt đầu MainActivity ở đây sử dụng Intent
//        Intent mainActivityIntent = new Intent(context, MainActivity.class);
//        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(mainActivityIntent);
//    }

    private void showNotification(Context context, String title, String content) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Check if the device is running Android 8.0 (API level 26) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Tạo âm thanh thông báo
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Lấy đường dẫn tới âm thanh từ tài nguyên raw
        Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.thongbao);

        Notification.Builder builder = new Notification.Builder(context, "channel_id")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(Notification.PRIORITY_DEFAULT);
//                .setSound(defaultSoundUri);
        builder.setSound(soundUri);
        Notification notification;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = builder.build();
        } else {
            //noinspection deprecation
            notification = builder.getNotification();
        }
        MediaPlayer mediaPlayer = MediaPlayer.create(context, soundUri);
        mediaPlayer.start();

        notificationManager.notify(1, notification);
    }
}
