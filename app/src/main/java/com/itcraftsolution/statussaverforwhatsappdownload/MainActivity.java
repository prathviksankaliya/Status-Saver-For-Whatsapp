package com.itcraftsolution.statussaverforwhatsappdownload;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;
import com.itcraftsolution.statussaverforwhatsappdownload.Adapter.HomeTabVIewPagerAdapter;
import com.itcraftsolution.statussaverforwhatsappdownload.CustomDialog.Custom_Dialog;
import com.itcraftsolution.statussaverforwhatsappdownload.CustomDialog.Custom_Dialog_Privacy;
import com.itcraftsolution.statussaverforwhatsappdownload.Utils.Utils;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Toolbar toolbar;
    private ReviewInfo info ;
    private ReviewManager manager;
    private boolean isGranted = false;
    private HomeTabVIewPagerAdapter adapter;
    private SharedPreferences spf;
    private boolean isPermissionGranted = false;
    private String [] tabTitles = new String[]{"IMAGES" , "VIDEOS","SAVED"};
    private ActivityResultLauncher<Intent> launcher;

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

                switch (item.getItemId())
                {
//                    case R.id.menuSaved:
//                                    savedStatus();
//                                    break;
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


//        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//            @Override
//            public void onActivityResult(ActivityResult result) {
//                if(result.getResultCode() == RESULT_OK)
//                {
//                    if(result.getData() != null)
//                    {
//                        Uri treeUri = result.getData().getData();
//                        getContentResolver().takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION |
//                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        Toast.makeText(MainActivity.this, ""+treeUri, Toast.LENGTH_SHORT).show();
//                        DocumentFile documentFile = DocumentFile.fromTreeUri(MainActivity.this, treeUri);
//
//                    }
//                }
//            }
//        });

    }

    private void loadData()
    {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Status Saver for Whatsapp");
        setSupportActionBar(toolbar);

        adapter = new HomeTabVIewPagerAdapter(MainActivity.this);
        binding.vpHome.setAdapter(adapter);

        new TabLayoutMediator(binding.tabHomeLayout, binding.vpHome,(((tab, position) -> tab.setText(tabTitles[position])))).attach();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, toolbar , R.string.OpenDrawer , R.string.CloseDrawer);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setItemIconTintList(null);
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

//    private void savedStatus()
//    {
//        binding.vpHome.setVisibility(View.GONE);
//        binding.cardView.setVisibility(View.GONE);
//        binding.tabHomeLayout.setVisibility(View.GONE);
//        binding.frMainContainer.setVisibility(View.VISIBLE);
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.frMainContainer , new SaveFragment());
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//    }

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


}