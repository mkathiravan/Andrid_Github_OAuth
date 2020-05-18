package net.kathir.oauth;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private String clientId = "0ee048eee2b40a83ed03";
    private String clientSecret = "d70e353ebac325356d9647267a020290af0b84dd";
    private String redirectUri = "chatapp://callback";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/login/oauth/authorize" + "?client_id=" + clientId + "&scope=repo&redirect_uri" + redirectUri));
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();

        if(uri != null && uri.toString().startsWith(redirectUri))
        {
            String code = uri.getQueryParameter("code");

            Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://api.github.com/").addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.build();

            GithubClient client = retrofit.create(GithubClient.class);
           Call<AccessToken> accessTokenCall =  client.getAccessToken(clientId,clientSecret,code);

           accessTokenCall.enqueue(new Callback<AccessToken>() {
               @Override
               public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                   Toast.makeText(MainActivity.this,"Yes",Toast.LENGTH_SHORT).show();
               }

               @Override
               public void onFailure(Call<AccessToken> call, Throwable t) {

                   Toast.makeText(MainActivity.this,"No",Toast.LENGTH_SHORT).show();
               }
           });

        }
    }
}
