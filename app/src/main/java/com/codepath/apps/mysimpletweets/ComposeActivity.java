package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    // EditText etCompose = (EditText) findViewById(R.id.etCompose);

    private TwitterClient client;
    Tweet newTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        setContentView(R.layout.activity_compose);
    }

    public void onSubmit(View v) {
        EditText etCompose = (EditText) findViewById(R.id.etCompose);
        String status = etCompose.getText().toString();

        client.getStatusUpdate(status, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                newTweet = Tweet.fromJSON(response);
                Intent data = new Intent();

                // Pass relevant data back as a result
                data.putExtra("tweet", (Parcelable) newTweet);

                // Activity finished ok, return the data
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(ComposeActivity.this, "Error: Tweet not posted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
