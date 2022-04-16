package com.itcraftsolution.statussaverforwhatsappdownload.Utils;

import android.os.Environment;

import java.io.File;

public class Utils {

    public static File RootDirectorywhatsapp =
            new File(Environment.getExternalStorageDirectory()+ "/StatusSaver/");

    public static void createFileFolder()
    {
        if(!RootDirectorywhatsapp.exists())
        {
            RootDirectorywhatsapp.mkdirs();
        }
    }
}
