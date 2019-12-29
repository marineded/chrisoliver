package com.henallux.chrisoliver.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.henallux.chrisoliver.Controller.AccountDAO;
import com.henallux.chrisoliver.R;

public class ConnectionActivity extends AppCompatActivity {
    private String mail = null;
    private String password = null;
    private Button buttonConnection;
    private int resultCode = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        buttonConnection = (Button) findViewById(R.id.identificationButton);
        buttonConnection.setOnClickListener((new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                EditText m = (EditText) findViewById(R.id.identificationEmail);
                EditText p = (EditText) findViewById(R.id.identificationPassword);
                mail = m.getText().toString();
                password = p.getText().toString();
                LoadAccount loadAccount = new LoadAccount();
                loadAccount.execute(mail,password);
            }
        }));
    }

    private class LoadAccount extends AsyncTask<String, Void, Integer>
    {
        ProgressDialog dialog ;
        protected void onPreExecute () {
            dialog = ProgressDialog.show(ConnectionActivity.this, getText(R.string._loading), getText(R.string._please_wait));
        }
        @Override
        protected Integer doInBackground(String... params) {
            int code = -1;
            String mail = (params.length == 0) ? null : params[0];
            String password = (params.length == 0) ? null : params[1];
            if (mail != null && password != null) {
                AccountDAO accountDAO = new AccountDAO();
                try {
                    code = accountDAO.connection(mail, password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer code)
        {
            resultCode = code;
            if (resultCode == 200)
            {
                Intent intent = new Intent(ConnectionActivity.this, MainActivity.class);
                startActivity(intent);
                dialog.dismiss();
                finish();

            }
            else
            {
                dialog.dismiss();
                String textError = (String) getText(R.string._error_connection);
                Toast.makeText(ConnectionActivity.this, textError, Toast.LENGTH_LONG).show();
            }
        }
    }
}
