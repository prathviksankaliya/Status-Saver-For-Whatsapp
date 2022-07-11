package com.itcraftsolution.statussaverforwhatsappdownload.Utils;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.itcraftsolution.statussaverforwhatsappdownload.Models.Statues;
import com.itcraftsolution.statussaverforwhatsappdownload.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utils {

    private static final String CHANNEL_NAME = "IT_CRAFT_SOLUTION";
    private static String APP_DIR = Environment.getExternalStorageDirectory().getPath() +
    File.separator + "StatusSaverForWhatsapp";


   public static File STATUS_DIRECTORY = new File(Environment.getExternalStorageDirectory() +
            File.separator + "WhatsApp/Media/.Statuses");

    public static File STATUS_DIRECTORY_NEW = new File(Environment.getExternalStorageDirectory() +
            File.separator + "Android/media/com.whatsapp/WhatsApp/Media/.Statuses");

    public static File STATUS_DIRECTORY_GBWHATSAPP = new File(Environment.getExternalStorageDirectory() +
            File.separator + "/GBWhatsapp/Media/.statuses");

    public static File RootDirectorywhatsapp =
            new File(Environment.getExternalStorageDirectory()+ "/StatusSaverForWhatsapp");

    public static void createFileFolder()
    {
        if(!RootDirectorywhatsapp.exists())
        {
            RootDirectorywhatsapp.mkdirs();
        }
    }


    public static void copyFile(Statues status, Context context) {

        File file = new File(APP_DIR);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Toast.makeText(context, "Somthing went wrong !!", Toast.LENGTH_SHORT).show();
            }
        }

        String fileName;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());

        if (status.getFilename().getName().endsWith(".mp4")) {
            fileName = "VID_" + currentDateTime + ".mp4";
        } else {
            fileName = "IMG_" + currentDateTime + ".jpg";
        }

        File destFile = new File(file + File.separator + fileName);

        try {
            org.apache.commons.io.FileUtils.copyFile(status.getFilename(), destFile);
            destFile.setLastModified(System.currentTimeMillis());
            new SingleMediaScanner(context, file);
            showNotification(context, destFile, status);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void showNotification(Context context,  File destFile, Statues status) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel(context);
        }

        Uri data = FileProvider.getUriForFile(context, "com.itcraftsolution.statussaverforwhatsappdownload" + ".provider", new File(destFile.getAbsolutePath()));
        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (status.getFilename().getName().endsWith(".mp4")) {
            intent.setDataAndType(data, "video/*");
        } else {
            intent.setDataAndType(data, "image/*");
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(context, CHANNEL_NAME);

        notification.setSmallIcon(R.drawable.logo128)
                .setContentTitle(destFile.getName())
                .setContentText("File Saved to" + APP_DIR)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(new Random().nextInt(), notification.build());

        Toast.makeText( context,"Saved to Gallery", Toast.LENGTH_LONG).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void makeNotificationChannel(Context context) {

        NotificationChannel channel = new NotificationChannel(CHANNEL_NAME, "Saved", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setShowBadge(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }


}
