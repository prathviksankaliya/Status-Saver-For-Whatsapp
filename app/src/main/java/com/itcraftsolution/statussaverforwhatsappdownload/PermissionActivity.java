package com.itcraftsolution.statussaverforwhatsappdownload;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.itcraftsolution.statussaverforwhatsappdownload.Utils.Utils;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.ActivityPermissionBinding;

public class PermissionActivity extends AppCompatActivity {

    private ActivityPermissionBinding binding;
    private SharedPreferences spf;
    private boolean isPermissionGranted;
    private String startDir, finalDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPermissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spf = getSharedPreferences("FolderPermission", MODE_PRIVATE);

        binding.btnPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                    isPermissionGranted = spf.getBoolean("isGranted", false);

                    if (!isPermissionGranted) {
                        folderPermission();
                    }
                } else {
                    if (!checkPermission()) {
                        showPermission();
                    }
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void folderPermission() {
        if (Utils.STATUS_DIRECTORY_NEW.exists()) {
            startDir = "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses";
        } else if (Utils.STATUS_DIRECTORY.exists()) {
            startDir = "WhatsApp%2FMedia%2F.Statuses";
        } else if (Utils.STATUS_DIRECTORY_WP_Business.exists()) {
            startDir = "Android%2Fmedia%2Fcom.whatsapp.w4b%2FWhatsApp Business%2FMedia%2F.Statuses";
        } else if (Utils.STATUS_DIRECTORY_GBWHATSAPP.exists()) {
            startDir = "GBWhatsApp%2FMedia%2F.Statuses";
        }else
        {
            Toast.makeText(PermissionActivity.this, "Can't Find Directory!!", Toast.LENGTH_SHORT).show();
        }

        StorageManager storageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
        Intent intent = storageManager.getPrimaryStorageVolume().createOpenDocumentTreeIntent();

        Uri uri = intent.getParcelableExtra("android.provider.extra.INITIAL_URI");
        String scheme = uri.toString();
        scheme = scheme.replace("/root/", "/document/");
        finalDir = scheme + "%3A" + startDir;

        uri = Uri.parse(finalDir);
        intent.putExtra("android.provider.extra.INITIAL_URI", uri);
        startActivityForResult(intent, 6);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                Uri treeUri = data.getData();
                if (treeUri.getPath().endsWith(".Statuses")) {
                    getContentResolver().takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    SharedPreferences.Editor edit = spf.edit();
                    edit.putBoolean("isGranted", true);
                    edit.putString("PATH", treeUri.toString());
                    edit.apply();

                    Intent intent = new Intent(PermissionActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Something Went Wrong!!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showPermission() {
        // permission for 23 to 29 SDK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    private boolean checkPermission() {

        int write = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);

        return write == PackageManager.PERMISSION_GRANTED &&
                read == PackageManager.PERMISSION_GRANTED;

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermission()) {
            Intent intent = new Intent(PermissionActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}