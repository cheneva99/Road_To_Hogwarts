package com.isep.roadtohogwarts.character.quiz;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import com.isep.roadtohogwarts.R;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class QuizFinishedFragment extends Fragment {

    private int score;
    private View inputView;

    public QuizFinishedFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("score", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult( String requestKey, @NonNull Bundle bundle) {
                score = bundle.getInt("score");
                TextView scoreResultTextView = inputView.findViewById(R.id.scoreResultTextView);
                TextView scoreCommentTextView = inputView.findViewById(R.id.scoreCommentTextView);
                TextView categoryTextView = inputView.findViewById(R.id.categoryTextView);
                GifImageView gifImageView = inputView.findViewById(R.id.gifImageView);
                categoryTextView.setText("Characters");
                scoreResultTextView.setText(score+"/10");
                if(score<8){
                    try {
                        GifDrawable gifDrawableFail = new GifDrawable(getResources(), R.drawable.failed_quiz);
                        gifImageView.setImageDrawable(gifDrawableFail);
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    scoreCommentTextView.setText(R.string.scoreComment);

                }
                else{
                    try {
                        GifDrawable gifDrawableSuccess = new GifDrawable(getResources(), R.drawable.quiz_success);
                        gifImageView.setImageDrawable(gifDrawableSuccess);
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    scoreCommentTextView.setText(R.string.congrats);

                }
                Button restartButton = inputView.findViewById(R.id.restartQuizButton);
                restartButton.setOnClickListener(view -> {
                    try {
                        Navigation.findNavController(view).navigateUp();
                    }
                    catch (Exception e){
                        Log.d("TAG", "onClick: "+e);
                    }
                });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inputView = inflater.inflate(R.layout.fragment_quiz_finished, container, false);

        return inputView;
    }

}