package ir.markazandroid.uinitializr.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ir.markazandroid.uinitializr.R;
import ir.markazandroid.uinitializr.fragment.abstracts.BaseNetworkFragment;
import ir.markazandroid.uinitializr.network.OnResultLoaded;
import ir.markazandroid.uinitializr.object.User;
import ir.markazandroid.uinitializr.view.ButtonHandler;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginSuccessListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends BaseNetworkFragment {

    private LoginSuccessListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }


    private TextInputLayout usernameLayout, passwordLayout;
    private EditText username, password;
    private Button submit;
    private View gotoRegister;
    private ButtonHandler buttonHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        usernameLayout = view.findViewById(R.id.username_layout);
        passwordLayout = view.findViewById(R.id.password_layout);
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        submit = view.findViewById(R.id.submit);
        gotoRegister = view.findViewById(R.id.register);
        buttonHandler = new ButtonHandler(getActivity(), submit, getResources().getColor(R.color.colorPrimary), false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getPreferencesManager().getUsername() != null) {
            username.setText(getPreferencesManager().getUsername());
            password.setText(getPreferencesManager().getPassword());
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) submit();
            }
        });
    }

    private void submit() {
        buttonHandler.click();
        User user = new User();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());

        getNetworkManager().login(user, new OnResultLoaded<User>() {

            @Override
            public void loaded(final User result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result != null) {
                            saveCreditions();
                            mListener.onLoginSuccess(result);
                        } else {
                            handleServerErrors();
                        }
                    }
                });
            }

            @Override
            public void failed(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void saveCreditions() {
        getPreferencesManager().setUsername(username.getText().toString());
        getPreferencesManager().setPassword(password.getText().toString());
    }

    private void handleServerErrors(/*ErrorObject error*/) {
        //   if (error.getMessage()!=null)
        //       Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
        usernameLayout.setError("اطلاعات نادرست است.");
    }

    private boolean validateInputs() {
        usernameLayout.setErrorEnabled(false);
        passwordLayout.setErrorEnabled(false);
        String u, p;
        boolean valid = true;
        /*u=username.getText().toString();
        p=password.getText().toString();

        if (u.length()<6 || u.length()>16){
            usernameLayout.setError(getString(R.string.username_length));
            valid=false;
        }

        if (p.length()<8 || p.length()>32){
            passwordLayout.setError(getString(R.string.password_length));
            valid=false;
        }*/


        return valid;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginSuccessListener) {
            mListener = (LoginSuccessListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RegisterSuccessListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        buttonHandler.dispose();
    }

    public interface LoginSuccessListener {
        void onLoginSuccess(User user);
    }
}
