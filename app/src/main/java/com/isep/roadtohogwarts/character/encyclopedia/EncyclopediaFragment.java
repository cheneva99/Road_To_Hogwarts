package com.isep.roadtohogwarts.character.encyclopedia;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.isep.roadtohogwarts.character.CharactersStatus;
import com.isep.roadtohogwarts.character.Houses;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EncyclopediaFragment extends Fragment implements CharacterRecyclerAdapter.OnItemListener {

    private View inputFragmentView;
    private RequestQueue queue;
    private List<Character> characterList;
    private List<Character> filteredCharacterList;

    RecyclerView recyclerView;
    CharacterRecyclerAdapter characterRecyclerAdapter;
    EditText searchBar;
    private ArrayList<String> houseList;
    private String houseFilterType;
    private String statusFilterType;
    private Button clearButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inputFragmentView = inflater.inflate(R.layout.fragment_characters_encyclopedia, container, false);
        characterList = new ArrayList<>();
        recyclerView = (RecyclerView) inputFragmentView.findViewById(R.id.recyclerview);
        characterRecyclerAdapter = new CharacterRecyclerAdapter(characterList, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(recyclerView.getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        queue = Volley.newRequestQueue(container.getContext());
        searchBar = (EditText) inputFragmentView.findViewById(R.id.searchEditText);
        clearButton = inputFragmentView.findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClearButtonClick(view);
            }
        });


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
                if (s.length() != 0) {
                    for (Character character : characterList) {

                        if (character.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                            searchedCharacters.add(character);
                        }
                    }
                    updateRecyclerViewData(searchedCharacters);

                } else {
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

    public void updateRecyclerViewData(List<Character> characterList) {
        characterRecyclerAdapter = new CharacterRecyclerAdapter(characterList, this);
        recyclerView.setAdapter(characterRecyclerAdapter);
    }

    public void callApi() {

        String myUrl = "https://hp-api.herokuapp.com/api/characters";

        StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);


                            for (int i = 0; i < jsonArray.length(); i++) {
                                Character character = new Character(jsonArray.getJSONObject(i));

                                characterList.add(character);

                            }
                            recyclerView.setHasFixedSize(true);
                            houseFilterType = "All houses";
                            statusFilterType="All";
                            updateRecyclerViewData(characterList);


                            setFilterHouse(inputFragmentView);
                            setFilterStatus(inputFragmentView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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

    public void onItemClick(int position) {
        Bundle result = new Bundle();
        result.putInt("position", position);
        getParentFragmentManager().setFragmentResult("position", result);


        Navigation.findNavController(this.getView()).navigate(R.id.action_goto_character_details);
    }


    private void setFilterHouse(View inputFragmentView) {
        getAllHouses();

        List<String> houseType = new ArrayList<>();
        houseType.add("All houses");
        houseType.add("No house");

        for (Houses house: Houses.values())
        {
            houseType.add(house.toString());

        }
        Spinner spinner = inputFragmentView.findViewById(R.id.filterHouse);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.custom_spinner, houseType);
        adapter.setDropDownViewResource(R.layout.custom_spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long id) {
                String filterType = (String) parent.getItemAtPosition(pos);
                ((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                parent.getChildAt(0).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_100));
                houseFilterType = filterType;
                updateCharacterList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void getAllHouses() {
        houseList = new ArrayList<>();
        houseList.add("All houses");
        characterList.forEach(character -> {
            if (!containsCaseInsensitive(houseList, character.getHouse()) && !character.getHouse().equals("")) {
                houseList.add(character.getHouse());
            }
        });
        Collections.sort(houseList, String.CASE_INSENSITIVE_ORDER);
        houseList.add("No house");
    }

    private boolean containsCaseInsensitive(List<String> typeList, String type) {
        return typeList.stream().anyMatch(x -> x.equalsIgnoreCase(type));
    }

    private void getFilteredRecyclerViewData(List<Character> characterList, String filterType) {
        List<Character> newcharacterList = new ArrayList<>();

        houseFilterType = filterType;
         if(filterType.equals("No house")){
             characterList.forEach(character -> {
                if (character.getHouse().equalsIgnoreCase("")) {
                    newcharacterList.add(character);
                }
            });
        }
        else {

             characterList.forEach(character -> {
                if (character.getHouse().equalsIgnoreCase(filterType)) {
                    newcharacterList.add(character);
                }
                else if(filterType.equals(Houses.Hufflepuff.toString()) && character.getHouse().equalsIgnoreCase("Huffleluff")){
                    newcharacterList.add(character);

                }
                else if(filterType.equals(Houses.Slytherin.toString()) && character.getHouse().equalsIgnoreCase("Slythetin")){
                    newcharacterList.add(character);

                }
            });
        }

        filteredCharacterList =  newcharacterList;
    }


    private void getFilteredStatusRecyclerViewData(List<Character> characterList, String filterType) {
        statusFilterType = filterType;
        List<Character> newcharacterList = new ArrayList<>();

        characterList.forEach(character -> {

            if(filterType.equals(CharactersStatus.Student.toString())&& character.isStudent()){
                newcharacterList.add(character);

            }else if(filterType.equals(CharactersStatus.Staff.toString())&& character.isStaff()){
                newcharacterList.add(character);

            }else if(filterType.equals(CharactersStatus.Other.toString()) && !character.isStaff() && !character.isStudent())
           {
                newcharacterList.add(character);
            }
        });
        filteredCharacterList =  newcharacterList;
    }

    private void setFilterStatus(View inputFragmentView) {
        List<String> statusType = new ArrayList<>();

        for (CharactersStatus characterStatus: CharactersStatus.values())
        {
            statusType.add(characterStatus.toString());

        }

        Spinner spinner = inputFragmentView.findViewById(R.id.filterStatus);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.custom_spinner, statusType);
        adapter.setDropDownViewResource(R.layout.custom_spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long id) {
                String filterType = (String) parent.getItemAtPosition(pos);
                ((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                parent.getChildAt(0).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_100));

              statusFilterType = filterType;
              updateCharacterList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void updateCharacterList(){
        filteredCharacterList = characterList;
        clearButton.setVisibility(View.VISIBLE);

       if(!houseFilterType.equals("All houses")&&!statusFilterType.equals(CharactersStatus.All.toString())) {

            getFilteredRecyclerViewData(filteredCharacterList, houseFilterType);

            getFilteredStatusRecyclerViewData(filteredCharacterList, statusFilterType);


        }
            else if(!houseFilterType.equals("All houses")||!statusFilterType.equals(CharactersStatus.All.toString())) {
           if (!houseFilterType.equals("All houses")) {

               getFilteredRecyclerViewData(filteredCharacterList, houseFilterType);

           }
           if (!statusFilterType.equals(CharactersStatus.All.toString())) {

               getFilteredStatusRecyclerViewData(filteredCharacterList, statusFilterType);
           }

       }
        else {
           clearButton.setVisibility(View.INVISIBLE);

           filteredCharacterList = characterList;
        }
        updateRecyclerViewData(filteredCharacterList);



    }

    public void onClearButtonClick(View v){
      statusFilterType=CharactersStatus.All.toString();
      houseFilterType="All houses";
      updateCharacterList();
      setFilterHouse(inputFragmentView);
      setFilterStatus(inputFragmentView);

    }
}