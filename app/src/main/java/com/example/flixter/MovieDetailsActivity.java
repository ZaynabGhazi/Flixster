package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.databinding.ActivityMovieDetailsBinding;
import com.example.flixter.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import okhttp3.Headers;

import static android.text.TextUtils.isEmpty;

public class MovieDetailsActivity extends AppCompatActivity {
    public static final String VIDEO_URL_1 ="https://api.themoviedb.org/3/movie/";
    public static String VIDEO_URL_2;

    public static final String TAG ="YOUTUBE";
    public static final String YOUTUBE_URL="https://www.youtube.com/watch?v=";
    Movie movie;
    String trailer_id;
    //view objects:
    ImageView ivPoster;
    TextView tvTitle;
    TextView tvYear;
    RatingBar rvVoteAverage;
    TextView tvOverview;
    TextView tvPopularity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSecretResources(this);
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


        //fetch youtube video
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(VIDEO_URL_1+movie.getId()+VIDEO_URL_2, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG,"onSuccess");
                JSONObject jsonObject= json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    trailer_id = results.getJSONObject(0).getString("key");
                    Log.i(TAG,"Results: "+results);
                    Log.i(TAG,"Result key : "+trailer_id);
                    //listener
                    ivPoster.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(TAG,"clicked BDimage");
                            Intent intent = new Intent(MovieDetailsActivity.this,MovieTrailerActivity.class);
                            intent.putExtra("VIDEO_KEY",trailer_id);
                            startActivity(intent);
                        }
                    });

                } catch (JSONException e) {
                    Log.e(TAG,"json exception",e);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG,"onFailure");

            }
        });

    }
    //Hide Api Key
    public static void initSecretResources(Context context){
        VIDEO_URL_2= "/videos?api_key="+context.getResources().getString(R.string.movie_api_key)+"&language=en-US";
    }
}