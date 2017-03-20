package sfu.cmpt276.carbontracker.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.Utility;
import sfu.cmpt276.carbontracker.carbonmodel.UtilityList;

public class UtilityActivity extends AppCompatActivity {
    String tempType;
    Date startDate;
    Date endDate;
    double amount;
    int people;
    int period;
    double currentAvg;
    double previousAvg;
    ListView list;
    int edit_position;

    Date date1 = new Date(2017, 2, 8);
    Date date2 = new Date(2017, 2, 28);
    Date date3 = new Date(2017, 1, 1);
    Date date4 = new Date(2017, 1, 19);


    private UtilityList myUtility = new UtilityList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility);

        myUtility.addUtility(new Utility("gas", date1, date2, 189.23, 4, 20, 450.76, 190.34));
        myUtility.addUtility(new Utility("electricity", date3, date4, 89.31, 3, 18, 1265.8, 1180.19));


        populateListView();
        setupAddBtn();
        registerClickCallback();
    }


    private void setupAddBtn() {
        Button addBtn = (Button)findViewById(R.id.addBillList);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = BillActivity.makeIntent(UtilityActivity.this);
                startActivityForResult(intent, 10);
            }
        });
    }

    private void populateListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,                       // Context for the activity
                R.layout.bill_item,          // Layout to use (create)
                myUtility.getUtilityDescription());            // Items to be displayed

        // Configure the list view
        list = (ListView) findViewById(R.id.utilityList);
        list.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case 10:
                if(resultCode == Activity.RESULT_OK){
                    tempType = BillActivity.getTypeName(data);
                    Toast.makeText(this, "" + tempType, Toast.LENGTH_SHORT).show();
                    String str_startDate = BillActivity.getStartDate(data);
                    String str_endDate = BillActivity.getEndDate(data);

                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                    try {
                        startDate = df.parse(str_startDate);
                        endDate = df.parse(str_endDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String str_amount = BillActivity.getUsed(data);
                    amount = Double.valueOf(str_amount);
                    String str_people = BillActivity.getPeople(data);
                    people = Integer.parseInt(str_people);

                    String str_period = BillActivity.getPeriod(data);
                    period = Integer.parseInt(str_period);
                    String str_currentAvg = BillActivity.getCurrentAvg(data);
                    currentAvg = Double.valueOf(str_currentAvg);
                    String str_previousAvg = BillActivity.getPreviousAvg(data);
                    previousAvg = Double.valueOf(str_previousAvg);

                    Utility utility = new Utility(tempType, startDate, endDate, amount, people, period, currentAvg, previousAvg);
                    myUtility.addUtility(utility);
                    populateListView();
                    break;
                }
            case 200:
                if(resultCode == Activity.RESULT_OK){
                    tempType = BillActivity.getTypeName(data);
                    Toast.makeText(this, "" + tempType, Toast.LENGTH_SHORT).show();
                    String str_startDate = BillActivity.getStartDate(data);
                    String str_endDate = BillActivity.getEndDate(data);

                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                    try {
                        startDate = df.parse(str_startDate);
                        endDate = df.parse(str_endDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String str_amount = BillActivity.getUsed(data);
                    amount = Double.valueOf(str_amount);
                    String str_people = BillActivity.getPeople(data);
                    people = Integer.parseInt(str_people);

                    String str_period = BillActivity.getPeriod(data);
                    period = Integer.parseInt(str_period);
                    String str_currentAvg = BillActivity.getCurrentAvg(data);
                    currentAvg = Double.valueOf(str_currentAvg);
                    String str_previousAvg = BillActivity.getPreviousAvg(data);
                    previousAvg = Double.valueOf(str_previousAvg);

                    Utility editUtility = new Utility(tempType, startDate, endDate, amount, people, period, currentAvg, previousAvg);
                    myUtility.editUtility(editUtility, edit_position);
                    populateListView();
                    break;
                }
                else if(resultCode == Activity.RESULT_FIRST_USER){
                    myUtility.removeUtility(edit_position);
                    populateListView();
                    break;
                }

        }

    }

    private void registerClickCallback() {
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                edit_position = position;
                Intent intent = BillActivity.makeIntent(UtilityActivity.this);
                startActivityForResult(intent, 200);

                return true;
            }
        });

    }
}
