package com.smd.clientapplication.smd.model;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikitos on 22.10.2017.
 */

public class PlotDataJsonParser {

    public PlotDataResponse readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        reader.beginObject();
        String message = null;
        ArrayList<Double> values = null;
        int sequenceNumber = 0;

        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("message")) {
                message = reader.nextString();
            } else if (name.equals("values") && reader.peek() != JsonToken.NULL) {
                values = readDoublesArray(reader);
            } else if (name.equals("num")) {
                sequenceNumber = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new PlotDataResponse(values, message, sequenceNumber);
    }

    public ArrayList<Double> readDoublesArray(JsonReader reader) throws IOException {
        ArrayList<Double> doubles = new ArrayList<Double>();

        reader.beginArray();
        while (reader.hasNext()) {
            doubles.add(reader.nextDouble());
        }
        reader.endArray();
        return doubles;
    }

    private List<Double> readValues(JsonReader reader){

        return null;

    }
}
