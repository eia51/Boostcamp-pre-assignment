package kr.or.connect.boostcamp;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;

public class MyApplication extends Application {

    public static String CUSTOM_TAB_PACKAGE_NAME;

    //for networking
    public static final String SEARCH_URL_HOST = "https://openapi.naver.com/v1/search/movie.json?query=";
    public static RequestQueue requestQueue;

    //for custom tab
    private static CustomTabsClient mCustomTabsClient;
    private static CustomTabsSession mCustomTabsSession;
    private static CustomTabsServiceConnection mCustomTabsServiceConnection;
    public static CustomTabsIntent mCustomTabsIntent;

    //for infinity loading
    public static int startNum = 1;
    public static boolean noDataFlag = false;

    //Rxjava observable
    public static ObservableField<Boolean> noDataObservable = new ObservableField<>();

    //final value for 'Naver Open API'
    public static final String clientId = "pZB4Q9x2uSzXyfqMAZOC";
    public static final String client_secret = "1y9rRxQL7G";


    //show toast
    public static void makeToast(Context context, String value) {
        Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
    }

    //image load
    public static void loadImage(ImageView iv, String url) {
        Glide.with(iv.getContext()).load(url).into(iv);
    }

    //custom tab init
    public static void customTabInitialize(Context context) {
        mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                mCustomTabsClient = customTabsClient;
                mCustomTabsClient.warmup(0L);
                mCustomTabsSession = mCustomTabsClient.newSession(null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mCustomTabsClient = null;
            }
        };

        CustomTabsClient.bindCustomTabsService(context, MyApplication.CUSTOM_TAB_PACKAGE_NAME, mCustomTabsServiceConnection);

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_back_white);
        MyApplication.mCustomTabsIntent = new CustomTabsIntent.Builder(mCustomTabsSession)
                .setShowTitle(false)
                .setToolbarColor(context.getResources().getColor(R.color.colorPrimary))
                .setCloseButtonIcon(icon)
                .setStartAnimations(context, R.anim.translate_slidein, R.anim.translate_slideout)
                .build();
    }

    //load url using custom tab
    public static void chromeCustomTab(Context context, String url) {
        mCustomTabsIntent.launchUrl(context, Uri.parse(url));
    }

    //change status bar color
    public static void setStatusBar(Window window) {
        View view = window.getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(view.getContext().getResources().getColor(R.color.colorPrimary));
        } else {
            window.setStatusBarColor(Color.BLACK);
        }
    }
}
