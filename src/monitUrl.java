import com.google.gson.*;
import util.ReadJsonFile;
import util.ScanPort;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class monitUrl {
    void test() throws IOException {
        JsonObject hosts = new JsonObject();
        JsonObject urlMon = new JsonObject();
        ReadJsonFile readjson = new ReadJsonFile();
        String jsonF = readjson.readfile();
        String[] addressParts;
        try {
            ScanPort openPort = new ScanPort();
            JsonParser parser = new JsonParser();
            JsonObject jo = (JsonObject) parser.parse(jsonF);
            JsonArray jsonArr = jo.getAsJsonArray("http");
            JsonArray jsonArrHttps = jo.getAsJsonArray("https");
            JsonArray jsonArrManual = jo.getAsJsonArray("manual");
            Gson googleJson = new Gson();
            ArrayList jsonObjectList = (ArrayList) googleJson.fromJson(jsonArr, ArrayList.class);
            ArrayList jsonObjectListHttps = (ArrayList) googleJson.fromJson(jsonArrHttps, ArrayList.class);
            ArrayList jsonObjectListManual = (ArrayList) googleJson.fromJson(jsonArrManual, ArrayList.class);
            if (!jsonObjectList.isEmpty()) {
                for (int i = 0; i < jsonObjectList.size(); i++) {
                    addressParts = jsonObjectList.toString().substring(1, jsonObjectList.toString().indexOf("]")).split(", ");
                    String[] http = (addressParts[i]).split("http://");
                    String[] toIp = http[1].split("/");
                    boolean resultTest = openPort.portAccess(toIp[0], 80, 5000);
                    if (resultTest) {
                        URL url = new URL(addressParts[i]);
                        URLConnection urlcon = url.openConnection();
                        boolean isNotFound = false;
                        boolean isOk = false;
                        Map<String, List<String>> header = urlcon.getHeaderFields();
                        String scanPart;
                        for (Map.Entry<String, List<String>> mp : header.entrySet()) {
                            scanPart = mp.getValue().toString();
                            isNotFound = scanPart.contains("[HTTP/1.1 404 Not Found]");
                            isOk = scanPart.contains("HTTP/1.1 200 OK");
                            if (isNotFound) {
                                break;
                            }
                            if (isOk) {
                                break;
                            }
                        }
                        if (isNotFound) {
                            urlMon.addProperty(addressParts[i], "404");
                        }
                        if (isOk) {
                            urlMon.addProperty(addressParts[i], "200");
                        }
                    } else {
                        urlMon.addProperty(addressParts[i], "522");
                    }
                }
                if (!jsonObjectListHttps.isEmpty()){
                    for (int i = 0; i < jsonObjectListHttps.size(); i++) {
                        addressParts = jsonObjectListHttps.toString().substring(1, jsonObjectListHttps.toString().indexOf("]")).split(", ");
                        String[] http = (addressParts[i]).split("https://");
                        String[] toIp = http[1].split("/");
                        boolean resultTest = openPort.portAccess(toIp[0], 443, 5000);
                        if (resultTest) {
                            urlMon.addProperty(addressParts[i]+":443", "OK");
                        }else {
                            urlMon.addProperty(addressParts[i]+":443", "522");
                        }
                    }
                }
                if (!jsonObjectListManual.isEmpty()){
                    for (int i = 0; i < jsonObjectListManual.size(); i++) {
                        addressParts = jsonObjectListManual.toString().substring(1, jsonObjectListManual.toString().indexOf("]")).split(", ");
                        String[] ip = (addressParts[i]).split("ip://");
                        String[] port = ip[1].split(":");
                        int port0 = Integer.parseInt(String.valueOf(port[1]));
                        boolean resultTest = openPort.portAccess(port[0], port0, 5000);
                        if (resultTest) {
                            urlMon.addProperty(port[0]+":"+port0, "OK");
                        }else {
                            urlMon.addProperty(port[0]+":"+port0, "522");
                        }
                    }
                }

            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }

        hosts.add("status", urlMon);
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
        String a = gson.toJson(hosts);
        System.out.println(a);

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(String.valueOf(jsonF));
        JsonObject rootObject = jsonElement.getAsJsonObject();
        JsonObject settingsOject = rootObject.getAsJsonObject("settings");
        String wwwData = settingsOject.get("wwwData").getAsString();
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(wwwData)))) {
            writer.write(a);
        }
    }
}