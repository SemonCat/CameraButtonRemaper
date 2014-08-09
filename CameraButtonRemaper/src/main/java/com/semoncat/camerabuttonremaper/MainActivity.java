package com.semoncat.camerabuttonremaper;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.semoncat.camerabuttonremaper.bean.PackageAdapter;
import com.semoncat.camerabuttonremaper.bean.PackageItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends BaseActivity {

    private PackageAdapter mAdapter;
    private List<PackageItem> mData;

    private ListView mListViewPackageInfo;

    private ProgressDialog progressDialog;


    private SharedPreferences mPreferences;

    public static final String DefaultCameraButtonAppName = "DefaultCameraButtonAppName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);


        new ListAppTask().execute();
    }

    @Override
    protected int setupLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void setupView() {
        mListViewPackageInfo = (ListView) findViewById(R.id.ListViewPackageInfo);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void setupAdapter() {
        mData = new ArrayList<PackageItem>();
        mAdapter = new PackageAdapter(this, mData);
        mListViewPackageInfo.setAdapter(mAdapter);
    }

    @Override
    protected void setupEvent() {
        mListViewPackageInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String packageName = mData.get(position).getPackageName();
                mPreferences.edit().putString(DefaultCameraButtonAppName, packageName).commit();


                Toast.
                        makeText(MainActivity.this, String.format(getString(R.string.setApp), packageName), Toast.LENGTH_SHORT).
                        show();
            }
        });

    }

    public class ListAppTask extends AsyncTask<Void, Void, List<PackageItem>> {

        protected List<PackageItem> doInBackground(Void... args) {
            PackageManager appInfo = getPackageManager();
            List<ApplicationInfo> listInfo = appInfo.getInstalledApplications(0);
            Collections.sort(listInfo, new ApplicationInfo.DisplayNameComparator(appInfo));

            List<PackageItem> data = new ArrayList<PackageItem>();

            for (int index = 0; index < listInfo.size(); index++) {
                try {
                    ApplicationInfo content = listInfo.get(index);
                    if ((content.flags != ApplicationInfo.FLAG_SYSTEM) && content.enabled) {
                        if (content.icon != 0) {
                            PackageItem item = new PackageItem();
                            item.setName(getPackageManager().getApplicationLabel(content).toString());
                            item.setPackageName(content.packageName);
                            item.setIcon(getPackageManager().getDrawable(content.packageName, content.icon, content));
                            data.add(item);
                        }
                    }
                } catch (Exception e) {

                }
            }

            return data;
        }

        protected void onPostExecute(List<PackageItem> result) {
            mData.clear();
            mData.addAll(result);
            mAdapter.notifyDataSetChanged();

            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }
}
