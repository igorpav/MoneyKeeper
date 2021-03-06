package team.e.com.moneykeeper.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import team.e.com.moneykeeper.R;

public class StatsFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stats, container, false);
        PieChart mPieChart = (PieChart) v.findViewById(R.id.piechart);
        mPieChart.addPieSlice(new PieModel("Питание", 1000, Color.parseColor("#FE6DA8")));
        mPieChart.addPieSlice(new PieModel("Развлечения", 800, Color.parseColor("#56B7F1")));
        mPieChart.addPieSlice(new PieModel("Подарки", 1000, Color.parseColor("#CDA67F")));
        mPieChart.addPieSlice(new PieModel("Транспорт", 3000, Color.parseColor("#FED70E")));
        mPieChart.startAnimation();
        return v;
    }
}
