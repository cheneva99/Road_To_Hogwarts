package com.isep.roadtohogwarts.spell.encyclopedia;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.isep.roadtohogwarts.Spell;
import com.isep.roadtohogwarts.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class EncyclopediaFragment extends Fragment {

    private View inputFragmentView;
    private RequestQueue queue;
    private List<Spell> spellList;
    RecyclerView recyclerView;
    SpellRecyclerAdapter spellRecyclerAdapter;
    EditText searchBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inputFragmentView = inflater.inflate(R.layout.fragment_spells_encyclopedia, container, false);


       spellList=new ArrayList<>();
        recyclerView = (RecyclerView) inputFragmentView.findViewById(R.id.recyclerview);

        spellRecyclerAdapter= new SpellRecyclerAdapter(spellList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        queue= Volley.newRequestQueue(container.getContext());
        searchBar = (EditText)inputFragmentView.findViewById(R.id.searchEditText);

        searchBar.addTextChangedListener(new TextWatcher() {
            List<Spell> searchedSpells = new ArrayList<>();


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
                searchedSpells.clear();
                if(s.length() != 0){
                    for (Spell spell : spellList) {

                        if (spell.getName().toLowerCase().contains(s.toString().toLowerCase())) {

                            searchedSpells.add(spell);


                        }else if(spell.getDescription().toLowerCase().contains(s.toString().toLowerCase())){
                            searchedSpells.add(spell);
                        } else if (spell.getType().toLowerCase().contains(s.toString().toLowerCase())) {

                            searchedSpells.add(spell);


                        }else if(spell.getDescription().toLowerCase().contains(s.toString().toLowerCase())){
                            searchedSpells.add(spell);
                        }
                    }
                   updateRecyclerViewData(searchedSpells);

                }
                else{
                    updateRecyclerViewData(spellList);



                }
            }
        });
        callApi("spells");
        return inputFragmentView;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void updateRecyclerViewData(List<Spell> spellList){
        spellRecyclerAdapter= new SpellRecyclerAdapter(spellList);
        recyclerView.setAdapter(spellRecyclerAdapter);
    }

    public void callApi(String fragment){
        String myUrl = String.format("https://the-harry-potter-database.herokuapp.com/api/1/%1$s/all",
                fragment);

        StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray jsonArray = new JSONArray(response);


                            for (int i = 0; i < jsonArray.length(); i++) {
                                Spell spell = new Spell(jsonArray.getJSONObject(i));

                                spellList.add(spell);

                            }
                            recyclerView.setHasFixedSize(true);

                           updateRecyclerViewData(spellList);

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



}