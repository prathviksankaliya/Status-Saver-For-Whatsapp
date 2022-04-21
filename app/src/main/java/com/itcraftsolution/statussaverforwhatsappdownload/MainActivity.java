package com.itcraftsolution.statussaverforwhatsappdownload;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.itcraftsolution.statussaverforwhatsappdownload.Fragments.HomeFragment;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> getPermission;
    private ActivityMainBinding binding;
    private Toolbar toolbar;


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("Status Saver for Whatsapp");
        setSupportActionBar(toolbar);

        savedStatus();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, toolbar , R.string.OpenDrawer , R.string.CloseDrawer);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setItemIconTintList(null);
        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
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
                    default:
                            savedStatus();

                }
                binding.drawerLayout.closeDrawer(Gravity.LEFT);

                return true;

            }
        });

        if (!checkPermission()){
            showPermissionDialog();
        }

        getPermission = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == MainActivity.RESULT_OK)
                {
                    Toast.makeText(MainActivity.this, "Permission Given in Android 11", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showPermissionDialog() {
        if (SDK_INT >= Build.VERSION_CODES.R) {

            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(intent, 2000);
            } catch (Exception e) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2000);

            }

        } else
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 333);
    }

        @RequiresApi(api = Build.VERSION_CODES.R)
        private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int write = ContextCompat.checkSelfPermission(getApplicationContext(),
                    WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(getApplicationContext(),
                    READ_EXTERNAL_STORAGE);

            return write == PackageManager.PERMISSION_GRANTED &&
                    read == PackageManager.PERMISSION_GRANTED;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 333) {
            if (grantResults.length > 0) {
                boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (read && write){

                }else {

                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {

                } else {

                }
            }
        }
    }

    private void shareApp()
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out Status Saver app for Whatsapp at: https://play.google.com/store/apps/details?id=com.itcraftsolution.statussaverforwhatsappdownload" );
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void rateUs()
    {
        Toast.makeText(this, "Rating", Toast.LENGTH_SHORT).show();
    }

    private void privacyPolicy()
    {
        Toast.makeText(this, "Rating", Toast.LENGTH_SHORT).show();
    }

    private void feedback()
    {
        Toast.makeText(this, "feed back", Toast.LENGTH_SHORT).show();
    }

    private void howToUse()
    {
        Toast.makeText(this, "how to use", Toast.LENGTH_SHORT).show();
    }

    private void savedStatus()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frMainContainer , new HomeFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {

        if(binding.drawerLayout.isDrawerOpen(Gravity.LEFT))
        {
            binding.drawerLayout.closeDrawer(Gravity.LEFT);
        }
        else {
            super.onBackPressed();
        }

    }
}