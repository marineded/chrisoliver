package com.henallux.chrisoliver.Controller;

import com.henallux.chrisoliver.Model.Selection;
import com.henallux.chrisoliver.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class AccountDAO {

    public static String TOKEN ="";
    public static Selection SELECTIONCUSTOMER;

    public int inscription(User user) throws Exception
    {
        int code =-1;
        try {
            URL url = new URL("http://onmangelocal-web.azurewebsites.net/api/Customer/Register");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            connection.connect();
            JSONObject data = userToJSON(user);
            writer.write(data.toString());
            writer.flush();
            code = connection.getResponseCode();
            writer.close();
            connection.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return code;
    }
    private JSONObject userToJSON(User user)
    {
        JSONObject jsonString = new JSONObject();
        try {
            jsonString.accumulate("EMail", user.getMail());
            jsonString.accumulate("Password", user.getPassword());
            jsonString.accumulate("ConfirmPassword", user.getPassword());
            jsonString.accumulate("Name", user.getName());
            jsonString.accumulate("FirstName", user.getFirstName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsonString;
    }

    public int connection(String userLogin, String userPassword) throws Exception
    {
        int code = -1;
        try {
            URL url = new URL("http://onmangelocal-web.azurewebsites.net/token");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "x-www-urlencoded");
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            connection.connect();
            String data = "Username=" + userLogin + "&Password=" + userPassword + "&grant_type=password";
            writer.write(data);
            writer.flush();
            code = connection.getResponseCode();
            if (code == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();
                jsonToToken(stringBuilder.toString());
                CustomerDAO customerDAO = new CustomerDAO();
                SELECTIONCUSTOMER = new Selection(customerDAO.getCustomers(userLogin));
            }
            connection.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return code;
    }
    private void jsonToToken(String stringJSON) throws Exception
    {
        JSONObject jsonObject = new JSONObject(stringJSON);
        TOKEN = jsonObject.getString("access_token");
    }
}
