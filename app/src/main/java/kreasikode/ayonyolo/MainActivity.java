package kreasikode.ayonyolo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {


    ProgressBar progressBar;
    private Context context;
    private Activity activity;
    private CoordinatorLayout coordinatorLayout;
    private WebView webView;
    private ImageView splash;

    @SuppressLint({"SetJavaScriptEnabled", "ObsoleteSdkInt"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        activity = this;

        progressBar = findViewById(R.id.pb_webLoad);

        coordinatorLayout = findViewById(R.id.cl_webView);

        splash = findViewById(R.id.img_splash);

        webView = findViewById(R.id.wv_nyoloWeb);
        webView.setVisibility(View.GONE);

        loadWeb();

    }

    private void loadWeb() {
        final Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(1000);

        final Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);

        final AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(fadeIn);
        animationSet.addAnimation(fadeOut);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                splash.setAnimation(fadeOut);
                splash.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        splash.setVisibility(View.GONE);
                        webView.setVisibility(View.VISIBLE);
                        webView.setAnimation(fadeIn);
                    }
                }, 1600);
            }
        });

        webView.loadUrl("https://techsohail1.000webhostapp.com/menu");
        webView.setWebChromeClient(new WebChromeClient());

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            settings.setDatabasePath("/data/data" + webView.getContext().getPackageName() + "/databases/");
        }
    }

    protected void exitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("Yakin Ingin Keluar?");
        builder.setMessage("Ada banyak cerita yang belum kamu baca, yakin mau keluar?");
        builder.setCancelable(true);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.super.onBackPressed();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();

        } else {
            exitDialog();
        }
    }
}
