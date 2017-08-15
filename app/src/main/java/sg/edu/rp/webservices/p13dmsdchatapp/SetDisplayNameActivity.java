package sg.edu.rp.webservices.p13dmsdchatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Set;

public class SetDisplayNameActivity extends AppCompatActivity {

    String TAG = ">>";

    EditText etDisplayName;
    Button btnDisplayName;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userListRef;

    @Override
    protected void onResume() {
        super.onResume();

        userListRef = firebaseDatabase.getReference("/P13-DB/userList/");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_display_name);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        final String uid = firebaseUser.getUid();
        Log.i(TAG, "onCreate: " + uid);
        userListRef = firebaseDatabase.getReference("/P13-DB/userList/");

        etDisplayName = (EditText) findViewById(R.id.etDisplayname);
        btnDisplayName = (Button) findViewById(R.id.btnSubmitDisplayname);

        btnDisplayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String display_name = etDisplayName.getText().toString();
                userListRef.child(uid).setValue(display_name);

                Intent i = new Intent(SetDisplayNameActivity.this, ChatActivity.class);
                startActivity(i);


            }
        });
    }
}
