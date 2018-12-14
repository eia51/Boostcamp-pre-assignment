package kr.or.connect.boostcamp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.android.volley.toolbox.Volley;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;


public class MainActivity extends RxAppCompatActivity implements FragmentCallback {

    private FragmentManager fragmentManager;
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //초기 volley request queue 초기화
        if (MyApplication.requestQueue == null)
            MyApplication.requestQueue = Volley.newRequestQueue(getApplicationContext());

        //패키지 이름 비어있으면 받아와서 저장
        if (TextUtils.isEmpty(MyApplication.CUSTOM_TAB_PACKAGE_NAME))
            MyApplication.CUSTOM_TAB_PACKAGE_NAME = getPackageName();

        //프래그먼트 매니저가 없으면 받아오기
        if (fragmentManager == null)
            fragmentManager = getSupportFragmentManager();

        if (backPressCloseHandler == null)
            backPressCloseHandler = new BackPressCloseHandler(this);

        //초기 검색, 리스트가 있는 프래그먼트를 만들어서 화면에 띄움
        MainFragment mainFragment = new MainFragment();
        fragmentManager.beginTransaction()
                .add(R.id.mainFrameLayout, mainFragment)
                .commit();

        //customtab 사용 초기화
        MyApplication.customTabInitialize(getApplicationContext());
        MyApplication.noDataObservable.set(false);

    }

    //뒤로가기 두번 누를 때 종료
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    //프래그먼트에서 텍스트 셋
    @Override
    public void setTitleText(String title) {
        getSupportActionBar().setTitle(title);
    }
}
