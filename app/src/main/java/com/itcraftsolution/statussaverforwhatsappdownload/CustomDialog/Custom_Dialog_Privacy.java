package com.itcraftsolution.statussaverforwhatsappdownload.CustomDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.itcraftsolution.statussaverforwhatsappdownload.R;

public class Custom_Dialog_Privacy extends Dialog {

    public Custom_Dialog_Privacy(@NonNull Context context) {
        super(context);
    }

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_privacy);
        btn = findViewById(R.id.btnOk);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
