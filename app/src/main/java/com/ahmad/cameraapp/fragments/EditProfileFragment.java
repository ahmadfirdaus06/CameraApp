package com.ahmad.cameraapp.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmad.cameraapp.R;
import com.ahmad.cameraapp.config.Cache;
import com.ahmad.cameraapp.config.ConnectionCheck;
import com.ahmad.cameraapp.config.DataSource;
import com.ahmad.cameraapp.models.User;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class EditProfileFragment extends Fragment {

    private TextView textName, textRegisteredBy, textLastLogin, textConfirm;
    private EditText inputEmail, inputContactNo, inputNewPassword, inputConfirmPassword;
    private ImageButton buttonClose;
    private User tempUser;
    private Cache cache;
    private String email, contactNo, newPassword, confirmPassword;
    private AlertDialog alertDiscard;
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private DataSource dataSource;
    private ConnectionCheck conn;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_profile_fragment,
                container, false);
    }

    public void onViewCreated (View view, Bundle savedInstanceState){
        cache = new Cache(getActivity());
        dataSource = new DataSource();
        conn = new ConnectionCheck(getActivity());
        textName = view.findViewById(R.id.text_name);
        textRegisteredBy = view.findViewById(R.id.text_registered_by);
        textLastLogin = view.findViewById(R.id.text_last_login);
        textConfirm = view.findViewById(R.id.text_confirm);
        inputEmail = view.findViewById(R.id.input_email);
        inputContactNo = view.findViewById(R.id.input_contact_no);
        inputNewPassword = view.findViewById(R.id.input_new_password);
        inputConfirmPassword = view.findViewById(R.id.input_confirm_password);
        buttonClose = view.findViewById(R.id.button_close);
        setup();
    }

    public void setup(){

        if (cache.getUserPrefCache() != null){
            tempUser = new User();

            tempUser = cache.getUserPrefCache();

            textName.setText(tempUser.getName());
            textRegisteredBy.setText(tempUser.getCreatedDate());
            textLastLogin.setText(tempUser.getLastLogin());
            inputEmail.setText(tempUser.getEmail());
            inputContactNo.setText(tempUser.getContactNo());
            inputNewPassword.setText("");
            inputConfirmPassword.setText("");

            inputEmail.addTextChangedListener(validator);
            inputContactNo.addTextChangedListener(validator);
            inputNewPassword.addTextChangedListener(validator);
            inputConfirmPassword.addTextChangedListener(validator);

            buttonClose.setOnClickListener(close);
            textConfirm.setOnClickListener(validSave);

        }
    }

    public void validateAllFields(){

        boolean valid = false;

        email = inputEmail.getText().toString().trim();
        contactNo = inputContactNo.getText().toString().trim();
        newPassword = inputNewPassword.getText().toString().trim();
        confirmPassword = inputConfirmPassword.getText().toString().trim();

        if (!email.isEmpty() && !contactNo.isEmpty() && !newPassword.isEmpty() && !confirmPassword.isEmpty() && newPassword.equals(confirmPassword)){
            valid = true;
        }
        else if (!email.isEmpty() && !contactNo.isEmpty() && newPassword.isEmpty()){
            valid = true;
        }
        else{
            valid = false;
        }


        if (valid){
            textConfirm.setOnClickListener(validSave);
        }
        else{
            textConfirm.setOnClickListener(invalidSave);
        }
    }

    public void updateData(User user){

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("user_id", user.getUserID());
            requestObject.put("staff_id", user.getStaffId());
            requestObject.put("email", user.getEmail());
            requestObject.put("contact_no", user.getContactNo());
            if (!user.getPassword().isEmpty()){
                requestObject.put("password", user.getPassword());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(requestObject);
        if (conn.isOnline()){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Saving...");
            progressDialog.show();
            requestQueue = Volley.newRequestQueue(getActivity());
            jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, dataSource.getUpdateUserData(), requestObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            try {
                                String message = response.getString("message");
                                System.out.println(message);
                                if (message.equals("Success")){
                                    JSONObject data = response.getJSONArray("data").getJSONObject(0);
                                    if (data != null){
                                        User user = new User();
                                        user.setUserID(data.getString("user_id"));
                                        user.setStaffId(data.getString("staff_id"));
                                        user.setPassword(data.getString("password"));
                                        user.setName(data.getString("name"));
                                        user.setEmail(data.getString("email"));
                                        user.setContactNo(data.getString("contact_no"));
                                        user.setUserType(data.getString("user_type"));
                                        user.setCreatedDate(data.getString("created_date"));
                                        user.setModifiedDate(data.getString("modified_date"));
                                        user.setLastLogin(data.getString("last_login"));
                                        user.setGrantedAccess(data.getString("granted_access"));
                                        if (cache.setUserPrefCache(user)){
                                            Toast.makeText(getActivity(), "Changes saved successfully.", Toast.LENGTH_SHORT).show();
                                            setup();
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                                Toast.makeText(getActivity(),
                                        "Upload Failed. Try again later.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            System.out.println(error);
                            Toast.makeText(getActivity(), "Upload Failed. Try again later."
                                    , Toast.LENGTH_SHORT).show();
                        }
                    });

            requestQueue.add(jsonObjectRequest);
        }else{
            Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_SHORT).show();
        }

    }

    public void refreshUI(){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();
    }

    public void close(){

        email = inputEmail.getText().toString().trim();
        contactNo = inputContactNo.getText().toString().trim();
        newPassword = inputNewPassword.getText().toString().trim();

        tempUser.setEmail(email);
        tempUser.setContactNo(contactNo);
        if (!newPassword.isEmpty()){
            tempUser.setPassword(newPassword);
        }

        Gson gson = new Gson();

        String newSave = gson.toJson(tempUser);
        String oldSave = gson.toJson(cache.getUserPrefCache());

        System.out.println(newSave);
        System.out.println(oldSave);

        if (!newSave.equals(oldSave)){
            alertDiscard = new AlertDialog.Builder(getActivity()).create();
            alertDiscard.setTitle("Confrm");
            alertDiscard.setMessage("Discard changes?");
            alertDiscard.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    });
            alertDiscard.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    });
            alertDiscard.show();
        }
        else{
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private View.OnClickListener validSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            System.out.println("Success");
            email = inputEmail.getText().toString().trim();
            contactNo = inputContactNo.getText().toString().trim();
            newPassword = inputNewPassword.getText().toString().trim();

            tempUser.setEmail(email);
            tempUser.setContactNo(contactNo);
            tempUser.setPassword(newPassword);
            updateData(tempUser);
        }
    };

    private View.OnClickListener invalidSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "Please verify all required fields.", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener close = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            close();
        }
    };

    private TextWatcher validator = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().isEmpty()){
                if (inputEmail.isFocused()){
                    inputEmail.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.red));
                }
                if (inputContactNo.isFocused()){
                    inputContactNo.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.red));
                }
                if (inputNewPassword.isFocused()){
                    inputConfirmPassword.setEnabled(false);
                    inputConfirmPassword.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.darker_grey));
                }
            }
            else{
                if (inputEmail.isFocused()){
                    inputEmail.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.blue));
                }
                if (inputContactNo.isFocused()){
                    inputContactNo.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.blue));
                }
                if (inputNewPassword.isFocused()){
                    inputConfirmPassword.setEnabled(true);
                    inputConfirmPassword.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.blue));
                }
                if (inputConfirmPassword.isFocused()){
                    if (inputConfirmPassword.getText().toString().trim().equals(inputNewPassword.getText().toString().trim())){
                        inputConfirmPassword.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.blue));
                        inputNewPassword.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.blue));
                    }
                    else{
                        inputConfirmPassword.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.red));
                        inputNewPassword.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.red));
                    }
                }
            }
            validateAllFields();
        }
    };
}
