package com.henallux.chrisoliver.Controller;

import com.henallux.chrisoliver.Model.Selection;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CustomerDAO {

    public Selection getCustomers(String login) throws Exception
    {
        try {
            URL url = new URL("http://onmangelocal-web.azurewebsites.net/api/Customers/?id=" + login);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + AccountDAO.TOKEN);
            connection.setDoOutput(false);
            connection.connect();
            int code = connection.getResponseCode();
            if (code == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();
                return jsonToSelection(stringBuilder.toString());
            }
            connection.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    private Selection jsonToSelection(String stringJSON) throws Exception
    {
        Selection selection;
        JSONObject jsonObject = new JSONObject(stringJSON);
        JSONObject jsonSelection = jsonObject.getJSONObject("SelectionFK");
        selection = new Selection(jsonSelection.getInt("Id"));
        return selection;
    }
}
