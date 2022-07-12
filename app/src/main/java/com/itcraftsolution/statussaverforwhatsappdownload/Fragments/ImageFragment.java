package com.itcraftsolution.statussaverforwhatsappdownload.Fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.itcraftsolution.statussaverforwhatsappdownload.Adapter.ImageRecyclerAdapter;
import com.itcraftsolution.statussaverforwhatsappdownload.MainActivity;
import com.itcraftsolution.statussaverforwhatsappdownload.Models.Statues;
import com.itcraftsolution.statussaverforwhatsappdownload.Utils.Utils;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.FragmentImageBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageFragment extends Fragment {


    private FragmentImageBinding binding;
    private ArrayList<Statues> list;
    private ImageRecyclerAdapter adapter;
    private ProgressDialog dialog;
    private SharedPreferences spf;
    private boolean isFolderPermissionGranted;
    private String istreeUri;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentImageBinding.inflate(getLayoutInflater());

//        dialog = new ProgressDialog(requireContext());
//        dialog.setMessage("Statuses Loading ....");
//        dialog.setCancelable(false);
//        dialog.show();
        list = new ArrayList<>();

        spf = requireContext().getSharedPreferences("FolderPermission", Context.MODE_PRIVATE);

        isFolderPermissionGranted = spf.getBoolean("isGranted", false);
        istreeUri = spf.getString("PATH", null);

        if(!isFolderPermissionGranted)
        {
            folderPermission();
        }else{
            requireContext().getContentResolver().takePersistableUriPermission(Uri.parse(istreeUri), Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if(istreeUri != null)
            {
                Statues model;

                DocumentFile file = DocumentFile.fromTreeUri(requireContext(), Uri.parse(istreeUri));
                DocumentFile[] documentFiles = file.listFiles();

                for (int i = 0; i < documentFiles.length; i++) {
                    documentFiles[i].getUri().toString();
                    DocumentFile singlefile = documentFiles[i];

                    if (singlefile.getUri().toString().endsWith(".png") || singlefile.getUri().toString().endsWith(".jpg")) {
                        File file1 = new File(singlefile.getUri().toString());
                        model = new Statues("whats " + i, documentFiles[i].getName(), file1, singlefile.getUri());
                        list.add(model);

                    }
                }
                setupRecyclerview(list);
            }
        }

//        if (Utils.STATUS_DIRECTORY.exists()) {
//
//            getData(Utils.STATUS_DIRECTORY);
//            dialog.dismiss();
//            binding.imageRefershView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//                @Override
//                public void onRefresh() {
//                    dialog.show();
//                    list = new ArrayList<>();
//                    getData(Utils.STATUS_DIRECTORY);
//                    binding.imageRefershView.setRefreshing(false);
//                    adapter.notifyDataSetChanged();
//                    dialog.dismiss();
//                }
//            });
//
//        } else if (Utils.STATUS_DIRECTORY_NEW.exists()) {
//
//            getData(Utils.STATUS_DIRECTORY_NEW);
//            dialog.dismiss();
//            binding.imageRefershView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//                @Override
//                public void onRefresh() {
//                    dialog.show();
//                    list = new ArrayList<>();
//                    getData(Utils.STATUS_DIRECTORY_NEW);
//                    binding.imageRefershView.setRefreshing(false);
//                    adapter.notifyDataSetChanged();
//                    dialog.dismiss();
//                }
//            });
//
//        } else if (Utils.STATUS_DIRECTORY_GBWHATSAPP.exists()) {
//
//            getData(Utils.STATUS_DIRECTORY_GBWHATSAPP);
//            dialog.dismiss();
//            binding.imageRefershView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//                @Override
//                public void onRefresh() {
//                    dialog.show();
//                    list = new ArrayList<>();
//                    getData(Utils.STATUS_DIRECTORY_GBWHATSAPP);
//                    binding.imageRefershView.setRefreshing(false);
//                    adapter.notifyDataSetChanged();
//                    dialog.dismiss();
//                }
//            });
//        } else {
//            binding.imageRefershView.setRefreshing(false);
//            dialog.dismiss();
//            Toast.makeText(requireContext(), "Can't Whatsapp File Find!! ", Toast.LENGTH_SHORT).show();
//        }
//
//        if(list.isEmpty())
//        {
//            binding.rvImage.setVisibility(View.GONE);
//            binding.VNotFoundImage.setVisibility(View.VISIBLE);
//        }

        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void folderPermission()
    {

        StorageManager storageManager = (StorageManager) requireContext().getSystemService(Context.STORAGE_SERVICE);
        Intent intent = storageManager.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
        String targetUri ="GBWhatsApp%2FMedia%2F.Statuses";
        Uri uri = intent.getParcelableExtra("android.provider.extra.INITIAL_URI");
        String scheme = uri.toString();
        scheme = scheme.replace("/root/", "/tree/");
        scheme = scheme + "%3A" + targetUri;

        uri = Uri.parse(scheme);
        intent.putExtra("android.provider.extra.INITIAL_URI", uri);
        intent.putExtra("android.content.extra.SHOW_ADVANCED", true);
        startActivityForResult(intent,6);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if(data != null)
            {
                Statues model;
                Uri treeUri = data.getData();
                requireContext().getContentResolver().takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Toast.makeText(requireContext(), ""+treeUri, Toast.LENGTH_SHORT).show();
                DocumentFile documentFile = DocumentFile.fromTreeUri(requireContext(), treeUri);


                SharedPreferences.Editor edit = spf.edit();
                edit.putBoolean("isGranted", true);
                edit.putString("PATH", treeUri.toString());
                edit.apply();

                DocumentFile[] documentFiles = documentFile.listFiles();
                for (int i = 0; i < documentFiles.length; i++) {
                    documentFiles[i].getUri().toString();
                    DocumentFile singlefile = documentFiles[i];

                    if (singlefile.getUri().toString().endsWith(".png") || singlefile.getUri().toString().endsWith(".jpg")) {
                        File file = new File(singlefile.getUri().toString());
                        model = new Statues("whats " + i, documentFiles[i].getName(), file, singlefile.getUri());
                    list.add(model);
                    }
                }
                setupRecyclerview(list);
            }
        }
    }
//    private void showPermission()
//    {
//        // permission for 23 to 29 SDK
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//        {
//            if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
//                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
//            {
//                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
//            }
//        }
//    }
//
//    private boolean checkPermission() {
//
//        int write = ContextCompat.checkSelfPermission(requireContext(),
//                Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        int read = ContextCompat.checkSelfPermission(requireContext(),
//                Manifest.permission.READ_EXTERNAL_STORAGE);
//
//        return write == PackageManager.PERMISSION_GRANTED &&
//                read == PackageManager.PERMISSION_GRANTED;
//
//    }
    private void setupRecyclerview(ArrayList<Statues> statusList)
    {
        adapter = new ImageRecyclerAdapter(requireContext(), statusList);
        binding.rvImage.setAdapter(adapter);
        binding.rvImage.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
    }
}
