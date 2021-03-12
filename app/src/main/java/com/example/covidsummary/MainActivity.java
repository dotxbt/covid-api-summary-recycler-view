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
import android.widget.TextView;
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
    private TextView tvButtonSort, tvMarquee;
    private ProgressBar pgBar;
    private MainAdapter adapter;
    private boolean isASC = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvContainer = findViewById(R.id.rv_container);
        etSearch = findViewById(R.id.input_search);
        tvButtonSort = findViewById(R.id.button_sort);
        tvMarquee = findViewById(R.id.marquee_message);
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

        tvButtonSort.setOnClickListener( v -> {
            if (isASC) {
                isASC = false;
                tvButtonSort.setText("Z-A");
            } else {
                isASC = true;
                tvButtonSort.setText("A-Z");
            }
            adapter.sortData(isASC);
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
        tvMarquee.setVisibility(View.GONE);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pgBar.setVisibility(View.GONE);
                        tvMarquee.setVisibility(View.VISIBLE);
                        System.out.println("RESPONSE : " + response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response).getJSONObject("Global");
                            tvMarquee.setText("  GLOBAL DATA : " + jsonObject.toString()
                                    .replace("{", "")
                                    .replace("}", "")
                                    .replace(",", " | ")
                                    .replace("\"", " ")
                                    .replace(":", " : "));
                            tvMarquee.setSelected(true);
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