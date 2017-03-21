package sfu.cmpt276.carbontracker.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.User;

public class SingleDayActivity extends AppCompatActivity {
    List journeyList = User.getInstance().getJourneyList();
    //List utilityList = (List) User.getInstance().getUtilityList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_day);

        setupSingleDayPieGraph();
    }

    private void setupSingleDayPieGraph() {
        //first check the date: if selected date between startDate and endDate of a bill
        //use the avg data, if not, use nearest data, then check journey part

        String singleDayEmission[] = new String[journeyList.size() + User.getInstance().getUtilityList().countUtility()];
        List<PieEntry> pieEntries = new ArrayList<>();


    }
}
