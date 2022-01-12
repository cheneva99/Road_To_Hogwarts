package com.isep.roadtohogwarts.spell.encyclopedia;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
import java.util.Collections;
import java.util.List;

public class EncyclopediaFragment extends Fragment {

    private View inputFragmentView;
    private RequestQueue queue;
    private List<Spell> spellList;
    private ArrayList<String> typeList;
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

        spellList = new ArrayList<>();
        recyclerView = inputFragmentView.findViewById(R.id.recyclerview);

        spellRecyclerAdapter = new SpellRecyclerAdapter(spellList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        queue = Volley.newRequestQueue(container.getContext());
        searchBar = inputFragmentView.findViewById(R.id.searchEditText);

        searchBar.addTextChangedListener(new TextWatcher() {
            final List<Spell> searchedSpells = new ArrayList<>();

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
        callApi("spells", inputFragmentView);
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

    public void callApi(String fragment, View inputFragmentView){
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
                            setFilter(inputFragmentView);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley error", "Error at sign in : " + error.getMessage());
            }
        });

        int socketTimeout = 30000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myRequest.setRetryPolicy(retryPolicy);
        queue.add(myRequest);
    }

    private void setFilter(View inputFragmentView) {
        getAllTypes();

        Spinner spinner = inputFragmentView.findViewById(R.id.filter);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.custom_spinner, typeList);
        adapter.setDropDownViewResource(R.layout.custom_spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long id) {
                String filterType = (String) parent.getItemAtPosition(pos);
                ((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                parent.getChildAt(0).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_100));

                if (filterType.equals("All types")) {
                    updateRecyclerViewData(spellList);
                } else {
                    List<Spell> newSpellList = new ArrayList<>();
                    if (filterType.equals(("No type"))) {
                        getFilteredRecyclerViewData(newSpellList, "null");
                    } else {
                        getFilteredRecyclerViewData(newSpellList, filterType);
                    }
                    updateRecyclerViewData(newSpellList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void getAllTypes(){
        typeList = new ArrayList<>();
        typeList.add("All types");
        spellList.forEach(spell -> {
            if(spell.getType().contains(",")){
                String[] spellsList = spell.getType().split(",");
                for (String spellInArray : spellsList) {
                    if (spellInArray.equals("null")) {
                        break;
                    }
                    if (!containsCaseInsensitive(typeList, spellInArray.trim()) && !spellInArray.trim().equals("null")) {
                        typeList.add(spellInArray.trim());
                    }
                }
            }
            else if(!containsCaseInsensitive(typeList, spell.getType()) && !spell.getType().equals("null")){
                typeList.add(spell.getType());
            }
        });
        Collections.sort(typeList, String.CASE_INSENSITIVE_ORDER);
        typeList.add("No type");
    }

    private boolean containsCaseInsensitive(List<String> typeList, String type){
        return typeList.stream().anyMatch(x -> x.equalsIgnoreCase(type));
    }

    private void getFilteredRecyclerViewData(List<Spell> newSpellList, String filterType) {
        spellList.forEach(spell -> {
            if(spell.getType().contains(",")){
                String[] spellsList = spell.getType().split(",");
                for (String spellInArray : spellsList) {
                    String filterString = spellInArray.trim();
                    if (filterString.equalsIgnoreCase(filterType)) {
                        newSpellList.add(spell);
                    }
                }
            }
            else if(spell.getType().equalsIgnoreCase(filterType)){
                newSpellList.add(spell);
            }
        });

        updateRecyclerViewData(newSpellList);
    }
}