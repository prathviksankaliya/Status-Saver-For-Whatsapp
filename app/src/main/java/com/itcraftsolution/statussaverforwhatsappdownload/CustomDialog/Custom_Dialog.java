package com.itcraftsolution.statussaverforwhatsappdownload.CustomDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.itcraftsolution.statussaverforwhatsappdownload.R;

public class Custom_Dialog extends Dialog {

    public Custom_Dialog(@NonNull Context context) {
        super(context);
    }

    private Button btn;
    private TextView txHeading, txDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_layout);

        btn = findViewById(R.id.btnOk);
        txHeading = findViewById(R.id.txHeading);
        txDetails = findViewById(R.id.txDetails);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
