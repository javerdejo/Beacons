package com.example.umacamp;

import android.content.Intent;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

class ShowInfoActivity extends AppCompatActivity {
    TextView siaTitle;
    TextView siaSubtitle;
    WebView siaWebBrowser;
    ImageButton siaButtonExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);

        String strURL;
        String strUUID;
        String strMajor;
        String strMinor;

        siaTitle = (TextView)findViewById(R.id.siaTitle);
        siaTitle.setText(getIntent().getStringExtra("TITLE_ID"));

        siaSubtitle = (TextView)findViewById(R.id.siaSubtitle);
        siaSubtitle.setText(getIntent().getStringExtra("INFO_ID"));

        siaWebBrowser = (WebView)findViewById(R.id.siaWebBrowser);
        siaWebBrowser.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        strURL = getResources().getString(R.string.url_server);
        strUUID = getResources().getString(R.string.uuid);
        strMajor = getIntent().getStringExtra("MAJOR_ID");
        strMinor = getIntent().getStringExtra("MINOR_ID");

        siaWebBrowser.loadUrl(strURL + "/" + strUUID + "/" + strMajor + "/" + strMinor);
        //Log.d("URL", strURL + "/" + strUUID + "/" + strMajor + "/" + strMinor);

        siaButtonExit = (ImageButton) findViewById(R.id.siaButtonExit);
        siaButtonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainApp.class);
                startActivity(intent);
            }
        });
    }
}
