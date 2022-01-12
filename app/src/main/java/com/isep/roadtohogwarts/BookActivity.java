package com.isep.roadtohogwarts;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity  {

    private final String TAG = this.getClass().getSimpleName();
    private RequestQueue queue;
    private List<Book> library;
    RecyclerView recyclerView;
    BookRecyclerAdapter bookRecyclerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Books");
        actionBar.setDisplayShowHomeEnabled(true);
        library=new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        bookRecyclerAdapter= new BookRecyclerAdapter(library);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(recyclerView.getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        queue= Volley.newRequestQueue(BookActivity.this);
        callApi("books");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_items, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                                 Book book = new Book(jsonArray.getJSONObject(i));

                                library.add(book);
                            }
                            recyclerView.setAdapter(bookRecyclerAdapter);
                            bookRecyclerAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error at sign in : " + error.getMessage());
            }
        });
        int socketTimeout = 30000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myRequest.setRetryPolicy(retryPolicy);
        queue.add(myRequest);

    }

}