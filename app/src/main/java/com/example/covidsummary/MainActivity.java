package com.example.covidsummary;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvContainer;
    private EditText etSearch;
    private TextView tvMarquee, tvTextSort;
    private LinearLayout tvButtonSort;
    private SwipeRefreshLayout srSwipe;
    private ProgressBar pgBar;
    private MainAdapter adapter;
    private boolean isASC = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayout();
        requestData();
    }

    private void initLayout() {
        rvContainer = findViewById(R.id.rv_container);
        etSearch = findViewById(R.id.input_search);
        tvButtonSort = findViewById(R.id.button_sort);
        tvTextSort = findViewById(R.id.text_sort);
        tvMarquee = findViewById(R.id.marquee_message);
        pgBar = findViewById(R.id.pgbar);
        srSwipe = findViewById(R.id.swipe_refresh);
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

        tvButtonSort.setOnClickListener( v -> sortingData());

        // click item callback here
        adapter.addOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void onClick(ItemModel itemModel) {
                showDetails(itemModel);
            }
        });

        srSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srSwipe.setRefreshing(false);
                adapter.removeAllItems();
                isASC = true;
                tvTextSort.setText("A-Z");
                requestData();
            }
        });
    }

    private void sortingData() {
        if (isASC) {
            isASC = false;
            tvTextSort.setText("Z-A");
        } else {
            isASC = true;
            tvTextSort.setText("A-Z");
        }
        adapter.sortData(isASC);
    }

    private void requestData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.covid19api.com/summary";
        pgBar.setVisibility(View.VISIBLE);
        tvMarquee.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pgBar.setVisibility(View.GONE);
                        tvMarquee.setVisibility(View.VISIBLE);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            setMarqueeText(jsonResponse.getJSONObject("Global"));
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            bindDataToView(gson.fromJson(jsonResponse.getJSONArray("Countries").toString(), ItemModel[].class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pgBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
    }

    private void setMarqueeText(JSONObject item) {
        tvMarquee.setText("  GLOBAL DATA : " + item.toString()
                .replace("{", "")
                .replace("}", "")
                .replace(",", " | ")
                .replace("\"", " ")
                .replace(":", " : "));
        tvMarquee.setSelected(true);
    }

    private void bindDataToView(ItemModel[] itemModels) {
        for (ItemModel item : itemModels) {
            adapter.addItem(item);
        }
    }

    private void showDetails(ItemModel item) {
        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomSheetStyleDialog);
        View contentDetailsView = LayoutInflater.from(this).inflate(R.layout.item_details, null);
        ((TextView)contentDetailsView.findViewById(R.id.country)).setText(item.getCountry());
        ((TextView)contentDetailsView.findViewById(R.id.date)).setText(item.getDate().toString());
        ((TextView)contentDetailsView.findViewById(R.id.details)).setText("Total Deaths     : " + formatNumber(item.getTotalDeaths())
            +"\nDeath Today     : " + formatNumber(item.getNewDeaths())
            +"\nTotal Confirmed : " + formatNumber(item.getTotalConfirmed())
            +"\nConfirmed Today : " + formatNumber(item.getNewConfirmed())
            +"\nTotal Recovered : " + formatNumber(item.getTotalRecovered())
            +"\nRecovered Today : " + formatNumber(item.getNewRecovered()));
        dialog.setContentView(contentDetailsView);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                FrameLayout bottomSheet = (FrameLayout)((BottomSheetDialog) dialog).findViewById(R.id.design_bottom_sheet);
                final BottomSheetBehavior behaviour = BottomSheetBehavior.from(bottomSheet);
                behaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        dialog.show();
    }

    private String formatNumber(int number) {
        return String.format("%,d",number).replace(',', '.');
    }
}