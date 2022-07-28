package com.itcraftsolution.statussaverforwhatsappdownload.Utils;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.itcraftsolution.statussaverforwhatsappdownload.Models.Statues;
import com.itcraftsolution.statussaverforwhatsappdownload.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utils {

    public static final String CHANNEL_NAME = "IT_CRAFT_SOLUTION";

    public static final String APP_DIR = Environment.DIRECTORY_PICTURES +
            File.separator + "StatusSaverForWhatsapp";

    public static File STATUS_SAVER_DIR = new File(Environment.getExternalStorageDirectory() +
            File.separator + Environment.DIRECTORY_PICTURES + File.separator + "/StatusSaverForWhatsapp/");

    public static File STATUS_DIRECTORY = new File(Environment.getExternalStorageDirectory() +
            File.separator + "/WhatsApp/Media/.Statuses");

    public static File STATUS_DIRECTORY_NEW = new File(Environment.getExternalStorageDirectory() +
            File.separator + "Android/media/com.whatsapp/WhatsApp/Media/.Statuses");

    public static File STATUS_DIRECTORY_WP_Business = new File(Environment.getExternalStorageDirectory() +
            File.separator + "Android/media/com.whatsapp.w4b/WhatsApp Business/Media/.Statuses");

    public static File STATUS_DIRECTORY_GBWHATSAPP = new File(Environment.getExternalStorageDirectory() +
            File.separator + "/GBWhatsapp/Media/.statuses");
    //
    public static File RootDirectorywhatsapp =
            new File(Environment.getExternalStorageDirectory() + "/StatusSaverForWhatsapp");


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void copyFile(Statues status, Context context) {

        if (!STATUS_SAVER_DIR.exists()) {
            if (!STATUS_SAVER_DIR.mkdirs()) {
                Toast.makeText(context, "Something went wrong !!", Toast.LENGTH_SHORT).show();
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

        File destFile = new File(STATUS_SAVER_DIR + File.separator + fileName);

        try {
            org.apache.commons.io.FileUtils.copyFile(status.getFilename(), destFile);
            destFile.setLastModified(System.currentTimeMillis());
            new SingleMediaScanner(context, STATUS_SAVER_DIR);
            showNotification(context, destFile);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void saveImgIntoGallery(Context context, Statues statues) {
        FileOutputStream fos;
        String fileName;
        InputStream inputStream;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());
        ContentValues contentValues = new ContentValues();
        File mediaFile = null;
        if (statues.getFilename().getName().endsWith(".mp4")) {
            fileName = "VID_" + currentDateTime + ".mp4";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                try {
                    mediaFile = new File(STATUS_SAVER_DIR + File.separator + fileName);
                    inputStream = context.getContentResolver().openInputStream(statues.getUri());
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, APP_DIR);
                    Uri imageUri = context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
                    fos = (FileOutputStream) context.getContentResolver().openOutputStream(imageUri);

                    if (inputStream != null) {
                        byte[] buf = new byte[8192];
                        int len;
                        while ((len = inputStream.read(buf)) > 0) {
                            fos.write(buf, 0, len);
                        }
                    }
                    fos.close();
                    inputStream.close();
                } catch (Exception e) {
                    Toast.makeText(context, "Not Save to Gallery" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                try {
                    mediaFile = new File(STATUS_SAVER_DIR + File.separator + fileName);
                    inputStream = context.getContentResolver().openInputStream(statues.getUri());
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, APP_DIR);
                    Uri imageUri = context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
                    fos = (FileOutputStream) context.getContentResolver().openOutputStream(imageUri);

                    if (inputStream != null) {
                        byte[] buf = new byte[8192];
                        int len;
                        while ((len = inputStream.read(buf)) > 0) {
                            fos.write(buf, 0, len);
                        }
                    }
                    fos.close();
                    inputStream.close();
                } catch (Exception e) {
                    Toast.makeText(context, "Not Save to Gallery" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            fileName = "IMG_" + currentDateTime + ".jpg";
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), statues.getUri());
                mediaFile = new File(STATUS_SAVER_DIR + File.separator + fileName);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, APP_DIR);
                    Uri imageUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    fos = (FileOutputStream) context.getContentResolver().openOutputStream(imageUri);
                } else {
                    Toast.makeText(context, "else part Image", Toast.LENGTH_SHORT).show();
                    if (!STATUS_SAVER_DIR.exists()) {
                        if (!STATUS_SAVER_DIR.mkdirs()) {
                            Toast.makeText(context, "Something went wrong !!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    fos = new FileOutputStream(mediaFile);
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } catch (Exception e) {
                Toast.makeText(context, "Not Save to Gallery" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        showNotification(context, mediaFile);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(mediaFile));
        context.sendBroadcast(intent);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void showNotification(Context context, File destFile) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel(context);
        }

        Uri data = FileProvider.getUriForFile(context, "com.itcraftsolution.statussaverforwhatsappdownload" + ".provider", new File(destFile.getAbsolutePath()));
        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (destFile.getName().endsWith(".mp4")) {
            intent.setDataAndType(data, "video/*");
        } else {
            intent.setDataAndType(data, "image/*");
        }

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(context, CHANNEL_NAME);

        notification.setSmallIcon(R.drawable.logo128)
                .setContentTitle(destFile.getName())
                .setContentText("File Saved to " + APP_DIR)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(new Random().nextInt(), notification.build());

        Toast.makeText(context, "Saved to Gallery", Toast.LENGTH_LONG).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void makeNotificationChannel(Context context) {

        NotificationChannel channel = new NotificationChannel(Utils.CHANNEL_NAME, "Saved", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setShowBadge(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }
}
