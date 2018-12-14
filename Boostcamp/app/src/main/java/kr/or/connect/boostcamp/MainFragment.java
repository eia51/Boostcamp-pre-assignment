package kr.or.connect.boostcamp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainFragment extends Fragment {

    public FragmentCallback callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCallback) {
            callback = (FragmentCallback) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (callback != null) {
            callback = null;
        }
    }

    private EditText mEdtSearch;
    private RecyclerView recyclerView;
    TextView mTvNoData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);
        callback.setTitleText("부스트캠프 사전과제");

        //리사이클러뷰
        recyclerView = (RecyclerView) rootView.findViewById(R.id.mvRecyclerList);

        //프로그레스바
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressbar_infinity);

        //검색 EditText
        mEdtSearch = (EditText) rootView.findViewById(R.id.etSearchInput);
        mEdtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onSearchBtnClicked(false);
                }
                return false;
            }
        });

        //검색 Button
        Button btnSearch = (Button) rootView.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(v -> onSearchBtnClicked(false));


        mTvNoData = (TextView) rootView.findViewById(R.id.tvNoData);
        return rootView;
    }

    //검색버튼 클릭(뷰 버튼 & 소프트키보드버튼)
    public void onSearchBtnClicked(boolean loadingFlag) {

        //값 추출
        String search = mEdtSearch.getText().toString();

        //프로그레스바 띄우기
        progressBar.setVisibility(View.VISIBLE);

        //클릭에 의한 접근일 시 끝 플래그 초기화
        if (!loadingFlag) {
            endFlag = false;
            MyApplication.startNum = 1;
        }

        //빈 값이 아니면 검색요청
        if (!TextUtils.isEmpty(search)) {
            setRequest(search, MyApplication.startNum, loadingFlag);
        }
    }

    boolean endFlag = false;
    int total = -1;

    //네이버 검색을 위한 open api 호출
    public void setRequest(String search, int startIndex, boolean loadingFlag) {
        try {

            String text = URLEncoder.encode(search, "UTF-8");
            //로딩해올 개수 지정
            int displayNum = (total < 0) ? 10 : (total - startIndex + 1 < 10) ? total - startIndex + 1 : 10;
            String url = MyApplication.SEARCH_URL_HOST + text + "&start=" + startIndex + "&display=" + displayNum;

            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    url,
                    response -> {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("DataCheck", response);
                        Gson gson = new Gson();
                        ResponseData result = gson.fromJson(response, ResponseData.class);

                        //받아온 데이터 없는 경우
                        if (result.getItems().size() <= 0) {
                            MyApplication.noDataFlag = true;
                            mAdapter.getItems().clear();
                            mAdapter.notifyDataSetChanged();
                        } else {
                            MyApplication.noDataFlag = false;
                        }

                        rx.Observable
                                .just(MyApplication.noDataFlag)
                                .compose(((MainActivity) Objects.requireNonNull(getActivity())).<Boolean>bindToLifecycle())
                                .subscribe(
                                        flag -> {
                                            int visibility = (flag) ? View.VISIBLE : View.INVISIBLE;
                                            mTvNoData.setVisibility(visibility);
                                        }
                                );

                        if (result.getItems().size() <= 0)
                            return;

                        total = result.getTotal();
                        if (result.getItems().size() < 10 || result.getItems().size() == total)
                            endFlag = true;

                        if (!loadingFlag) {
                            setRecyclerList(result.getItems());
                        } else {
                            mAdapter.concatList(result.getItems());
                            mAdapter.notifyDataSetChanged();
                        }
                    },
                    error -> progressBar.setVisibility(View.INVISIBLE)

            ) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("X-Naver-Client-Id", MyApplication.clientId);
                    params.put("X-Naver-Client-Secret", MyApplication.client_secret);
                    return params;
                }
            };
            request.setShouldCache(false);
            MyApplication.requestQueue.add(request);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private ProgressBar progressBar;
    private MovieAdapter mAdapter;

    //리사이클러뷰 설정
    private void setRecyclerList(ArrayList<Movie> items) {

        mAdapter = new MovieAdapter(getActivity(), items);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(Objects.requireNonNull(getActivity())));

        //무한스크롤 구현
        MovieAdapter.EndlessScrollListener listener = position -> {
            fetchData();
            return false;
        };
        //어댑터 설정
        mAdapter.setEndlessScrollListener(listener);
    }

    //데이터 5개 추가로딩
    private void fetchData() {
        MyApplication.startNum += 10;
        onSearchBtnClicked(true);
    }
}
