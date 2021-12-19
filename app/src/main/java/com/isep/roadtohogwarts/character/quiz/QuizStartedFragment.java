package com.isep.roadtohogwarts.character.quiz;

import static com.isep.roadtohogwarts.R.id;
import static com.isep.roadtohogwarts.R.layout;

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
import com.isep.roadtohogwarts.Character;
import com.isep.roadtohogwarts.Question;
import com.isep.roadtohogwarts.Quiz;
import com.isep.roadtohogwarts.R;
import com.isep.roadtohogwarts.Spell;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class QuizStartedFragment extends Fragment {
    private View inputFragmentView;
    private RequestQueue queue;
    private List<Character> characterList;
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
        characterList = new ArrayList<>();
        answer1RadioButton = inputFragmentView.findViewById(id.answer1);
        answer2RadioButton = inputFragmentView.findViewById(id.answer2);
        answer3RadioButton = inputFragmentView.findViewById(id.answer3);
        checkedRadioId = -1;
        queue = Volley.newRequestQueue(container.getContext());
        Button button = inputFragmentView.findViewById(id.submitButton);
        RadioGroup radioGroup = inputFragmentView.findViewById(id.radiogroup);
        callApi("characters");

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
            TextView errorTextView = inputFragmentView.findViewById(id.errorTextView);
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
                    if (checkedRadioId == quiz.getCurrentQuestion().getCorrectAnswer()) {
                        quiz.addPoints();
                    }
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
        String myUrl = String.format("http://hp-api.herokuapp.com/api/%1$s",
                fragment);

        StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                response -> {
                    try{
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Character character = new Character(jsonArray.getJSONObject(i));
                            if (!character.getHouse().equals("") || !character.getPatronus().equals("")) {
                                characterList.add(character);
                            }
                        }
                        generateQuestion();
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

    public void generateQuestion() {
        List<Character> questionAsked = new ArrayList();
        List<Question> questionList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            Question question;
            int answerRandomNumber = random.nextInt(3 - 1);
            int randomTypeNumber = random.nextBoolean() ? 0 : 1;

            if(randomTypeNumber == 0) {
                question = generateHouseQuestion(answerRandomNumber);
            } else {
                question = generatePatronusQuestion(answerRandomNumber);
            }

            questionList.add(question);
            questionAsked.add(characterList.get(answerRandomNumber));
        }
        quiz = new Quiz(questionList);
    }
    public Question generateHouseQuestion(int answerRandomNumber){
        List<Character> quizCharactersChoices = new ArrayList<>();
        Random random = new Random();
        List<String> houseTypes = new ArrayList<>();

        while (quizCharactersChoices.size() < 3) {
            int randomNumber = random.nextInt(characterList.size() - 1);

            if (!characterList.get(randomNumber).getHouse().equals("")) {
                if (!quizCharactersChoices.contains(characterList.get(randomNumber)) || !characterList.contains(characterList.get(randomNumber))) {
                    if (!houseTypes.contains(characterList.get(randomNumber).getHouse())) {
                        quizCharactersChoices.add(characterList.get(randomNumber));
                        houseTypes.add(characterList.get(randomNumber).getHouse());
                    }
                }
            }
        }

        Question question = new Question(quizCharactersChoices.get(answerRandomNumber).getName(), quizCharactersChoices.get(0).getHouse(), quizCharactersChoices.get(1).getHouse(), quizCharactersChoices.get(2).getHouse(), answerRandomNumber);
        question.setType("House");
        return question;
    }

    public Question generatePatronusQuestion(int answerRandomNumber){
        List<Character> quizCharactersChoices = new ArrayList<>();
        Random random = new Random();

        while (quizCharactersChoices.size() < 3) {
            int randomNumber = random.nextInt(characterList.size() - 1);

            if (!characterList.get(randomNumber).getPatronus().equals("")) {
                if (!quizCharactersChoices.contains(characterList.get(randomNumber)) || !characterList.contains(characterList.get(randomNumber)) ) {
                        quizCharactersChoices.add(characterList.get(randomNumber));
                }
            }
        }

        Question question = new Question(quizCharactersChoices.get(answerRandomNumber).getName(), quizCharactersChoices.get(0).getPatronus(), quizCharactersChoices.get(1).getPatronus(), quizCharactersChoices.get(2).getPatronus(), answerRandomNumber);
        question.setType("Patronus");
        return question;
    }

    public void setQuestionView(Quiz quiz){
        TextView questionNumberTextView =inputFragmentView.findViewById(id.questionNumberTextView);
        TextView categoryTextView =inputFragmentView.findViewById(id.categoryTextView);

        TextView questionTextView =inputFragmentView.findViewById(id.questionTextView);

        questionNumberTextView.setText("" + quiz.getQuestionNumber()+"/10");
        categoryTextView.setText("Characters");
        if (quiz.getQuestionList().get(quiz.getQuestionNumber()-1).getType().equals("House")) {
            questionTextView.setText("Which house is " + quiz.getQuestionList().get(quiz.getQuestionNumber()-1).getQuestion() + " in:");
        } else {
            questionTextView.setText("What is the patronus of " + quiz.getQuestionList().get(quiz.getQuestionNumber()-1).getQuestion() + ":");
        }
        answer1RadioButton.setText(quiz.getQuestionList().get(quiz.getQuestionNumber()-1).getAnswer1());
        answer2RadioButton.setText(quiz.getQuestionList().get(quiz.getQuestionNumber()-1).getAnswer2());
        answer3RadioButton.setText(quiz.getQuestionList().get(quiz.getQuestionNumber()-1).getAnswer3());
    }

}

