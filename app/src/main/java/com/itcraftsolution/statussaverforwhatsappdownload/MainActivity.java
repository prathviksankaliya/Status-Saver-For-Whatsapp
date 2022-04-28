package com.itcraftsolution.statussaverforwhatsappdownload;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;
import com.itcraftsolution.statussaverforwhatsappdownload.CustomDialog.Custom_Dialog;
import com.itcraftsolution.statussaverforwhatsappdownload.CustomDialog.Custom_Dialog_Privacy;
import com.itcraftsolution.statussaverforwhatsappdownload.CustomDialog.Custom_Permission;
import com.itcraftsolution.statussaverforwhatsappdownload.Fragments.HomeFragment;
import com.itcraftsolution.statussaverforwhatsappdownload.Fragments.SaveFragment;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Toolbar toolbar;
    private ReviewInfo info ;
    private ReviewManager manager;
    private boolean isGranted = false;


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("Status Saver for Whatsapp");
        setSupportActionBar(toolbar);

//        activeReviewInfo();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, toolbar , R.string.OpenDrawer , R.string.CloseDrawer);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setItemIconTintList(null);
        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.menuSaved:
                                    savedStatus();
                                    break;
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

        if(isGranted)
        {
            binding.storagePermission.getRoot().setVisibility(View.GONE);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frMainContainer , new HomeFragment());
            fragmentTransaction.commit();
        }
        binding.storagePermission.btnPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPermissionDialog();
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
                "Hey check out Status Saver app for Whatsapp at: https://play.google.com/store/apps/details?id=com.itcraftsolution.picturepoint" );
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void rateUs()
    {

        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.itcraftsolution.picturepoint" );
//        + MainActivity.this.getPackageName()
        Intent intent = new Intent(Intent.ACTION_VIEW , uri);
        try{
            startActivity(intent);
        }catch(Exception e)
        {
            Toast.makeText(this, "Unable to open "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }


//        2nd approach  in - app rating Review

//        if(info != null)
//        {
//            Task<Void> flow = manager.launchReviewFlow(MainActivity.this , info);
//            flow.addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    Toast.makeText(MainActivity.this, "Rating Is Done", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }

    }

    private void activeReviewInfo()
    {
        manager = ReviewManagerFactory.create(MainActivity.this);
        Task<ReviewInfo> manageInfoTask =  manager.requestReviewFlow();
        manageInfoTask.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(@NonNull Task<ReviewInfo> task) {
                if(task.isSuccessful())
                {
                    info = task.getResult();

                }
                else {
                    Toast.makeText(MainActivity.this, "Failed ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void privacyPolicy()
    {
        Custom_Dialog_Privacy dialog = new Custom_Dialog_Privacy(MainActivity.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void feedback()
    {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_SUBJECT , "FeedBack of Status saver for Whatsapp");
        intent.setData(Uri.parse("mailto:itcraftsolution1@gmail.com"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
                startActivity(intent);
        }catch (ActivityNotFoundException e)
        {
            Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void howToUse()
    {
        Custom_Dialog dialog = new Custom_Dialog(MainActivity.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void savedStatus()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frMainContainer , new SaveFragment());
        fragmentTransaction.addToBackStack(null);
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
//        new AlertDialog.Builder(MainActivity.this)
//                .setTitle("Exit App")
//                .setCancelable(false)
//                .setMessage("Are You want to Sure Exit The App ?")
//                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        MainActivity.super.onBackPressed();
//                    }
//                })
//                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onResume() {
        super.onResume();
        if(checkPermission())
        {
            isGranted = true;
            binding.storagePermission.getRoot().setVisibility(View.GONE);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frMainContainer , new HomeFragment());
            fragmentTransaction.commit();
        }else {
            Toast.makeText(this, "Please Allow Storage Permission", Toast.LENGTH_SHORT).show();
        }

    }
}