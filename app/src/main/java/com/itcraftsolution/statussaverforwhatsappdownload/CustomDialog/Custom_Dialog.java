package com.itcraftsolution.statussaverforwhatsappdownload.CustomDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.itcraftsolution.statussaverforwhatsappdownload.Fragments.HomeFragment;
import com.itcraftsolution.statussaverforwhatsappdownload.R;

public class Custom_Dialog extends Dialog {

    public Custom_Dialog(@NonNull Context context) {
        super(context);
    }

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_layout);

        btn = findViewById(R.id.btnOk);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dismiss();
            }
        });
    }
}
