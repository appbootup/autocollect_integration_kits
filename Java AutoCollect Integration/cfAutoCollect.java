package cashfreeUser_java_integration;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class cfAutoCollect {
    public static String token;
    public static String clientId;
    public static String clientSecret;
    public static  String stage;
    public static  String baseUrl;

    cfAutoCollect(String clientId, String clientSecret, String stage) {

        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.stage = stage.toUpperCase();
        if (stage == "TEST"){
            this.baseUrl = "https://cac-gamma.cashfree.com";
        }
        else if (stage == "PROD"){
            this.baseUrl = "https://cac-api.cashfree.com";
        }
        else System.out.println("Stage value invalid");


    }

    public String client_auth() {

        HashMap<String,String> headers =new HashMap<String,String>();

        String postData = "";
        try {
            headers.put("X-Client-Id", this.clientId);
            headers.put("X-Client-Secret", this.clientSecret);
            String link = this.baseUrl + "/cac/v1/authorize";

            String response = makePostCall(link, headers, postData);

            JSONParser parser = new JSONParser();
            JSONObject responseObj = (JSONObject) parser.parse(response);
            String getToken = (String) ((JSONObject) responseObj.get("data")).get("token");


            this.token = getToken;

            return getToken;
        }
        catch (Exception e){
            return  "Empty JSON response.";
        }

    }


    public static String getParamsString(Map<String, String> params)  {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(entry.getKey()); // Using URLEncoder is recommended here
            result.append("=");
            result.append(entry.getValue()); // Using URLEncoder is recommended here too
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }


    public String makeGetCall(String endpoint, Map<String, String> headers, Map<String, String> getParametersMap) {
        String response = "";

        try {

            if(!getParametersMap.isEmpty()){
                String getData = getParamsString(getParametersMap);
                endpoint += getData;
            }

            URL myURL = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection)myURL.openConnection();
            for(Map.Entry m:headers.entrySet()){
                conn.setRequestProperty(m.getKey().toString(),m.getValue().toString());
            }

            conn.setRequestMethod("GET");
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                response += output;
            }
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }




    public String makePostCall(String link, Map<String, String> headers, String postData) {
        String response = "";
        try {
            URL myURL = new URL(link);

            HttpURLConnection conn = (HttpURLConnection)myURL.openConnection();

            for(Map.Entry m:headers.entrySet()){
                conn.setRequestProperty(m.getKey().toString(),m.getValue().toString());
            }

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", "" + postData.getBytes().length);

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(postData.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            while ((output = br.readLine()) != null) {
                response += output;
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String create_virtual_account(String vAccountId, String name, String phone, String email) throws Exception{

        if ((vAccountId == null) || (name  == null) || (email == null ) || (phone == null)){
            return  "Mandatory parameters missing";
        }
        else {

            HashMap<String,String> userParam =new HashMap<String,String>();
            //created a hashmap with the input received
            userParam.put("vAccountId",vAccountId);
            userParam.put("name", name);
            userParam.put("email",email);
            userParam.put("phone",phone);

            JSONObject userParamJson = new JSONObject();
            userParamJson.putAll(userParam);

            HashMap<String,String> headers =new HashMap<String,String>();
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + this.token);

            String link = this.baseUrl + "/cac/v1/createVA";

            String response = makePostCall(link,headers,userParamJson.toString());

            JSONParser parser = new JSONParser();
            JSONObject responseObj = (JSONObject) parser.parse(response);

            return responseObj.toJSONString();
        }
    }

    public String recent_payments(String vAccountId)  {

        if (vAccountId == null) {
            return "Mandatory parameters missing";
        } else {
            try {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + this.token);
                String link = this.baseUrl + "/cac/v1/payments/" + vAccountId;

                HashMap<String, String> getData = new HashMap<String, String>();

                String response = makeGetCall(link, headers, getData);

                JSONParser parser = new JSONParser();
                JSONObject responseObj = (JSONObject) parser.parse(response);

                return responseObj.toJSONString();
            }
            catch (Exception e){
                return  "Empty JSON response.";
            }

        }

    }



    public String list_all_va() {

        try {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Authorization", "Bearer " + this.token);
            String link = this.baseUrl + "/cac/v1/allVA";
            HashMap<String, String> getData = new HashMap<String, String>();

            String response = makeGetCall(link, headers, getData);

            JSONParser parser = new JSONParser();
            JSONObject responseObj = (JSONObject) parser.parse(response);

            return responseObj.toJSONString();
        }
         catch (Exception e){
            return  "Empty JSON response.";
        }
    }





    public static void main(String[] args) throws Exception{
        cfAutoCollect newuser = new cfAutoCollect("CF27EFXRWU3WKIAAUUQ", "5bd9ba5e0bf69112dde1a7f0a77121c3b78b144c", "TEST");

        System.out.println(newuser.client_auth());
        System.out.println(newuser.create_virtual_account("TES1","Testing","9910115208","a.b@gmail.com"));
        System.out.println(newuser.recent_payments("TES1"));
        System.out.println(newuser.list_all_va());
    }


}


