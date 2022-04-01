package com.itcraftsolution.statussaverforwhatsappdownload;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.itcraftsolution.statussaverforwhatsappdownload.Fragments.SpalshFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frMainContainer , new SpalshFragment());
        fragmentTransaction.commit();
    }
}