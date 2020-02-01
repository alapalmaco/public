package com.businessalapalma.alapalma1;

import com.google.firebase.database.core.Tag;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import androidx.appcompat.app.AppCompatActivity;
import com.amplitude.api.Amplitude;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.PermissionRequest;
import android.widget.Toast;
import android.Manifest;


import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }


    MyApplication myApplication;

    WebView wv;


    // arreglar sign in google
    public static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCenter.start(getApplication(), "99f2633d-9eec-4389-8d3c-ca2d22775af6",
                Analytics.class, Crashes.class);


        myApplication=MyApplication.getInstance();






        setContentView(R.layout.activity_main);
//        getActionBar().hide();
        wv=findViewById(R.id.webview1);
        if(isNetworkAvailable()){
            //Toast.makeText(this,"available",Toast.LENGTH_LONG).show();
            //wv.setWebChromeClient(new WebChromeClient());
            wv.setWebViewClient(new MyWebViewClient());
            wv.getSettings().setJavaScriptEnabled(true);
            wv.getSettings().setAllowFileAccessFromFileURLs(true);
            wv.getSettings().setAllowUniversalAccessFromFileURLs(true);

            // arreglar sign in google

            // arreglar sign in google
            wv.getSettings().setUserAgentString(USER_AGENT);

            wv.loadUrl("https://alapalma.com");
        }
        else {
            Toast.makeText(this," Internet not available",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(Settings.ACTION_WIRELESS_SETTINGS);
            startActivity(intent);
            while (true){
                if(isNetworkAvailable()){




                    wv.setWebChromeClient(new WebChromeClient() {
                        // Grant permissions for cam

                        @Override
                        public void onPermissionRequest(final PermissionRequest request) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);

                                List<String> permissions = new ArrayList<String>();

                                if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                                    permissions.add(Manifest.permission.CAMERA);

                                }
                                if (!permissions.isEmpty()) {
                                    requestPermissions(permissions.toArray(new String[permissions.size()]), 111);
                                }
                            }
                        }
                    });

                    wv.getSettings().setMediaPlaybackRequiresUserGesture(false);

                    wv.setWebViewClient(new MyWebViewClient());
                    wv.getSettings().setJavaScriptEnabled(true);
                    wv.loadUrl("https://alapalma.com");
                    break;





                }
            }
        }

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("intent://")) {
                try {
                    Context context = view.getContext();
                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);

                    if (intent != null) {
                        view.stopLoading();

                        PackageManager packageManager = context.getPackageManager();
                        ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                        if (info != null) {
                            context.startActivity(intent);
                        } else {
                            String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                            view.loadUrl(fallbackUrl);

                            // or call external broswer
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl));
//                    context.startActivity(browserIntent);
                        }

                        return true;
                    }
                } catch (URISyntaxException e) {

                }
            }

            return false;
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch(keyCode)
            {
                case KeyEvent.KEYCODE_BACK:
                    if(wv.canGoBack() == true){
                        wv.goBack();
                    }else{
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }






}



