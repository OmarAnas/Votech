package com.example.votech;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class SecondFragment extends Fragment{

    Button createPoll;
    EditText title, desc, startDate, endDate;
    DataQueryBuilder queryBuilder = DataQueryBuilder.create();
    Spinner groupSpinner;


    ArrayList<groups> groups = new ArrayList<>();
    ArrayList<Integer> selectedGroupsID = new ArrayList<>();
    BackendlessUser user = Backendless.UserService.CurrentUser();
    int currentUserId = Integer.parseInt(user.getProperty("id").toString());
    int instructorFacultyID= Integer.parseInt(user.getProperty("FacultyID").toString());
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    Date stDate; //start date
    Date enDate; //end date
    int newPollId;
    Context mContext;
    DatePickerDialog datePickerDialog;
    public SecondFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
        Log.i("Attach","Attached");
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
//        groups.add("Select the target group(s)");
//        groups.add("All Groups");
//        groupSpinner.setAdapter(adapter);
        getGroups();

        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView) view;
                if(i==0) {
                    tv.setTextColor(Color.parseColor("#646873"));
                }
                else if(i==1){
                    selectedGroupsID.clear();
                    tv.setText("All groups");
//                    selectedGroupId = -2;
                    for (int x =2 ; x < adapterView.getCount() ; x++)
                    {
                        groups g= (groups) groupSpinner.getItemAtPosition(x);
                        selectedGroupsID.add(g.getId());
                    }
                }
                else {
                    selectedGroupsID.clear();
                    groups g= (groups) groupSpinner.getItemAtPosition(i);
                    tv.setText(g.getName());
                    tv.setTextColor(Color.WHITE);
                    selectedGroupsID.add(g.getId());
//                    selectedGroupId= g.getId();
//                    Backendless.Data.of(groups.class).find(queryBuilder.setWhereClause("Name= '" + tv.getText().toString()+"'"), new AsyncCallback<List<groups>>() {
//                        @Override
//                        public void handleResponse(List<groups> response) {
//                            selectedGroupId=response.get(0).getId();
//                        }
//
//                        @Override
//                        public void handleFault(BackendlessFault fault) {
//                        }
//                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               setStartDate();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startDate.getText().toString().equals(""))
                    Toast.makeText(getActivity(), "Please choose Start Date First", Toast.LENGTH_LONG).show();
                else
                    setEndDate();

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
            Backendless.Data.of(Polls.class).find(DataQueryBuilder.create().setProperties("Max(id) as id"),new AsyncCallback<List<Polls>>() {
                @Override
                public void handleResponse(List<Polls> response) {
                    Polls poll = new Polls();

                    newPollId = response.get(0).getId() + 1;
                    poll.setTitle(title.getText().toString());
                    poll.setDescription(desc.getText().toString());
                    poll.setId(newPollId);
                    poll.setInstructorID(currentUserId);
                    try {
                        stDate = formatter.parse(startDate.getText().toString());
                        enDate = formatter.parse(endDate.getText().toString());
                        Log.i("start date",stDate+"");
                        poll.setStartDate(stDate);
                        poll.setEndDate(enDate);
                    } catch (ParseException e) {
                        Toast.makeText(mContext, "try catch date: "+e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    Backendless.Persistence.of(Polls.class).save(poll, new AsyncCallback<Polls>() {
                        @Override
                        public void handleResponse(Polls response) {
                            Toast.makeText(mContext, "Poll created Successfully", Toast.LENGTH_LONG).show();

                            Intent in = new Intent(mContext,pollVote.class);
                            in.putExtra("pollID",newPollId+"");
                            startActivity(in);

                            Backendless.Data.of(PollGroups.class).find(DataQueryBuilder.create().setProperties("Max(id) as id"),new AsyncCallback<List<PollGroups>>() {
                                @Override
                                public void handleResponse(List<PollGroups> response) {
                                    for (int i = 0; i < selectedGroupsID.size(); i++) {
                                        PollGroups pg = new PollGroups();
                                        pg.setId(response.get(0).getId() + (i+1));
                                        pg.setPollID(newPollId);
                                        pg.setGroupID(selectedGroupsID.get(i));
                                        Backendless.Persistence.of(PollGroups.class).save(pg, new AsyncCallback<PollGroups>() {
                                            @Override
                                            public void handleResponse(PollGroups response) {
                                            }

                                            @Override
                                            public void handleFault(BackendlessFault fault) {
                                                Toast.makeText(mContext, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    selectedGroupsID.clear();
                                    ((Activity) mContext).finish();
                                }
                                @Override
                                public void handleFault(BackendlessFault fault) {
                                }
                            });

                        }
                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(mContext, fault.getMessage(), Toast.LENGTH_LONG).show();
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

        Backendless.Data.of(groups.class).find(queryBuilder.setWhereClause("FacultyID=" + instructorFacultyID).setSortBy("id DESC"), new AsyncCallback<List<groups>>() {
            @Override
            public void handleResponse(List<groups> groupsList) {
                groups g=new groups();
                g.setName("Select the target group(s)");
                groups.add(g);
                groups g2=new groups();
                g2.setName("All groups");
                groups.add(g2);
                for (int i = 0; i < groupsList.size(); i++)
                    groups.add(groupsList.get(i));

//                Log.i("Error",groups.get(0));
                ArrayAdapter adapter = new ArrayAdapter(mContext, R.layout.spinner_item, groups){


                    @Override
                    public View getView(int position,View convertView, ViewGroup parent) {
                        View view= super.getView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if(position == 0){
                            // Set the hint text color gray
                            tv.setText("Select the target group(s)");
                            tv.setTextColor(Color.parseColor("#646873"));

                        }
                        return view;
                    }

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
                            tv.setText("Select the target group(s)");
                            tv.setTextColor(Color.parseColor("#646873"));

                        }
                        else {
                            tv.setText(groups.get(position).getName());
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
    public void setStartDate() {
        Calendar calendar = Calendar.getInstance();

        datePickerDialog=  new DatePickerDialog(getActivity(),R.style.MyDialogTheme, new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            startDate.setText(day +"/"+ (month+1) + "/" +year);
        }
    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
    calendar.add(Calendar.MONTH, 2); //add two months for max date
    datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
    datePickerDialog.show();
   }
    public void setEndDate() {
        Calendar calendar = Calendar.getInstance();

        String startDatestr=startDate.getText().toString().trim();
        int startDateday =Integer.parseInt(startDatestr.split("/")[0]);
        int startDatemonth =Integer.parseInt(startDatestr.split("/")[1])-1;
        int startDateyear =Integer.parseInt(startDatestr.split("/")[2]);

        DatePickerDialog datePickerDialog=  new DatePickerDialog(getActivity(),R.style.MyDialogTheme, new DatePickerDialog.OnDateSetListener() {
           @Override
           public void onDateSet(DatePicker datePicker, int y, int m, int d) {
               endDate.setText(d +"/"+(m+1)+"/"+y);
           }
       }, startDateyear, startDatemonth, startDateday);


        calendar.set(Calendar.YEAR,startDateyear);
        calendar.set(Calendar.MONTH, startDatemonth);
        calendar.set(Calendar.DAY_OF_MONTH, startDateday);
       datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
       calendar.add(Calendar.MONTH, 6); //add 6 months for max date
       datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
       datePickerDialog.show();
   }
}
