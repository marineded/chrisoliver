package com.henallux.chrisoliver.View;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.Toast;

import com.henallux.chrisoliver.R;

public class ConnectionInscriptionActivity extends AppCompatActivity {

    private Button buttonConnection;
    private Button buttonInscription;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_inscription);

        buttonConnection = (Button) findViewById(R.id.button_connection);
        buttonInscription =(Button) findViewById(R.id.button_inscription);

        if (haveInternetConnection()) {
            buttonConnection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(ConnectionInscriptionActivity.this, ConnectionActivity.class);
                    startActivity(intent);
                }
            });


            buttonInscription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(ConnectionInscriptionActivity.this, InscriptionActivity.class);
                    startActivity(intent);
                }
            });
        }
        else
        {
            String errorMessage = (String) getText(R.string._no_internet_connection);
            Toast.makeText(ConnectionInscriptionActivity.this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    private boolean haveInternetConnection(){
        // Fonction haveInternetConnection : return true si connecté, return false dans le cas contraire
        NetworkInfo network = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (network == null || !network.isConnected())
        {
            // Le périphérique n'est pas connecté à Internet
            return false;
        }
        // Le périphérique est connecté à Internet
        return true;
    }
}

