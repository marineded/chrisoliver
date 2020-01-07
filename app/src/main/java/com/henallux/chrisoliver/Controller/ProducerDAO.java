package com.henallux.chrisoliver.Controller;

import com.henallux.chrisoliver.Model.Producer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CollectionDAO {

    public ArrayList<Producer> getAllProducers() throws Exception
    {
        try {
            URL url = new URL("http://onmangelocal-web.azurewebsites.net/api/Producers");
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
                return jsonToProducer(stringBuilder.toString());
            }
            connection.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    private ArrayList<Producer> jsonToProducer(String stringJSON) throws Exception
    {
        ArrayList<Producer> producers = new ArrayList<>();
        Producer producer;
        JSONArray jsonArray = new JSONArray(stringJSON);
        for (int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject jsonProducer = jsonArray.getJSONObject(i);
            producer = new Producer(jsonProducer.getString("EMail"),
                    jsonProducer.getString("Street"),
                    jsonProducer.getInt("NumberStreet"),
                    jsonProducer.getString("City"),
                    jsonProducer.getInt("ZipCode"),
                    jsonProducer.getString("Country"),
                    jsonProducer.getString("PhoneNumber"),
                    jsonProducer.getString("SocietyName"),
                    jsonProducer.getInt("Reward"),
                    jsonProducer.getInt("EvalutationNumber"),
                    jsonProducer.getString("Description"),
                    jsonProducer.getString("Timetable"),
                    ((jsonProducer.getString("TVANumber").equals("null")) ? null : jsonProducer.getString("TVANumber")),
                    ((jsonProducer.getString("WebSite").equals("null")) ? null : jsonProducer.getString("WebSite")));
            producers.add(producer);
        }
        return producers;
    }

    public Boolean putProducer (Producer producer)
    {
        try {
            URL url = new URL("http://onmangelocal-web.azurewebsites.net/api/Producers/?id=" + producer.getEmail());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", "Bearer " + AccountDAO.TOKEN);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            connection.connect();
            JSONObject data = producerToJSON(producer);
            writer.write(data.toString());
            writer.flush();
            int code = connection.getResponseCode();
            writer.close();
            connection.disconnect();
            return ((code == 204) ? true : false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    private JSONObject producerToJSON(Producer producer)
    {
        JSONObject jsonString = new JSONObject();
        try
        {
            jsonString.accumulate("EMail", producer.getEmail());
            jsonString.accumulate("Street", producer.getStreet());
            jsonString.accumulate("NumberStreet", producer.getNumberStreet());
            jsonString.accumulate("City", producer.getCity());
            jsonString.accumulate("ZipCode", producer.getZipCode());
            jsonString.accumulate("Country", producer.getCountry());
            jsonString.accumulate("PhoneNumber", producer.getPhoneNumber());
            jsonString.accumulate("SocietyName", producer.getSocietyName());
            jsonString.accumulate("Reward", producer.getReward());
            jsonString.accumulate("EvalutationNumber", producer.getEvaluationNumber());
            jsonString.accumulate("Description", producer.getDescription());
            jsonString.accumulate("Timetable", producer.getTimeTable());
            jsonString.accumulate("TVANumber", producer.getTvaNumber());
            jsonString.accumulate("WebSite", producer.getWebSite());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
