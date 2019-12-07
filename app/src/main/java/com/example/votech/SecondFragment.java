package com.example.votech;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SecondFragment extends Fragment{

    Button createPoll;
    EditText title, desc, startDate, endDate;
    DataQueryBuilder queryBuilder = DataQueryBuilder.create();
    Spinner groupSpinner;
    Polls poll;
    PollGroups pg;
    ArrayList<String> groups = new ArrayList<>();
    ArrayAdapter adapter;
    BackendlessUser user = Backendless.UserService.CurrentUser();
    int currentUserId = Integer.parseInt(user.getProperty("id").toString());
    DateFormat formatter = new SimpleDateFormat("mm/dd/yyyy");
    Date stDate; //start date
    Date enDate; //end date
    int newPollId;
    int selectedGroupId;
    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_second, container, false);
        createPoll = view.findViewById(R.id.createPollButton);
        title = view.findViewById(R.id.titleInput);
        desc = view.findViewById(R.id.descInput);
        startDate = view.findViewById(R.id.startDateInput);
        endDate = view.findViewById(R.id.endDateInput);
        groupSpinner = view.findViewById(R.id.groupSpinner);
        createPoll = view.findViewById(R.id.createPollButton);
        createPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPoll();
            }
        });
        groups.add("Select the target group(s)");
        groups.add("All Groups");
        groupSpinner.setAdapter(adapter);
        getGroups();

        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView) view;
                if(i==0) {
                    tv.setTextColor(Color.parseColor("#646873"));
                }
                else if(tv.getText().toString().equals("All Groups")){
                    selectedGroupId = -2;
                }
                else {
                    tv.setTextColor(Color.WHITE);

                    Backendless.Data.of(groups.class).find(queryBuilder.setWhereClause("Name= '" + tv.getText().toString()+"'"), new AsyncCallback<List<groups>>() {
                        @Override
                        public void handleResponse(List<groups> response) {
                            selectedGroupId=response.get(0).getId();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    public void createPoll() {
        if(title.getText().toString().equals("")){
            Toast.makeText(getContext(), "Title is required", Toast.LENGTH_LONG).show();
        }
        else if(desc.getText().toString().equals("")){
            Toast.makeText(getContext(), "Description is required", Toast.LENGTH_LONG).show();
        }
        else if(startDate.getText().toString().equals("")){
            Toast.makeText(getContext(), "Start date is required", Toast.LENGTH_LONG).show();
        }
        else if(endDate.getText().toString().equals("")){
            Toast.makeText(getContext(), "End date is required", Toast.LENGTH_LONG).show();
        }
        else if(groupSpinner.getSelectedItemPosition()==0){
            Toast.makeText(getContext(), "Target group(s) is required", Toast.LENGTH_LONG).show();
        }
        else{
            Backendless.Data.of(Polls.class).getObjectCount(new AsyncCallback<Integer>() {
                @Override
                public void handleResponse(Integer count) {
                    poll = new Polls();
                    pg = new PollGroups();
                    newPollId = count + 1;
                    poll.setTitle(title.getText().toString());
                    poll.setDescription(desc.getText().toString());
                    poll.setId(newPollId);
                    poll.setInstructorID(Integer.parseInt(user.getProperty("id").toString()));
                    try {
                        stDate = formatter.parse(startDate.getText().toString());
                        enDate = formatter.parse(endDate.getText().toString());
                        poll.setStartDate(stDate);
                        poll.setEndDate(enDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    pg.setPollID(newPollId);
                    pg.setGroupID(selectedGroupId);

                    Backendless.Data.of(PollGroups.class).getObjectCount(new AsyncCallback<Integer>() {
                        @Override
                        public void handleResponse(Integer count) {
                            pg.setId(count + 1);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                        }
                    });
                    Backendless.Persistence.of(Polls.class).save(poll, new AsyncCallback<Polls>() {
                        @Override
                        public void handleResponse(Polls response) {

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                        }
                    });
                    Backendless.Persistence.of(PollGroups.class).save(pg, new AsyncCallback<PollGroups>() {
                        @Override
                        public void handleResponse(PollGroups response) {

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                        }
                    });
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(getContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    public void getGroups() {

        Backendless.Data.of(groups.class).find(queryBuilder.setWhereClause("FacultyID=" + currentUserId).setSortBy("id DESC"), new AsyncCallback<List<groups>>() {
            @Override
            public void handleResponse(List<groups> groupsList) {
                for (int i = 0; i < groupsList.size(); i++)
                    groups.add(groupsList.get(i).getName());

                ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.spinner_item, groups){
                    @Override
                    public boolean isEnabled(int position){
                        if(position == 0)
                            return false;
                        else
                            return true;
                    }
                    @Override
                    public View getDropDownView(int position, View convertView,ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if(position == 0){
                            // Set the hint text color gray
                            tv.setTextColor(Color.parseColor("#646873"));
                        }
                        else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                groupSpinner.setAdapter(adapter);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(getContext(), "" + fault.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
