package com.example.cameraapp.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cameraapp.R;
import com.example.cameraapp.config.Cache;
import com.example.cameraapp.config.ConnectionCheck;
import com.example.cameraapp.miscellanous.LogAccessRequestAsync;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements LogAccessRequestAsync.AsyncResponse{

    private ConnectionCheck conn;
    private Cache cache;
    private Window window;
    private Toolbar toolbar;
    private ListView listReport;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fab;
    private List<String> array= new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private AlertDialog alertAddReport, alertLogout;
    private LogAccessRequestAsync logAccessRequestAsync;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment,
                container, false);
    }

    public void onViewCreated (View view, Bundle savedInstanceState){
        conn = new ConnectionCheck(getActivity());
        cache = new Cache(getActivity());
        toolbar = view.findViewById(R.id.toolbar);
        listReport = view.findViewById(R.id.list_report);
        swipeRefreshLayout = view.findViewById(R.id.refresh);
        fab = view.findViewById(R.id.fab);
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
                System.out.println("User Profile");
            }
            return false;
        }
    };

    private AdapterView.OnItemClickListener clickList = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String report = listReport.getItemAtPosition(position).toString();
            Bundle data = new Bundle();
            data.putString("report", report);
            ReportStatusFragment reportStatusFragment = new ReportStatusFragment();
            reportStatusFragment.setArguments(data);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.container, reportStatusFragment).addToBackStack(null).commit();
        }
    };

    private SwipeRefreshLayout.OnRefreshListener refresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
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
                            Step1Fragment step1Fragment = new Step1Fragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                    .replace(R.id.container, step1Fragment, "step1Fragment")
                                    .addToBackStack(null).commit();
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
        for (int i = 1; i <= 20; i++){
            array.add("Report #" + i);
        }
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, array);
        listReport.setAdapter(adapter);
        listReport.setOnItemClickListener(clickList);
        swipeRefreshLayout.setOnRefreshListener(refresh);
        fab.setOnClickListener(add);
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
    }

    @Override
    public void processFinish(boolean response) {
        if (response){
            redirect();
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
}
