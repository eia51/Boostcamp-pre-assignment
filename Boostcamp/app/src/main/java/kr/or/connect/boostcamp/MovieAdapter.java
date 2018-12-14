package kr.or.connect.boostcamp;


import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private Context context;
    private ArrayList<Movie> items;
    private EndlessScrollListener endlessScrollListener;

    public ArrayList<Movie> getItems() {
        return items;
    }

    public void setItems(ArrayList<Movie> items) {
        this.items = items;
    }

    public MovieAdapter(Context context, ArrayList<Movie> items) {
        this.context = context;
        this.items = (items == null) ? new ArrayList<>() : items;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_movie, parent, false);

        return new MovieViewHolder(itemView);
    }

    public void setEndlessScrollListener(EndlessScrollListener endlessScrollListener) {
        this.endlessScrollListener = endlessScrollListener;
    }


    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        //데이터 바인팅을 통해 자동으로 값 할당
        Movie item = getItem(position);
        holder.binding.setMovie(item);

        //리사이클러뷰 내 아이템이 클릭됐을 때 발생하는 이벤트
        holder.binding.getRoot().setOnClickListener(v -> {
            Movie selectedItem = getItem(position);
            MyApplication.chromeCustomTab(context, selectedItem.getLink());
        });


        // you can cache getItemCount() in a member variable for more performance tuning
        if (position == getItemCount() - 1) {
            if (endlessScrollListener != null) {
                Log.d("DataCheck", "pos : " + position + ", getItemCount : " + getItemCount());
                endlessScrollListener.onLoadMore(position);
            }
        }

    }

    public void addItem(Movie item) {
        items.add(item);
    }

    public void concatList(ArrayList<Movie> nItems) {
        items.addAll(nItems);

    }

    public Movie getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @BindingAdapter("imgRes")
    public static void loadImage(ImageView iv, String url) {
        if (!TextUtils.isEmpty(url))
            MyApplication.loadImage(iv, url);
        else
            iv.setImageResource(R.drawable.no_pic);
    }

    public interface EndlessScrollListener {
        boolean onLoadMore(int position);
    }
}
