package com.isep.roadtohogwarts.potion.quiz;

import static com.isep.roadtohogwarts.R.*;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.isep.roadtohogwarts.Potion;
import com.isep.roadtohogwarts.Question;
import com.isep.roadtohogwarts.Quiz;
import com.isep.roadtohogwarts.R;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class QuizStartedFragment extends Fragment {
    private View inputFragmentView;
    private RequestQueue queue;
    private List<Potion> potionList;
    private Quiz quiz;
    private RadioButton answer1RadioButton;
    private RadioButton answer2RadioButton;
    private RadioButton answer3RadioButton;
    private int checkedRadioId;

    public QuizStartedFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        inputFragmentView = inflater.inflate(layout.fragment_quiz, container, false);
        potionList=new ArrayList<>();
         answer1RadioButton = inputFragmentView.findViewById(R.id.answer1);
         answer2RadioButton = inputFragmentView.findViewById(R.id.answer2);
         answer3RadioButton = inputFragmentView.findViewById(R.id.answer3);
        checkedRadioId =-1;
        queue= Volley.newRequestQueue(container.getContext());
        Button button = inputFragmentView.findViewById(R.id.submitButton);
        RadioGroup radioGroup = inputFragmentView.findViewById(R.id.radiogroup);
        callApi("potions");

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            switch (radioGroup.getCheckedRadioButtonId()) {
                case id.answer1:
                    checkedRadioId = 0;
                    break;
                case id.answer2:
                    checkedRadioId = 1;
                    break;
                case id.answer3:
                    checkedRadioId = 2;
                    break;
            }


        });

        button.setOnClickListener(new View.OnClickListener() {
            TextView errorTextView = inputFragmentView.findViewById(R.id.errorTextView);
            public void onClick(View v) {
                errorTextView.setVisibility(View.INVISIBLE);
                if (quiz.getQuestionNumber() < 10 && checkedRadioId!=-1) {
                    if (checkedRadioId == quiz.getCurrentQuestion().getCorrectAnswer()) {
                        quiz.addPoints();
                    }

                   radioGroup.clearCheck();
                    checkedRadioId=-1;

                    quiz.nextQuestion();
                    setQuestionView(quiz);


                }
                else if(checkedRadioId==-1){
                    errorTextView.setVisibility(View.VISIBLE);

                }
                else if(quiz.getQuestionNumber() == 10){
                    try {

                        Bundle result = new Bundle();
                        result.putInt("score", quiz.getScore());
                        getParentFragmentManager().setFragmentResult("score", result);


                        Navigation.findNavController(v).navigate(id.action_quiz_ended);

                    }
                    catch (Exception e){
                        Log.d("TAG", "onClick: "+e);
                    }
                }
            }

        });


        return inputFragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    public void callApi(String fragment){
        String myUrl = String.format("https://the-harry-potter-database.herokuapp.com/api/1/%1$s/all",
                fragment);

        StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                response -> {
                    try{
                        JSONArray jsonArray = new JSONArray(response);


                        for (int i = 0; i < jsonArray.length(); i++) {
                            Potion potion = new Potion(jsonArray.getJSONObject(i));

                            potionList.add(potion);

                        }
                        List<Potion> questionAsked = new ArrayList();
                        List<Question> questionList =  new ArrayList<>();

                        for (int i = 0; i < 10; i++) {
                            List<Potion> quizPotionsChoices = new ArrayList<>();
                            Random random = new Random();
                            while(quizPotionsChoices.size()<3){
                                int randomNumber = random.nextInt(potionList.size() - 1);
                                if(!quizPotionsChoices.contains(potionList.get(randomNumber))||!questionList.contains(potionList.get(randomNumber))||potionList.get(randomNumber).getDescription()!=null){
                                    quizPotionsChoices.add(potionList.get(randomNumber));
                                }

                            }
                            int answerRandomNumber = random.nextInt(3 - 1);
                            Question question = new Question(quizPotionsChoices.get(answerRandomNumber).getDescription(),quizPotionsChoices.get(0).getName(),quizPotionsChoices.get(1).getName(),quizPotionsChoices.get(2).getName(),answerRandomNumber);
                            questionList.add(question);
                            questionAsked.add(potionList.get(answerRandomNumber));


                        }
                        quiz = new Quiz(questionList);
                        setQuestionView(quiz);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Log.e(TAG, "Error at sign in : " + error.getMessage());
            }
        });
        int socketTimeout = 30000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myRequest.setRetryPolicy(retryPolicy);
        queue.add(myRequest);



    }



    public void setQuestionView(Quiz quiz){
        TextView questionNumberTextView =inputFragmentView.findViewById(R.id.questionNumberTextView);
        TextView categoryTextView =inputFragmentView.findViewById(R.id.categoryTextView);

        TextView questionTextView =inputFragmentView.findViewById(R.id.questionTextView);

        questionNumberTextView.setText(""+quiz.getQuestionNumber()+"/10");
        categoryTextView.setText("Potions");
        questionTextView.setText("Which potion match this description: " +quiz.getQuestionList().get(quiz.getQuestionNumber()-1).getQuestion());
        answer1RadioButton.setText(quiz.getQuestionList().get(quiz.getQuestionNumber()-1).getAnswer1());
        answer2RadioButton.setText(quiz.getQuestionList().get(quiz.getQuestionNumber()-1).getAnswer2());
        answer3RadioButton.setText(quiz.getQuestionList().get(quiz.getQuestionNumber()-1).getAnswer3());


    }




}

