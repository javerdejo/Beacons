package com.example.umacamp;

import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

class ShowInfoActivity extends AppCompatActivity {
    WebView siaWebBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);

        siaWebBrowser = (WebView)findViewById(R.id.siaWebBrowser);
        siaWebBrowser.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        String strURL = getResources().getString(R.string.url_server);
        String strUUID = getResources().getString(R.string.uuid);
        String strMajor = getIntent().getStringExtra("MAJOR_ID");
        String strMinor = getIntent().getStringExtra("MINOR_ID");

        siaWebBrowser.loadUrl(strURL + "/" + strUUID + "/" + strMajor + "/" + strMinor);
    }
}
