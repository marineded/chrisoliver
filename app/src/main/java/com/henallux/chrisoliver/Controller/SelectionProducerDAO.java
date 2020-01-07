package com.henallux.chrisoliver.Controller;

import com.henallux.chrisoliver.Model.Producer;
import com.henallux.chrisoliver.Model.Selection;
import com.henallux.chrisoliver.Model.SelectionProducer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class SelectionCollectionDAO {

    public ArrayList<SelectionProducer> getAllSelectionProducer() throws Exception
    {
        try {
            URL url = new URL("http://onmangelocal-web.azurewebsites.net/api/SelectionProducers");
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

    private ArrayList<SelectionProducer> jsonToProducer(String stringJSON) throws Exception
    {
        ArrayList<SelectionProducer> selectionProducers = new ArrayList<>();
        SelectionProducer selectionProducer;
        JSONArray jsonArray = new JSONArray(stringJSON);
        for (int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject jsonSelectionProducer = jsonArray.getJSONObject(i);

            // Get Selection
            JSONObject selectionObject = jsonSelectionProducer.getJSONObject("SelectionFK");
            Selection selection = new Selection(selectionObject.getInt("Id"));

            // Get Producer
            JSONObject producerObject = jsonSelectionProducer.getJSONObject("ProducerFK");
            Producer producer = new Producer(producerObject.getString("EMail"),
                    producerObject.getString("Street"),
                    producerObject.getInt("NumberStreet"),
                    producerObject.getString("City"),
                    producerObject.getInt("ZipCode"),
                    producerObject.getString("Country"),
                    producerObject.getString("PhoneNumber"),
                    producerObject.getString("SocietyName"),
                    producerObject.getInt("Reward"),
                    producerObject.getInt("EvalutationNumber"),
                    producerObject.getString("Description"),
                    producerObject.getString("Timetable"),
                    ((producerObject.getString("TVANumber").equals("null")) ? null : producerObject.getString("TVANumber")),
                    ((producerObject.getString("WebSite").equals("null")) ? null : producerObject.getString("WebSite")));

            selectionProducer = new SelectionProducer( jsonSelectionProducer.getInt("Id"),selection,producer);
            selectionProducers.add(selectionProducer);
        }
        return selectionProducers;
    }

    public Boolean addSelectionProducer(SelectionProducer selectionProducer) throws Exception
    {
        try {
            ArrayList<SelectionProducer> sP = getAllSelectionProducer();
            Boolean ok = true;
            for (SelectionProducer allSelectionProducer : sP) {
                if ( allSelectionProducer.getSelection().getId() == AccountDAO.SELECTIONCUSTOMER.getId()
                        && allSelectionProducer.getProducer().getEmail().equals(selectionProducer.getProducer().getEmail())) {
                    ok = false;
                }
            }
            if (ok) {
                URL url = new URL("http://onmangelocal-web.azurewebsites.net/api/SelectionProducers");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + AccountDAO.TOKEN);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                connection.connect();
                JSONObject data = selectionProducerToJSON(selectionProducer);
                writer.write(data.toString());
                writer.flush();
                int code = connection.getResponseCode();
                writer.close();
                connection.disconnect();
                return ((code == 201) ? true : false);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private JSONObject selectionProducerToJSON(SelectionProducer selectionProducer)
    {
        JSONObject jsonString = new JSONObject();
        try {
            jsonString.accumulate("Id", selectionProducer.getId() );
            jsonString.accumulate("SelectionFK", selectionToJSON(selectionProducer.getSelection()));
            jsonString.accumulate("ProducerFK", producerToJSON(selectionProducer.getProducer()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsonString;
    }

    private JSONObject selectionToJSON(Selection selection)
    {
        JSONObject jsonString = new JSONObject();
        try {
            jsonString.accumulate("Id", selection.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsonString;
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

    public Boolean deleteSelectionProducer(Integer id)
    {
        try {
            URL url = new URL("http://onmangelocal-web.azurewebsites.net/api/SelectionProducers/" + (long) id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Authorization", "Bearer " + AccountDAO.TOKEN);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.connect();
            int code = connection.getResponseCode();
            connection.disconnect();
            return ((code == 200) ? true : false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
