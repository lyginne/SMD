package com.smd.clientapplication.smd.model;

/**
 * Created by nikitos on 22.10.2017.
 */

public abstract class DiagnosticCallBack {

    public abstract void onDataRecieved(PlotDataResponse plotDataResponse);
    public abstract void onError();
}
