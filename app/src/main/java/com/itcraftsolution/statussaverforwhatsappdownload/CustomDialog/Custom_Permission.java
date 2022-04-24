package com.itcraftsolution.statussaverforwhatsappdownload.CustomDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.itcraftsolution.statussaverforwhatsappdownload.R;

public class Custom_Permission extends Dialog {

    public Custom_Permission(@NonNull Context context) {
        super(context);
    }
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage_dialog);
        btn = findViewById(R.id.btnPermission);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                SharedPreferences spf = getContext().getSharedPreferences("Permission" , Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = spf.edit();
                edit.putBoolean("Allow" , true);
                edit.apply();
            }
        });

    }


}
