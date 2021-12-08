package com.isep.roadtohogwarts.spell.quiz;

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
                // We use a String here, but any type that can be put in a Bundle is supported
                score = bundle.getInt("score");
                // Do something with the result
                Log.d("TAG", "onFragmentResult: "+score);
                TextView scoreResultTextView = inputView.findViewById(R.id.scoreResultTextView);
                TextView scoreCommentTextView = inputView.findViewById(R.id.scoreCommentTextView);
                TextView categoryTextView = inputView.findViewById(R.id.categoryTextView);
                categoryTextView.setText("Spells");
                scoreResultTextView.setText(score+"/10");
                if(score<8){
                    scoreCommentTextView.setText(R.string.scoreComment);

                }
                else{
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