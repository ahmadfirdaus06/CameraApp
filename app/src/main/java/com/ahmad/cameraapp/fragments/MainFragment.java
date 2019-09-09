package com.ahmad.cameraapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmad.cameraapp.R;
import com.ahmad.cameraapp.adapters.ReportAdapter;
import com.ahmad.cameraapp.config.Cache;
import com.ahmad.cameraapp.config.ConnectionCheck;
import com.ahmad.cameraapp.config.DataSource;
import com.ahmad.cameraapp.models.Report;
import com.ahmad.cameraapp.service.GetDataAsync;
import com.ahmad.cameraapp.service.LogAccessRequestAsync;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements LogAccessRequestAsync.AsyncResponse, GetDataAsync.AsyncResponse {

    private ConnectionCheck conn;
    private Cache cache;
    private Window window;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fab;
    private List<String> array= new ArrayList<String>();
    private AlertDialog alertAddReport, alertLogout;
    private LogAccessRequestAsync logAccessRequestAsync;
    private ProgressDialog progressDialog;
    private ReportAdapter adapter;
    private RecyclerView listReport;
    private RecyclerView.LayoutManager layoutManager;
    private TextView textNoReport;
    private GetDataAsync getDataAsync;
    private GetDataAsync.AsyncResponse getDataResponse;
    private JsonObjectRequest objectRequest;
    private RequestQueue requestQueue;
    private DataSource dataSource;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment,
                container, false);
    }

    public void onViewCreated (View view, Bundle savedInstanceState){
        dataSource = new DataSource();
        conn = new ConnectionCheck(getActivity());
        cache = new Cache(getActivity());
        toolbar = view.findViewById(R.id.toolbar);
        listReport = view.findViewById(R.id.list_report);
        swipeRefreshLayout = view.findViewById(R.id.refresh);
        fab = view.findViewById(R.id.fab);
        textNoReport = view.findViewById(R.id.text_no_report);
        setup();
    }

    private Toolbar.OnMenuItemClickListener menuSetup = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.logout){
                alertLogout = new AlertDialog.Builder(getActivity()).create();
                alertLogout.setMessage("Are you sure to logout?");
                alertLogout.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (conn.isOnline()){
                                    logout();
                                }
                                else{
                                    Toast.makeText(getActivity(), "No internet connection!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                alertLogout.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        });
                alertLogout.show();
            }
            else if(menuItem.getItemId() == R.id.user_profile){
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.container, editProfileFragment, "editProfileFragment")
                        .addToBackStack(null).commit();
            }
            return false;
        }
    };

    private SwipeRefreshLayout.OnRefreshListener refresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshData(1);
        }
    };

    private View.OnClickListener add = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            alertAddReport = new AlertDialog.Builder(getActivity()).create();
            alertAddReport.setMessage("File a new report?");
            alertAddReport.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (cache.removeAllReportCache()){
                                Step1Fragment step1Fragment = new Step1Fragment();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                        .replace(R.id.container, step1Fragment, "step1Fragment")
                                        .addToBackStack(null).commit();
                            }
                        }
                    });
            alertAddReport.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    });
            alertAddReport.show();
        }
    };

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if(dy > 0){
                fab.hide();
            } else{
                fab.show();
            }
        }
    };

    public void setup(){
        toolbar.setTitle("My Report");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(menuSetup);
        window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int color = ((ColorDrawable) toolbar.getBackground()).getColor();
        window.setStatusBarColor(color);
        swipeRefreshLayout.setOnRefreshListener(refresh);
        fab.setOnClickListener(add);
        listReport.addOnScrollListener(scrollListener);
        layoutManager = new LinearLayoutManager(getActivity());
        listReport.setLayoutManager(layoutManager);
        listReport.setItemAnimator(new DefaultItemAnimator());
        listReport.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        setListDetails();
        getDataResponse = this;
    }


    public void setListDetails(){
        adapter = new ReportAdapter(getActivity(), cache.getReport(), new ReportAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Report report) {
                ReportSummaryFragment reportSummaryFragment = new ReportSummaryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("report_id", report.getReport_id());
                reportSummaryFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right,
                        R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.container, reportSummaryFragment, "reportSummaryFragment")
                        .addToBackStack(null)
                        .commit();

            }
        });
        listReport.setAdapter(adapter);
        updateUI();
    }

    public void updateUI(){
        if (adapter != null && adapter.getItemCount() > 0){
            textNoReport.setVisibility(View.INVISIBLE);
            listReport.setVisibility(View.VISIBLE);
        }
        else{
            textNoReport.setVisibility(View.VISIBLE);
            listReport.setVisibility(View.INVISIBLE);
        }
    }

    public void refreshData(int type){
        if (type == 1){
            swipeRefreshLayout.setRefreshing(true);
        }
        else if (type == 0){
            swipeRefreshLayout.setRefreshing(false);
        }
        if (cache.getUserPrefCache() != null){
            String userId = cache.getUserPrefCache().getUserID();
            String url = dataSource.getGetAllReportsDataUrl();
            JSONObject object = new JSONObject();
            try {
                object.put("user_id", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (conn.isOnline()){
                requestQueue = Volley.newRequestQueue(getActivity());
                objectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String message = "";
                                try {
                                    message = response.getString("message");
                                    if (message.equals("Success")){
                                        JSONArray users = response.getJSONArray("userList");
                                        JSONArray students = response.getJSONArray("studentList");
                                        JSONArray reports = response.getJSONArray("reportList");
                                        JSONArray attachments = response.getJSONArray("attachmentList");
                                        JSONArray misconducts = response.getJSONArray("misconductList");
                                        getDataAsync = new GetDataAsync(getActivity());
                                        getDataAsync.setListener(getDataResponse).execute(users, students, reports, attachments, misconducts);
                                    }
                                } catch (JSONException e) {
                                    System.out.println(e);
                                    Toast.makeText(getActivity(), "Couln't fetch data, Try again later",Toast.LENGTH_SHORT).show();
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Couln't fetch data, Try again later",Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

                requestQueue.add(objectRequest);
            }
            else{
                Toast.makeText(getActivity(), "Couln't fetch data, Try again later",Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    public void redirect(){
        if (cache.getUserPrefCache() == null){
            LoginFragment loginFragment = new LoginFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, loginFragment).commit();
        }
    }

    public void logout(){
        logAccessRequestAsync = new LogAccessRequestAsync(getActivity());
        logAccessRequestAsync.setListener(this).execute();
    }

    @Override
    public void onResume(){
        super.onResume();
        redirect();
        refreshData(0);
    }

    @Override
    public void processFinish(boolean response) {
        if (response){
            if(cache.removeAllData()){
                redirect();
            }

        }else{
            Toast.makeText(getActivity(), "Logout failed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void start() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading. Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    @Override
    public void finish() {
        progressDialog.dismiss();
    }

    @Override
    public void startGetData() {

    }

    @Override
    public void finishGetData() {
        swipeRefreshLayout.setRefreshing(false);
        adapter.refreshAdapter(cache.getReport());
        updateUI();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (swipeRefreshLayout != null){
            refreshData(1);
        }
    }
}
