package mud.arca.io.mud;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginScreenActivity extends AppCompatActivity {

    private final int RC_SIGN_IN = 1001;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // If the user's already logged in, just continue to MainActivity
            Toast.makeText(getApplicationContext(), "Welcome, " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + " (" + FirebaseAuth.getInstance().getCurrentUser().getEmail() + ")", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            // Otherwise, send to login screen
            login();

        }

    }

    /**
     * Login method: executes when login is clicked
     * currently redirects to main screen
     */
    public void login() {

        // Custom layout
        AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.auth_layout)
                .setGoogleButtonId(R.id.goog_auth)
                .setEmailButtonId(R.id.email_auth)
                .setPhoneButtonId(R.id.phone_auth)
                .build();


        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.PhoneBuilder().build()
                        ))
                        .setAuthMethodPickerLayout(customLayout)
                        .setTheme(R.style.LoginScreenAuthUI)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_SIGN_IN:
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (resultCode == RESULT_OK) {
                    // Successfully signed in
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    Toast.makeText(getApplicationContext(), "Welcome, " + user.getDisplayName() + " (" + user.getEmail() + ")", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    // Sign in failed. If response is null the user canceled the
                    // sign-in flow using the back button. Otherwise check
                    // response.getError().getErrorCode() and handle the error.
                    login();

                    // Cheesy error handling
                    if (response != null) {
                        Toast.makeText(getApplicationContext(), response.getError().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }

    }
}
