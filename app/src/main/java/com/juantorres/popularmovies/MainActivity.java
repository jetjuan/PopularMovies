package com.juantorres.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.juantorres.popularmovies.utils.JSONUtils;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.iv_test)
    public ImageView imageView;

    @BindView(R.id.tv_test)
    public TextView textViewTest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This was missing... and I wasted 2 hours wondering why my code didn't work :(
        ButterKnife.bind(this);

    }

    @OnClick(R.id.iv_test) public void picassoTest(){
        Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();

        Picasso.with(this).load("http://i.imgur.com/DvpvklR.png").into(imageView);
        System.out.print("Method picasso working");
    }

    @OnClick(R.id.tv_test) public void test(){
        MoviesLoaderTask loader = new MoviesLoaderTask();
        String json = null;
        try {
            json = loader.execute().get();
        }catch (ExecutionException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

//        textViewTest.setText(json);
        //TODO complete the above
        try {
            textViewTest.setText(JSONUtils.getMoviePostersFromJSONString(json).get(0).getPosterPath());

        }catch (Exception e){

        }

    }


}
