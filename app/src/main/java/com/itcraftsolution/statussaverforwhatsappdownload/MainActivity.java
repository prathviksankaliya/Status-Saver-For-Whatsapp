package com.itcraftsolution.statussaverforwhatsappdownload;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayoutMediator;
import com.itcraftsolution.statussaverforwhatsappdownload.Adapter.HomeTabVIewPagerAdapter;
import com.itcraftsolution.statussaverforwhatsappdownload.CustomDialog.Custom_Dialog;
import com.itcraftsolution.statussaverforwhatsappdownload.CustomDialog.Custom_Dialog_Privacy;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Toolbar toolbar;
    private HomeTabVIewPagerAdapter adapter;
    private SharedPreferences spf;
    private boolean isPermissionGranted = false;
    private String[] tabTitles = new String[]{"IMAGES", "VIDEOS", "SAVED"};

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadData();

        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menuHowUse:
                        howToUse();
                        break;
                    case R.id.menuFeedback:
                        feedback();
                        break;
                    case R.id.menuPrivacy:
                        privacyPolicy();
                        break;
                    case R.id.menuRateus:
                        rateUs();
                        break;
                    case R.id.menuShare:
                        shareApp();
                        break;
                }
                binding.drawerLayout.closeDrawer(Gravity.LEFT);

                return true;

            }
        });


    }

    private void loadData() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Status Saver for Whatsapp");
        setSupportActionBar(toolbar);

        adapter = new HomeTabVIewPagerAdapter(MainActivity.this);
        binding.vpHome.setAdapter(adapter);

        new TabLayoutMediator(binding.tabHomeLayout, binding.vpHome, (((tab, position) -> tab.setText(tabTitles[position])))).attach();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setItemIconTintList(null);
    }


    private void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out Status Saver app for Whatsapp at: https://play.google.com/store/apps/developer?id=IT+Craft+Solution");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void rateUs() {

        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.itcraftsolution.statussaverforwhatsappdownload");
//        + MainActivity.this.getPackageName()
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void privacyPolicy() {
        Custom_Dialog_Privacy dialog = new Custom_Dialog_Privacy(MainActivity.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void feedback() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_SUBJECT, "FeedBack of Status saver for Whatsapp");
        intent.setData(Uri.parse("mailto:itcraftsolution1@gmail.com"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void howToUse() {
        Custom_Dialog dialog = new Custom_Dialog(MainActivity.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            binding.drawerLayout.closeDrawer(Gravity.LEFT);
        } else if (Objects.requireNonNull(binding.tabHomeLayout.getTabAt(0)).isSelected()) {
            super.onBackPressed();
        } else {
            binding.tabHomeLayout.selectTab(binding.tabHomeLayout.getTabAt(0));
        }
    }
}