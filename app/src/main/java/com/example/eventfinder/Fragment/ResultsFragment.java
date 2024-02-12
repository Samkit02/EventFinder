package com.example.eventfinder.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.eventfinder.Adapter.ResultsAdapter;
import com.example.eventfinder.Client.RetrofitClient;
import com.example.eventfinder.Listener.OnFragmentInteractionListener;
import com.example.eventfinder.MainActivity;
import com.example.eventfinder.Model.Event;
import com.example.eventfinder.R;
import com.example.eventfinder.Util.EventSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultsFragment extends Fragment implements ResultsAdapter.ItemClicks {

    private List<Event> events;
    ResultsAdapter resultsAdapter;
    private EventSpinner eventSpinner;
    private Context mContext;
    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private Button back;
    public ResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_results, container, false);
        recyclerView = view.findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        events = new ArrayList<>();
        resultsAdapter = new ResultsAdapter(events, mContext, this);
        recyclerView.setAdapter(resultsAdapter);

        back = view.findViewById(R.id.back1);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.changeFragment(2);
            }
        });

        //getEvents();
        return view;
    }

    private void getEvents() {
        showProgress(true);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

        Map<String, String> data = new HashMap<>();


        data.put("keyword", preferences.getString("keyword", null));
        data.put("dist", preferences.getString("distance", null));
        data.put("CAT", preferences.getString("category", null));
        data.put("LAT", preferences.getString("latitude", null));
        data.put("LONG", preferences.getString("longitude", null));

        Call<List<Event>> apiCall = RetrofitClient.getInstance().getApIs().getEventsTable(data);
        Log.e("apicall", apiCall.toString());
        apiCall.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(@NonNull Call<List<Event>> call, @NonNull Response<List<Event>> response) {
                showProgress(false);
                List<Event> event = response.body();
                setAdapter(event);
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter(List<Event> event) {
        resultsAdapter = new ResultsAdapter(event, mContext, this);
        resultsAdapter.notifyDataSetChanged();
    }

    public void showProgress(boolean show){
        if(eventSpinner==null)
            eventSpinner = new EventSpinner();

        if(show)
            eventSpinner.show(getChildFragmentManager(), MainActivity.class.getName());
        else
            eventSpinner.dismissAllowingStateLoss();

    }

    @Override
    public void likeClick(int position) {

    }

    @Override
    public void dislikeClick(int position) {

    }
}