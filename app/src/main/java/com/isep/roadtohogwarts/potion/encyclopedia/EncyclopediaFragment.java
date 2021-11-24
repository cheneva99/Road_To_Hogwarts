package com.isep.roadtohogwarts.potion.encyclopedia;

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
import com.isep.roadtohogwarts.Potion;
import com.isep.roadtohogwarts.R;
import com.isep.roadtohogwarts.databinding.FragmentPotionsEncyclopediaBinding;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class EncyclopediaFragment extends Fragment {

    private View inputFragmentView;
    private RequestQueue queue;
    private List<Potion> potionList;
    RecyclerView recyclerView;
    PotionRecyclerAdapter potionRecyclerAdapter;
    EditText searchBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inputFragmentView = inflater.inflate(R.layout.fragment_potions_encyclopedia, container, false);


        potionList=new ArrayList<>();
        recyclerView = (RecyclerView) inputFragmentView.findViewById(R.id.recyclerview);

        potionRecyclerAdapter= new PotionRecyclerAdapter(potionList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        queue= Volley.newRequestQueue(container.getContext());
        searchBar = (EditText)inputFragmentView.findViewById(R.id.searchEditText);

        searchBar.addTextChangedListener(new TextWatcher() {
            List<Potion> searchedPotions = new ArrayList<>();


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
                searchedPotions.clear();
                if(s.length() != 0){
                    for (Potion potion : potionList) {

                        if (potion.getName().toLowerCase().contains(s.toString().toLowerCase())) {

                            searchedPotions.add(potion);


                        }else if(potion.getDescription().toLowerCase().contains(s.toString().toLowerCase())){
                            searchedPotions.add(potion);
                        }
                    }
                   updateRecyclerViewData(searchedPotions);

                }
                else{
                    updateRecyclerViewData(potionList);



                }
            }
        });
        callApi("potions");
        return inputFragmentView;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void updateRecyclerViewData(List<Potion> potionList){
        potionRecyclerAdapter= new PotionRecyclerAdapter(potionList);
        recyclerView.setAdapter(potionRecyclerAdapter);
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
                                Potion potion = new Potion(jsonArray.getJSONObject(i));

                                potionList.add(potion);

                            }
                            recyclerView.setHasFixedSize(true);

                           updateRecyclerViewData(potionList);

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