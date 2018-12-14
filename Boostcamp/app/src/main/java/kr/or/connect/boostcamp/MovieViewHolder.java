package kr.or.connect.boostcamp;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import kr.or.connect.boostcamp.databinding.ItemMovieBinding;

public class MovieViewHolder extends RecyclerView.ViewHolder {
    ItemMovieBinding binding;

    public MovieViewHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }
}

