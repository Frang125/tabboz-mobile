package com.example.rstudio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.WebViewAssetLoader;
import androidx.webkit.WebViewClientCompat;




//nota breve: questo porting è un semplice wrapper della versione javascript con qualche modifica per adattarlo agli schermi piccoli oltre ad farlo funzionare offline.

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // Creata variabile che si collega al relativo webview
        WebView test= findViewById(R.id.web);
        // Abilitazioni varie per il webview  per il corretto funzionamento del app
        test.getSettings().setJavaScriptEnabled(true);
        test.getSettings().setDomStorageEnabled(true);
        test.setWebViewClient(new WebViewClient());
        WebSettings settings = test.getSettings();
        settings.setAllowFileAccess(true);
        final WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder().addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this)).addPathHandler("/res/", new WebViewAssetLoader.ResourcesPathHandler(this)).build();
        test.setWebViewClient(new LocalContentWebViewClient(assetLoader));
        //Caricamento , effettuato tramite la creazione di un server locale, della directory contente il file index del progetto
        test.loadUrl("https://appassets.androidplatform.net/assets/index.html");
    }


   // creazione classe necessaria per creare il server locale direttamente collegato agli asset prensenti in app localmente
    // questo finto server locale funziona solamente al interno del dispositivo

    private static class LocalContentWebViewClient extends WebViewClientCompat {

        private final WebViewAssetLoader mAssetLoader;

        LocalContentWebViewClient(WebViewAssetLoader assetLoader) {
            mAssetLoader = assetLoader;
        }

        @Override

        public WebResourceResponse shouldInterceptRequest(WebView view,
                                                          WebResourceRequest request) {
            return mAssetLoader.shouldInterceptRequest(request.getUrl());
        }





    }


    // bug fix riguardante l'avvio di tabooz, che anche se veniva messo in background durante la comparsa di avvio continua ad riprodurre il suono di avvio
    @Override
    protected void onStop() {
        super.onStop();
        AudioManager manager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        manager.setStreamVolume(AudioManager.STREAM_MUSIC, 0,  AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }





}



