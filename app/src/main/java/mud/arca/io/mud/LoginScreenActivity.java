package mud.arca.io.mud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginScreenActivity extends AppCompatActivity {

    private final int RC_SIGN_IN = 1001;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        findViewById(R.id.sign_in_button).setOnClickListener(v -> login());

    }

    /**
     * Login method: executes when login is clicked
     * currently redirects to main screen
     */
    public void login() {
        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build())
                        )
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

                    // ...
                } else {
                    // Sign in failed. If response is null the user canceled the
                    // sign-in flow using the back button. Otherwise check
                    // response.getError().getErrorCode() and handle the error.

                    // Cheesy error handling
                    if (response != null) {
                        Toast.makeText(getApplicationContext(), response.getError().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }

    }
}
