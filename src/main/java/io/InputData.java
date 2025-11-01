package io;

import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import graph.graph.Graph;

public class InputData {
    public static Graph inputDataFromJSON(String filename) {
        try (JsonReader reader = new JsonReader(new FileReader("data/" + filename))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Graph data = gson.fromJson(reader, Graph.class);
            reader.close();
            return data;
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return null;
    }
}
