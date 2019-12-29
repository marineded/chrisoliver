package com.henallux.chrisoliver.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.henallux.chrisoliver.Model.User;
import com.henallux.chrisoliver.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InscriptionActivity extends AppCompatActivity {
    private Button nextButton;

    private EditText nameCustomer;
    private EditText firstNameCustomer;
    private EditText mailCustomer;
    private EditText passwordOneCustomer;
    private EditText passwordTwoCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        nameCustomer = (EditText) findViewById(R.id.inscriptionName);
        firstNameCustomer = (EditText) findViewById(R.id.inscriptionFirstName);
        mailCustomer = (EditText) findViewById(R.id.inscriptionMail);
        passwordOneCustomer = (EditText) findViewById(R.id.inscriptionPassword);
        passwordTwoCustomer = (EditText) findViewById(R.id.inscriptionPasswordConfirm);

        nextButton = (Button) findViewById(R.id.button_next_inscription_customer);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String password = passwordOneCustomer.getText().toString();
                String passwordVerification = passwordTwoCustomer.getText().toString();
                String name = nameCustomer.getText().toString();
                String firstName = firstNameCustomer.getText().toString();
                String mail = mailCustomer.getText().toString();

                if (password.equalsIgnoreCase(passwordVerification) && EditTextValidationString(password)
                        && EditTextValidationString(name) && EditTextValidationString(firstName)
                        && EditTextValidationMail(mail) && SizePasswordValidation(password))
                {
                    User user = new User(mail, name, firstName, password);
                    Intent intent = new Intent(InscriptionActivity.this, InscriptionConfirmationActivity.class);
                    intent.putExtra("User", user);

                    startActivity(intent);
                }
                else
                {
                    if (SizePasswordValidation(password) == false)
                    {
                        String textError = (String) getText(R.string._size_password);
                        Toast.makeText(InscriptionActivity.this, textError, Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if (password.equalsIgnoreCase(passwordVerification) == false)
                        {
                            String textError = (String) getText(R.string._password_error);
                            Toast.makeText(InscriptionActivity.this, textError, Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            if (EditTextValidationString(name) == false)
                            {
                                String textError = (String) getText(R.string._incorrect_name);
                                Toast.makeText(InscriptionActivity.this, textError, Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                if ( EditTextValidationString(firstName) == false)
                                {
                                    String textError = (String) getText(R.string._incorrect_first_name);
                                    Toast.makeText(InscriptionActivity.this, textError, Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    if ( EditTextValidationMail(mail) == false)
                                    {
                                        String textError = (String) getText(R.string._incorrect_email);
                                        Toast.makeText(InscriptionActivity.this, textError, Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private boolean EditTextValidationString (String editTextToVerification)
    {
        if (editTextToVerification.length() == 0)
        {
            return false;
        }
        boolean valid = true;
        char text[] = editTextToVerification.toCharArray();
        for (int i = 0; i < text.length; i++)
        {
            if (text[i] == ' ')
            {
                valid = false;
            }
        }
        return valid;
    }

    private boolean EditTextValidationMail (String editTextToVerification)
    {
        Matcher matcher;
        String EMAIL_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
                + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(editTextToVerification);
        return matcher.matches();
    }

    private boolean SizePasswordValidation (String password)
    {
        if (password.length() > 7)
        {
            return true;
        }
        return  false;
    }


}
