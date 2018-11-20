package util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class testJson {
    public String readfile () throws IOException {
        String json;
        ReadJsonFile readjson = new ReadJsonFile();
        String jsonF = readjson.readfile();

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(String.valueOf(jsonF));
        JsonObject rootObject = jsonElement.getAsJsonObject();
        JsonObject settingsOject = rootObject.getAsJsonObject("settings");
        String wwwData = settingsOject.get("wwwData").getAsString();

        try (BufferedReader br = new BufferedReader(new FileReader(wwwData))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            json = sb.toString();

        }return json;



    }
}