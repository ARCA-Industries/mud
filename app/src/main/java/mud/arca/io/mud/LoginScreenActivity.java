package mud.arca.io.mud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);


    }

    /**
     * Login method: executes when login is clicked
     * currently redirects to main screen
     * @param v View to return when clicked
     */
   public void login(View v) {

        String username;
        String password;

/*        while (true) {
            if (!username.equals(enteredUsername) || !password.equals(enteredPassword)) {
                // wrong login credentials!
                continue;
            }
            break;
        }*/

        startActivity(new Intent(this, MainActivity.class));
    }
}
