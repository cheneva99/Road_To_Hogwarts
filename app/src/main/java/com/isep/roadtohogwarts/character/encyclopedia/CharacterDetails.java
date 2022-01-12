
package com.isep.roadtohogwarts.character.encyclopedia;
 import android.os.Bundle;

        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;


        import androidx.annotation.NonNull;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentResultListener;

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
        import com.squareup.picasso.Picasso;

        import org.json.JSONArray;
        import org.json.JSONException;


public class CharacterDetails extends Fragment  {

    private View inputFragmentView;
    private RequestQueue queue;

    public Character currentCharacter;
    private  TextView gender;
    private  TextView house;
    private  TextView status;
    private  TextView species;
    private  TextView actor;
    private TextView patronus;
    private  TextView name;
    private ImageView characterImage;
    private String characterName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("characterName", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(String requestKey, @NonNull Bundle bundle) {

                characterName = bundle.getString("characterName");
                species = inputFragmentView.findViewById(R.id.species);
                house = inputFragmentView.findViewById(R.id.house);
                actor = inputFragmentView.findViewById(R.id.actorName);
                patronus = inputFragmentView.findViewById(R.id.patronus);
                name = inputFragmentView.findViewById(R.id.characterName);
                status = inputFragmentView.findViewById(R.id.status);
                characterImage = inputFragmentView.findViewById(R.id.characterCard);
                gender = inputFragmentView.findViewById(R.id.gender);

                queue = Volley.newRequestQueue(getContext());
                callApi(characterName);
            }
        });
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inputFragmentView = inflater.inflate(R.layout.fragment_character_details, container, false);

        return inputFragmentView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public void callApi(String characterName){

        String myUrl = "https://hp-api.herokuapp.com/api/characters";

        StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray jsonArray = new JSONArray(response);


                            for (int i = 0; i < jsonArray.length(); i++) {
                                if(jsonArray.getJSONObject(i).getString("name").equals(characterName)){
                                    currentCharacter = new Character(jsonArray.getJSONObject(i));
                                }
                            }

                            gender.setText(currentCharacter.getGender());
                            house.setText(currentCharacter.getHouse());
                            name.setText(currentCharacter.getName());
                            patronus.setText(currentCharacter.getPatronus());
                            status.setText(currentCharacter.getStatus());
                            species.setText(currentCharacter.getSpecies());
                            actor.setText(currentCharacter.getActorName());
                            setImage();

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

public void setImage() {
    try {
        String url = currentCharacter.getImageUrl();

        if(!url.equals("") ) {
            Picasso.get().load(url).resize(150,200).into(characterImage);
        }else{
            Picasso.get().load(R.drawable.background).resize(150,200).into(characterImage);

        }

    }catch(Exception e){
        Log.d("image", "onBindViewHolder: "+e);



    }
}



}