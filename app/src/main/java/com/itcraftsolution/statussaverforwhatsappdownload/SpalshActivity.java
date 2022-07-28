package com.itcraftsolution.statussaverforwhatsappdownload;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SpalshActivity extends AppCompatActivity {

    private SharedPreferences spf;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
        spf = getSharedPreferences("FolderPermission", MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (spf.getBoolean("isGranted", false)) {
                    intent = new Intent(SpalshActivity.this, MainActivity.class);
                } else {

                    intent = new Intent(SpalshActivity.this, PermissionActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 1100);
    }
}