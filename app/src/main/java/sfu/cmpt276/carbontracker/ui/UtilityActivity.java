package sfu.cmpt276.carbontracker.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import java.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.User;
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
    int mode;

    Date date1 = new Date(2017 - 1900, 2 - 1, 8);
    Date date2 = new Date(2017 - 1900, 2 - 1, 28);
    Date date3 = new Date(2017 - 1900, 1 - 1, 1);
    Date date4 = new Date(2017 - 1900, 1 - 1, 19);


    private UtilityList myUtility = User.getInstance().getUtilityList();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility);

        //myUtility.addUtility(new Utility("gas", date1, date2, 189.23, 4, 20, 450.76, 190.34));
        //myUtility.addUtility(new Utility("electricity", date3, date4, 89.31, 3, 18, 1265.8, 1180.19));

        populateListView();
        setupAddBtn();
        registerClickCallback();

    }

    private void setupAddBtn() {
        Button addBtn = (Button) findViewById(R.id.addBillList);
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
    protected void onResume() {
        super.onResume();
        populateListView();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 10:
                if (resultCode == Activity.RESULT_OK) {
                    Utility utility = new Utility(tempType, startDate, endDate, amount, people, period, currentAvg, previousAvg);
                    myUtility.addUtility(utility);
                    populateListView();
                    break;
                }
            case 200:
                if (resultCode == Activity.RESULT_OK) {

                    Utility editUtility = new Utility(tempType, startDate, endDate, amount, people, period, currentAvg, previousAvg);
                    //myUtility.editUtility(editUtility, edit_position);
                    User.getInstance().EditUtilityIntoUtilityList(edit_position, editUtility);
                    populateListView();
                    break;
                }

                else if (resultCode == Activity.RESULT_FIRST_USER) {
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
                mode = 10;
                Intent intent = BillActivity.makeIntent(UtilityActivity.this);
                intent.putExtra("pos", edit_position);
                intent.putExtra("mode", mode);
                //startActivityForResult(intent, 200);
                startActivity(intent);
                Toast.makeText(UtilityActivity.this, "" + edit_position, Toast.LENGTH_SHORT).show();

                return true;
            }
        });

    }

}
