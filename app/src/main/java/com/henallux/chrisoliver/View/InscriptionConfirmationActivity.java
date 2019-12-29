package com.henallux.chrisoliver.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.henallux.chrisoliver.Controller.AccountDAO;
import com.henallux.chrisoliver.Model.User;
import com.henallux.chrisoliver.R;

public class InscriptionConfirmationActivity extends AppCompatActivity {
    private Button confirmationButton;

    private TextView name;
    private TextView firstName;
    private TextView mail;

    private int resultCode = -1;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription_confirmation);

        user = getIntent().getExtras().getParcelable("User");

        name = (TextView) findViewById(R.id.inscriptionName);
        firstName = (TextView) findViewById(R.id.inscriptionFirstName);
        mail = (TextView) findViewById(R.id.inscriptionMail);

        name.setText(user.getName());
        firstName.setText(user.getFirstName());
        mail.setText(user.getMail());

        confirmationButton = (Button) findViewById(R.id.button_confirmation);
        confirmationButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LoadAccount loadAccount = new LoadAccount();
                        loadAccount.execute(user);
                    }});
    }

    private class LoadAccount extends AsyncTask<User, Void, Integer>
    {
        ProgressDialog dialog ;
        protected void onPreExecute () {
            dialog = ProgressDialog.show(InscriptionConfirmationActivity.this, getText(R.string._loading), getText(R.string._please_wait));
        }

        @Override
        protected Integer doInBackground(User... params) {
            int code = -1;
            User user = (params.length == 0) ? null : params[0];
            if (user != null) {
                AccountDAO accountDAO = new AccountDAO();
                try {
                    code = accountDAO.inscription(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer c)
        {
            resultCode = c;
            if (resultCode == 200)
            {
                String message = (String) getText(R.string._validation);
                Toast.makeText(InscriptionConfirmationActivity.this, message, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(InscriptionConfirmationActivity.this, ConnectionActivity.class);
                dialog.dismiss();
                startActivity(intent);
                finish();
            }
            else
            {
                dialog.dismiss();
                String textError = Integer.toString(c);
                Toast.makeText(InscriptionConfirmationActivity.this, textError, Toast.LENGTH_LONG).show();
            }
        }
    }
}
