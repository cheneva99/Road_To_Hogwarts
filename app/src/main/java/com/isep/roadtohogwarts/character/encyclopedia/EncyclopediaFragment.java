package com.isep.roadtohogwarts.character.encyclopedia;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.isep.roadtohogwarts.Character;
import com.isep.roadtohogwarts.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class EncyclopediaFragment extends Fragment implements CharacterRecyclerAdapter.OnItemListener {

    private View inputFragmentView;
    private RequestQueue queue;
    private List<Character> characterList;
    RecyclerView recyclerView;
   CharacterRecyclerAdapter characterRecyclerAdapter;
    EditText searchBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inputFragmentView = inflater.inflate(R.layout.fragment_characters_encyclopedia, container, false);


        characterList=new ArrayList<>();
        recyclerView = (RecyclerView) inputFragmentView.findViewById(R.id.recyclerview);

        characterRecyclerAdapter= new CharacterRecyclerAdapter(characterList,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(recyclerView.getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        queue= Volley.newRequestQueue(container.getContext());
        searchBar = (EditText)inputFragmentView.findViewById(R.id.searchEditText);

        searchBar.addTextChangedListener(new TextWatcher() {
            List<Character> searchedCharacters = new ArrayList<>();


            @Override
            public void afterTextChanged(Editable s) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                searchedCharacters.clear();
                if(s.length() != 0){
                    for (Character character : characterList) {

                        if (character.getName().toLowerCase().contains(s.toString().toLowerCase())) {

                            searchedCharacters.add(character);


                        }else if(character.getHouse().toLowerCase().contains(s.toString().toLowerCase())){
                            searchedCharacters.add(character);
                        }
                    }
                   updateRecyclerViewData(searchedCharacters);

                }
                else{
                    updateRecyclerViewData(characterList);



                }
            }
        });
        callApi();
        return inputFragmentView;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void updateRecyclerViewData(List<Character> characterList){
        characterRecyclerAdapter= new CharacterRecyclerAdapter(characterList,this);
        recyclerView.setAdapter(characterRecyclerAdapter);
    }

    public void callApi(){

        String myUrl = "https://hp-api.herokuapp.com/api/characters";

        StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray jsonArray = new JSONArray(response);


                            for (int i = 0; i < jsonArray.length(); i++) {
                                Character character = new Character(jsonArray.getJSONObject(i));

                                characterList.add(character);

                            }
                            recyclerView.setHasFixedSize(true);

                           updateRecyclerViewData(characterList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }}, new Response.ErrorListener() {
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
    public void onItemClick(int position) {
        Log.d("TAG", "onItemClick: "+"OUI");
        Bundle result = new Bundle();
        result.putInt("position", position);
        getParentFragmentManager().setFragmentResult("position", result);


        Navigation.findNavController(this.getView()).navigate(R.id.action_goto_character_details);

        //Navigation.findNavController(v).navigate(R.id.action_quiz_ended);

    }




}