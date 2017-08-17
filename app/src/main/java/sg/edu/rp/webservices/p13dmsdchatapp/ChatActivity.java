package sg.edu.rp.webservices.p13dmsdchatapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    String username = "";
    String TAG = ">";
    String location = "Woodlands";
    TextView tvWeatherOutput;
    Button btnSendMsg;
    EditText etMsgBody;
    String messageString, time = "";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference refUser, refMsg;
    FirebaseAuth mAuth;

    ArrayAdapter aa;
    ArrayList<Message> almessage;
    ListView lv;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();

        btnSendMsg = (Button) findViewById(R.id.btnSendMsg);
        tvWeatherOutput = (TextView) findViewById(R.id.tvWeatherOutput);
        etMsgBody = (EditText) findViewById(R.id.etMsgBody);

        lv = (ListView) this.findViewById(R.id.lvMessages);
        almessage = new ArrayList<Message>();
        aa = new MessageAdapter(this, R.layout.custom_listview_row, almessage);
        lv.setAdapter(aa);

        String url = "https://api.data.gov.sg/v1/environment/2-hour-weather-forecast";
        GetWS ws = new GetWS();
        ws.execute(url);

        firebaseDatabase = FirebaseDatabase.getInstance();
        refUser = firebaseDatabase.getReference("/P13-DB/userList");
        refMsg = firebaseDatabase.getReference("/P13-DB/messageList");

        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    username = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue().toString();
                } catch (Exception e){

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentTime = Calendar.getInstance().getTime();
                time = currentTime.toString();

                messageString = etMsgBody.getText().toString();
                Message message2 = new Message(messageString, time, username);
                refMsg.push().setValue(message2);
                almessage.add(message2);
                refUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(etMsgBody.getWindowToken(), 0);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                aa.notifyDataSetChanged();
                etMsgBody.setText("");
            }
        });


        refMsg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Message post = postSnapshot.getValue(Message.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private class GetWS extends AsyncTask<String, Integer, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(ChatActivity.this, "Processing", "A moment please..");
            pd.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            pd.dismiss();
            super.onPostExecute(result);
//            Log.i(TAG, "onPostExecute: " + result);

            try {
                JSONObject jobj = new JSONObject(result);
//                JSONArray jarray_metadata = jobj.getJSONArray("area_metadata");
                JSONArray jarray_items = jobj.getJSONArray("items");
//                JSONArray jarray_api_info = jobj.getJSONArray("api_info");

//                Log.i(TAG, "onPostExecute: " + jarray_metadata.toString());
//                Log.i(TAG, "onPostExecute: " + jarray_items.toString());
//                Log.i(TAG, "onPostExecute: " + jarray_api_info.toString());

                for (int i = 0; i < jarray_items.length(); i++) {
                    JSONObject jobj2 = jarray_items.getJSONObject(i);

                    JSONArray jarray_forecasts = jobj2.getJSONArray("forecasts");

                    Log.i(TAG, "onPostExecute: " + jarray_forecasts);

                    for (int j = 0; j < jarray_forecasts.length(); j++) {
                        JSONObject forecast_obj = jarray_forecasts.getJSONObject(j);
                        String area = forecast_obj.getString("area");
                        Log.i(TAG, "onPostExecute: " + area);

                        if (area.equalsIgnoreCase(location)) {
                            String forecastNameOfLocation = forecast_obj.getString("area");
                            String forecastDesc = forecast_obj.getString("forecast");
                            tvWeatherOutput.setText("Greetings, " + username + ", 2-hour forecast for " + forecastNameOfLocation + " is " + forecastDesc);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String results = "";
            try {
                String StringUrl1 = params[0];
                URL url = new URL(StringUrl1);

                URLConnection connection;
                connection = url.openConnection();

                HttpURLConnection httpConnection = (HttpURLConnection) connection;
//                httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpConnection.setRequestProperty("api-key", "a5Qlf3AYhyaDIIks6BUSsHjE8ARqJslQ");
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

//                OutputStream os = httpConnection.getOutputStream();
//                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
//                String msg = params[1] + "=" + params[2];
//                String msg2 = "&" + params[3] + "=" + params[4];
//                String msg3 = "&" + params[5] + "=" + params[6];
//                osw.write(msg);
//                osw.flush();

                int responseCode = httpConnection.getResponseCode();

                InputStream is = httpConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                BufferedReader br = new BufferedReader(isr);

                StringBuffer sb = new StringBuffer();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                results = sb.toString();
//                Log.d("Result", results);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return results;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onOptionsItemSelected>>: " + refUser.removeValue());

                Intent i = new Intent(ChatActivity.this, MainActivity.class);
                startActivity(i);

        }
        return true;
    }
}
