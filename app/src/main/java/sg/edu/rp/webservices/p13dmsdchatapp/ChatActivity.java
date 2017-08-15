package sg.edu.rp.webservices.p13dmsdchatapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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

public class ChatActivity extends AppCompatActivity {

    String TAG = ">";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String url = "https://api.data.gov.sg/v1/environment/2-hour-weather-forecast";
        GetWS ws = new GetWS();
        ws.execute(url);
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
                JSONArray jarray_metadata = jobj.getJSONArray("area_metadata");
//                JSONArray jarray_items = jobj.getJSONArray("items");
//                JSONArray jarray_api_info = jobj.getJSONArray("api_info");

//                Log.i(TAG, "onPostExecute: " + jarray_metadata.toString());
//                Log.i(TAG, "onPostExecute: " + jarray_items.toString());
//                Log.i(TAG, "onPostExecute: " + jarray_api_info.toString());



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
                Log.d("Result", results);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return results;
        }

    }
}
