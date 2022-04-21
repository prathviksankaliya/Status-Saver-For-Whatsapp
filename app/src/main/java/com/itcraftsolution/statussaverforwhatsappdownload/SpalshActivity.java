package com.itcraftsolution.statussaverforwhatsappdownload;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.itcraftsolution.statussaverforwhatsappdownload.Fragments.HomeFragment;

public class SpalshActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SpalshActivity.this , MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1100);
    }
}