package com.ahmad.cameraapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ahmad.cameraapp.R;
import com.ahmad.cameraapp.config.Cache;
import com.ahmad.cameraapp.config.ConnectionCheck;
import com.ahmad.cameraapp.service.LogAccessRequestAsync;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment implements LogAccessRequestAsync.AsyncResponse {

    private ConnectionCheck conn;
    private Cache cache;
    private Window window;
    private EditText inputPassword, inputId;
    private ProgressBar progressBar;
    private Button buttonLogin;
    private LogAccessRequestAsync logAccessRequestAsync;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment,
                container, false);
    }

    public void onViewCreated (View view, Bundle savedInstanceState){
        conn = new ConnectionCheck(getActivity());
        cache = new Cache(getActivity());
        inputId = view.findViewById(R.id.input_id);
        inputPassword = view.findViewById(R.id.input_password);
        progressBar = view.findViewById(R.id.progressBar);
        buttonLogin = view.findViewById(R.id.button_login);
        setup();
    }

    private View.OnClickListener login = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (conn.isOnline()){
                String id = inputId.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                if (!id.isEmpty() && !password.isEmpty()){

                    JSONObject objectRequest = new JSONObject();
                    try {
                        objectRequest.put("staff_id", id);
                        objectRequest.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    login(objectRequest);
                }
                else{
                    Toast.makeText(getActivity(), "Enter your id and password!", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getActivity(), "No internet connection!", Toast.LENGTH_SHORT).show();
            }

        }
    };

    public void login(JSONObject objectRequest){
        logAccessRequestAsync = new LogAccessRequestAsync(getActivity());
        logAccessRequestAsync.setListener(this).execute(objectRequest);
    }

    public void setup(){
        window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.toolbar_color));
        progressBar.setVisibility(View.INVISIBLE);
        buttonLogin.setOnClickListener(login);
    }

    public void redirect(){

        if (cache.getUserPrefCache() != null){
            MainFragment mainFragment = new MainFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        redirect();
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
        }
        else{
            Toast.makeText(getActivity(), "Invalid id or password!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void start() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void finish() {
        progressBar.setVisibility(View.INVISIBLE);
    }
}
