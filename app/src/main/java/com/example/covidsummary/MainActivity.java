package com.example.covidsummary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvContainer;
    private EditText etSearch;
    private ProgressBar pgBar;
    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvContainer = findViewById(R.id.rv_container);
        etSearch = findViewById(R.id.input_search);
        pgBar = findViewById(R.id.pgbar);
        adapter = new MainAdapter(this);
        rvContainer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvContainer.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // click item callback here
        adapter.addOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void onClick(ItemModel itemModel) {

                Toast.makeText(MainActivity.this, "ITEM : " + itemModel.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // request
        requestData();
    }

    private void requestData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.covid19api.com/summary";
        pgBar.setVisibility(View.VISIBLE);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pgBar.setVisibility(View.GONE);
                        System.out.println("RESPONSE : " + response.toString());
                        try {
                            JSONArray array = new JSONObject(response).getJSONArray("Countries");
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            bindDataToView(gson.fromJson(array.toString(), ItemModel[].class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pgBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Ups!!! Something error", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
    }

    private void bindDataToView(ItemModel[] itemModels) {
        for (ItemModel item : itemModels) {
            adapter.addItem(item);
        }
    }


}