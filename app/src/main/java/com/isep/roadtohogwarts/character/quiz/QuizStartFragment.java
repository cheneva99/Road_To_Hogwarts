package com.isep.roadtohogwarts.character.quiz;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.isep.roadtohogwarts.R;


public class QuizStartFragment extends Fragment {


    public QuizStartFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        View inputFragmentView = inflater.inflate(R.layout.fragment_quiz_start, container, false);


        Button button = inputFragmentView.findViewById(R.id.startQuizButton);

        TextView textView =  inputFragmentView.findViewById(R.id.introductionTextView);
        textView.setText("How much do you know about spells ?");




        button.setOnClickListener(view -> {

            try {
               Navigation.findNavController(view).navigate(R.id.action_quiz_started);

            }
            catch (Exception e){
                Log.d("TAG", "onClick: "+e);
            }


        });

        return inputFragmentView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}