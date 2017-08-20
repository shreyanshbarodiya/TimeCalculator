package shreyansh.learn.com.timecalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import shreyansh.learn.com.timecalculator.Admin.AdminActivities;
import shreyansh.learn.com.timecalculator.User.UserActivities;

public class LoginActivity extends AppCompatActivity {

    Button button, button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Admin
                Intent intent = new Intent(LoginActivity.this,AdminActivities.class);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //User
                Intent intent = new Intent(LoginActivity.this, UserActivities.class);
                startActivity(intent);
            }
        });
    }
}
