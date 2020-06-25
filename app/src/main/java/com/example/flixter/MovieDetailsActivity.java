package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixter.databinding.ActivityMovieDetailsBinding;
import com.example.flixter.models.Movie;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {
    Movie movie;
    //view objects:
    ImageView ivPoster;
    TextView tvTitle;
    TextView tvYear;
    RatingBar rvVoteAverage;
    TextView tvOverview;
    TextView tvPopularity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //view binding:
        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //unwrap intent extra using Parcels and key=class_name
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details of selected movie: %s ", movie.toString()));
        //resolve view objects
        ivPoster = (ImageView)binding.ivPoster;
        tvTitle = (TextView)binding.tvTitle;
        tvYear = (TextView)binding.tvYear;
        rvVoteAverage = (RatingBar)binding.ratingBar;
        tvOverview = (TextView)binding.tvOverview;
        tvPopularity = (TextView)binding.tvPopularity;
        //populate objects
        Glide.with(this).load(movie.getBackdropPath()).placeholder(R.drawable.flicks_backdrop_placeholder).into(ivPoster);
        tvTitle.setText(movie.getTitle());
        tvYear.setText("("+movie.getRelease_year()+")");
        float voteAverage = movie.getVoteAverage().floatValue();
        rvVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);
        tvOverview.setText(movie.getOverview());
        tvPopularity.setText(movie.getPopularity().toString()+" views.");

    }
}