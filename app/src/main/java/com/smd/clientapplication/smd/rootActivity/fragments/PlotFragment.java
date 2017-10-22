package com.smd.clientapplication.smd.rootActivity.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.smd.clientapplication.smd.R;
import com.smd.clientapplication.smd.model.DiagnosticCallBack;
import com.smd.clientapplication.smd.model.PlotDataResponse;
import com.smd.clientapplication.smd.model.Requester;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlotFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlotFragment extends Fragment {

    private final Handler mHandler = new Handler();
    private Runnable mTimer2;
    private LineGraphSeries<DataPoint> mSeries2;
    private double graph2LastXValue = 5d;
    private ConcurrentLinkedQueue<Double> queue = new ConcurrentLinkedQueue<Double>();
    private volatile int sequenceNumber;
    private volatile String message;

    private volatile Object fence = new Object();
    TextView engineStatus;


    private OnFragmentInteractionListener mListener;

    public PlotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlotFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlotFragment newInstance(String param1, String param2) {
        PlotFragment fragment = new PlotFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_plot, container, false);
        engineStatus = (TextView) rootView.findViewById(R.id.engineStatusTextView);

        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        mSeries2 = new LineGraphSeries<>();
        graph.addSeries(mSeries2);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(40);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-2);
        graph.getViewport().setMaxY(2);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public void requestMoreValues(){
        Requester requester = Requester.getInstance();
        requester.getPlotData(new DiagnosticCallBack() {
            @Override
            public void onDataRecieved(PlotDataResponse plotDataResponse) {
                int newSequenceNumber = plotDataResponse.getSequenceNumber();
                synchronized (fence) {
                    if(newSequenceNumber > sequenceNumber) {
                        queue.addAll(plotDataResponse.getValues());

                        sequenceNumber = plotDataResponse.getSequenceNumber();
                        message = plotDataResponse.getMessage();
                        mHandler.postDelayed(mTimer2, 1);
                    }
                    else {
                        mHandler.postDelayed(mTimer2, 1000);
                    }

                }
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        mTimer2 = new Runnable() {
            @Override
            public void run() {
                Double value =queue.poll();
                synchronized (fence){
                    engineStatus.setText(message);
                }

                if(value!=null){
                    graph2LastXValue += 1d;
                    mSeries2.appendData(new DataPoint(graph2LastXValue, value), true, 40);
                    mHandler.postDelayed(this, 1);
                }
                else{
                    requestMoreValues();
                }

            }
        };
        mHandler.postDelayed(mTimer2, 1000);
    }


    @Override
    public void onPause() {
        mHandler.removeCallbacks(mTimer2);
        super.onPause();
    }

    double mLastRandom = 2;
    Random mRand = new Random();
    private double getRandom() {
        return mLastRandom += mRand.nextDouble()*0.5 - 0.25;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
