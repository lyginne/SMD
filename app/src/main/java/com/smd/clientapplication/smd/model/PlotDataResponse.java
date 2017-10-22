package com.smd.clientapplication.smd.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikitos on 22.10.2017.
 */

public class PlotDataResponse {
    final private ArrayList<Double> values;
    private final String message;
    private final int sequenceNumber;

    public PlotDataResponse(ArrayList<Double> values, String message, int sequenceNumber) {
        this.values = values;
        this.message = message;
        this.sequenceNumber = sequenceNumber;
    }

    public ArrayList<Double> getValues() {
        return values;
    }

    public String getMessage() {
        return message;
    }

    public int getSequenceNumber(){
        return sequenceNumber;
    }
}
