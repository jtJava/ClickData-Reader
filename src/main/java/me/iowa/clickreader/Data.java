package me.iowa.clickreader;

import jdk.nashorn.internal.parser.JSONParser;
import lombok.Getter;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Getter
public class Data {


    private final List<Integer> downDelays = new ArrayList<>();
    private final List<Integer> upDelays = new ArrayList<>();
    private final List<Integer> clickDelays = new ArrayList<>();

    public void readData(String data) {
        this.downDelays.clear();;
        this.upDelays.clear();
        this.clickDelays.clear();
        try {
            URL url = new URL("https://cpsapi.lemin.us/results/" + data);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //add request header
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print in String, it doesn't parse if there are line breaks.
            JSONObject jsonObject = new JSONObject(response.toString().replaceAll("\n", ""));

            jsonObject.getJSONArray("downTimes").forEach(object -> {
                downDelays.add(Integer.valueOf(String.valueOf(object)));
            });

            jsonObject.getJSONArray("upTimes").forEach(object -> {
                upDelays.add(Integer.valueOf(String.valueOf(object)));
            });

            jsonObject.getJSONArray("clickDelays").forEach(object -> {
                clickDelays.add(Integer.valueOf(String.valueOf(object)));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
