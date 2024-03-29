package hr.foi.rsc.rscapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;

import hr.foi.rsc.core.Input;
import hr.foi.rsc.core.SessionManager;
import hr.foi.rsc.core.prompts.LoadingPrompt;
import hr.foi.rsc.model.Token;
import hr.foi.rsc.rscapp.handlers.LoginHandler;
import hr.foi.rsc.rscapp.handlers.ResponseHandler;
import hr.foi.rsc.model.Credentials;
import hr.foi.rsc.rscapp.handlers.TokenHandler;
import hr.foi.rsc.webservice.ServiceAsyncTask;
import hr.foi.rsc.webservice.ServiceParams;
import hr.foi.rsc.webservice.ServiceResponse;

public class LoginActivity extends Activity {

    ImageView logo;
    EditText username;
    EditText password;
    TextInputLayout usernameLayout;
    TextInputLayout passwordLayout;
    Button signIn;
    TextView register;
    List<Input> inputs;
    LoadingPrompt loadingPrompt;
    String usernameValue;
    String passwordValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // binding
        logo=(ImageView)findViewById(R.id.imgvLogo);
        username=(EditText)findViewById(R.id.txtUsername);
        password=(EditText)findViewById(R.id.txtPassword);
        signIn=(Button)findViewById(R.id.btnSignIn);
        register=(TextView)findViewById(R.id.txtRegister);
        usernameLayout=(TextInputLayout)findViewById(R.id.txtUsernameLayout);
        passwordLayout=(TextInputLayout)findViewById(R.id.txtPasswordLayout);
        signIn.setOnClickListener(onSignIn);
        register.setOnClickListener(onRegister);
        loadingPrompt = new LoadingPrompt(this);

        // for validation
        inputs = Arrays.asList(
                new Input(username, Input.TEXT_MAIN_PATTERN, getString(R.string.username_error)),
                new Input(password, Input.PASSWORD_PATTERN, getString(R.string.password_error))
        );

        if(savedInstanceState == null) {
            startAnimation();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * starts login animations
     */
    private void startAnimation() {
        Log.i("hr.foi.debug", "LoginActivity -- login animation started");
        Animation moveAnimation= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_logo);
        Animation fadeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_login_form);

        logo.startAnimation(moveAnimation);
        username.startAnimation(fadeAnimation);
        password.startAnimation(fadeAnimation);
        usernameLayout.startAnimation(fadeAnimation);
        passwordLayout.startAnimation(fadeAnimation);
        signIn.startAnimation(fadeAnimation);
        register.startAnimation(fadeAnimation);
        Log.i("hr.foi.debug", "LoginActivity -- login animation ended");
    }

    /**
     * listener that is called when sign in button is clicked
     */
    View.OnClickListener onSignIn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("hr.foi.debug", "LoginActivity -- initiated login");

            usernameValue = username.getText().toString();
            passwordValue = password.getText().toString();

            if(Input.validate(inputs)) {
                Credentials credentials = new Credentials(usernameValue, passwordValue);
                Log.i("hr.foi.debug", "LoginActivity -- sending credentials to service");

                SessionManager manager=SessionManager.getInstance(getApplicationContext());
                manager.createSession(credentials,"credentials");


                ServiceParams params = new ServiceParams(getString(hr.foi.rsc.webservice.R.string.login_path),
                        HttpMethod.POST, credentials, null, true);

                TokenHandler tokenHandler = new TokenHandler(LoginActivity.this);
                try {
                    new ServiceAsyncTask(tokenHandler).execute(params);
                } catch(Exception e) {
                    Toast.makeText(getApplicationContext(), "Please check your connection.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    /**
     * called when register is clicked
     */
    View.OnClickListener onRegister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
            startActivity(intent);
        }
    };
}
