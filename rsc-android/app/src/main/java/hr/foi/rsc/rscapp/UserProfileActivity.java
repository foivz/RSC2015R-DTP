package hr.foi.rsc.rscapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;

import hr.foi.rsc.core.Input;
import hr.foi.rsc.core.SessionManager;
import hr.foi.rsc.model.Token;
import hr.foi.rsc.rscapp.handlers.UpdateHandler;
import hr.foi.rsc.model.Credentials;
import hr.foi.rsc.model.Person;
import hr.foi.rsc.webservice.ServiceAsyncTask;
import hr.foi.rsc.webservice.ServiceParams;

public class UserProfileActivity extends AppCompatActivity {

    Button change;
    EditText firstName;
    EditText lastName;
    EditText username;
    EditText password;
    EditText confirmPassword;
    Person user;
    List<Input> inputs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // session
        user= SessionManager.getInstance(getApplicationContext()).retrieveSession("person", Person.class);

        // binding
        firstName = (EditText) findViewById(R.id.firstNameInput);
        username=(EditText) findViewById(R.id.usernameInput);
        username.setClickable(false);
        lastName = (EditText) findViewById(R.id.lastNameInput);
        password = (EditText) findViewById(R.id.passwordInput);
        confirmPassword = (EditText) findViewById(R.id.confirmPasswordInput);
        change = (Button) findViewById(R.id.submitButton);

        // input fill
        firstName.setText(user.getName());
        lastName.setText(user.getSurname());
        username.setText(user.getCredentials().getUsername());
        password.setText(user.getCredentials().getPassword());

        change.setOnClickListener(onChange);

        // input validation
        inputs = Arrays.asList(
                new Input(firstName, Input.TEXT_MAIN_PATTERN, getString(R.string.firstname_error)),
                new Input(lastName, Input.TEXT_MAIN_PATTERN, getString(R.string.lastname_error)),
                new Input(password, Input.PASSWORD_PATTERN, getString(R.string.password_error)),
                new Input(confirmPassword, Input.PASSWORD_PATTERN, getString(R.string.matching_password_error))
        );

        // hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * invoked when submit button is clicked
     */
    View.OnClickListener onChange = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("hr.foi.debug", "UserProfileActivity -- initiated user update");

            String firstNameValue = firstName.getText().toString();
            String lastNameValue = lastName.getText().toString();
            String passwordValue = password.getText().toString();

            // if inputs are valid and passwords are matching
            if (Input.validate(inputs)
                    && inputs.get(inputs.size() - 2).equals(inputs.get(inputs.size() - 1))) {

                Log.i("hr.foi.debug", "UserProfileActivity -- fetching user from session");

                Log.i("hr.foi.debug", "UserProfileActivity --  user fetched from session " + user.toString());
                user.setName(firstNameValue);
                user.setSurname(lastNameValue);
                Credentials changedPassword = new Credentials(user.getCredentials().getUsername(), passwordValue);
                user.setCredentials(changedPassword);

                SessionManager manager=SessionManager.getInstance(getApplicationContext());

                Token userToken=manager.retrieveSession("token",Token.class);

                Log.i("hr.foi.debug", "UserProfileActivity --  calling web service ");

                UpdateHandler updateHandler = new UpdateHandler(UserProfileActivity.this, user);
                new ServiceAsyncTask(updateHandler)
                        .execute(new ServiceParams(getString(hr.foi.rsc.webservice.R.string.persons_path)
                        + user.getIdPerson(), HttpMethod.PUT, user,userToken));
            }
        }


    };

}
