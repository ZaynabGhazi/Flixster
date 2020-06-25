package com.example.flixter.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.flixter.GlideApp;
import com.example.flixter.MovieDetailsActivity;
import com.example.flixter.R;
import com.example.flixter.models.Movie;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }
    //inflate layout and return it inside holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter","onCreateVH");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie,parent,false);
        return new ViewHolder(movieView);
    }
    //populate data into view through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    //get movie at p
        Log.d("MovieAdapter","onBindVH "+position);
        Movie movie = movies.get(position);
        //bind movie data to VH
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    //inner viewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageURL;
            int placeholder_id;
            //landscape-dependent image display
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageURL = movie.getBackdropPath();
                placeholder_id = R.drawable.flicks_backdrop_placeholder;
            }
            else {
                imageURL = movie.getPosterPath();
                placeholder_id = R.drawable.movie_placeholder;
            }
            //render image:
            GlideApp.with(context).load(imageURL).transform(new RoundedCornersTransformation(30, 1)).placeholder(placeholder_id).into(ivPoster);



        }

        @Override
        public void onClick(View view) {
            //show movie-details of selected movie
            int position = this.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION){
                Movie movie = movies.get(position);
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                //extraKey = name of class/Value = movie
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                //launch new activity
                context.startActivity(intent);
            }
        }
    }
}
