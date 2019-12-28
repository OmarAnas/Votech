package com.example.votech;


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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FirstFragment extends Fragment {

    ListView pollsList;
    TextView noPolls;
    ArrayList<Polls> polls = new ArrayList<>();
    ArrayList<Integer> Pollids = new ArrayList<>();
    DataQueryBuilder dataQuery = DataQueryBuilder.create();
    String date;
    ArrayAdapter adapter;
    int groupID;
    int UserID;
    int userType;
    Context mContext;
    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_first, container, false);
        pollsList=view.findViewById(R.id.pollsList);

        date =new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());

        groupID= Integer.parseInt(Backendless.UserService.CurrentUser().getProperty("groupID").toString());
        UserID=Integer.parseInt(Backendless.UserService.CurrentUser().getProperty("id").toString());
        userType=Integer.parseInt(Backendless.UserService.CurrentUser().getProperty("UserTypeID").toString());

       if(userType==2) // student to get his poll according to his group
        getPollsGroups();
       if(userType==1) // instructor to get all polls he posted
        getPolls();

       noPolls=view.findViewById(R.id.noPolls);

       pollsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Polls p = (Polls)pollsList.getItemAtPosition(i);
               Intent in = new Intent(mContext,pollVote.class);
               in.putExtra("pollID",p.getId().toString());
               getActivity().finish();
               startActivity(in);
           }
       });

        return view;
    }
    public void getPolls() {

        dataQuery.setWhereClause("instructorID= "+UserID).setSortBy("startDate DESC").setPageSize(100);

        Backendless.Data.of(Polls.class).find(dataQuery,new AsyncCallback<List<Polls>>() {
            @Override
            public void handleResponse(List<Polls> response) {
                checkIfempty(response);
                for (int i=0;i<response.size();i++){
                    polls.add(response.get(i));
                }
                Log.i("response",response.size()+"");
                Log.i("ArrayList ",polls.size()+"");
                adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, polls){
                    @Override
                    public View getView(int position,  View convertView, ViewGroup parent) {
                        View view= super.getView(position, convertView, parent);

                        view.setBackground(mContext.getResources().getDrawable(R.drawable.list_view_item));
                        TextView textView=(TextView) view.findViewById(android.R.id.text1);
                        textView.setText(polls.get(position).getTitle());
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize(20F);
                        return view;
                    }
                };
                Log.i("Count",adapter.getCount()+"");
                pollsList.setAdapter(adapter);
            }
            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(mContext, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getPolls(ArrayList<Integer> ids) {
        String concatIds="";
        for (int i=0; i <ids.size();i++)
            if (i==(ids.size()-1))
                concatIds+=ids.get(i).toString();
            else
            concatIds+=ids.get(i).toString()+" , ";
        DataQueryBuilder dataQuery = DataQueryBuilder.create();

        if(ids.size()>0)
            dataQuery.setWhereClause("startDate <= '"+date+"' AND endDate >= '"+date+"' AND id IN("+concatIds+")")
                    .setSortBy("startDate DESC")
                    .setPageSize(100);

        Log.i("query",dataQuery.getWhereClause()+"");

            Backendless.Data.of(Polls.class).find(dataQuery,new AsyncCallback<List<Polls>>() {
            @Override
            public void handleResponse(List<Polls> response) {
                checkIfempty(response);
                for (int i=0;i<response.size();i++){
                   polls.add(response.get(i));
               }
                adapter = new ArrayAdapter(mContext,android.R.layout.simple_list_item_1, polls){
                    @Override
                    public View getView(int position,  View convertView, ViewGroup parent) {
                        View view= super.getView(position, convertView, parent);

                        view.setBackground(mContext.getResources().getDrawable(R.drawable.list_view_item));
                        TextView textView=(TextView) view.findViewById(android.R.id.text1);
                        textView.setText(polls.get(position).getTitle());
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize(20F);
                        return view;
                    }
                };
               pollsList.setAdapter(adapter);
            }
            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(mContext, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getPollsGroups() {
        DataQueryBuilder dataQuery = DataQueryBuilder.create();
        Backendless.Data.of(PollGroups.class).find(dataQuery.setWhereClause("groupID = "+groupID).setPageSize(100),new AsyncCallback<List<PollGroups>>() {
           @Override
           public void handleResponse(List<PollGroups> response) {
               for (int i = 0; i < response.size() ; i++) {
                   Pollids.add(response.get(i).getPollID());
               }
             getPolls(Pollids);
           }
           @Override
           public void handleFault(BackendlessFault fault) {
               Toast.makeText(mContext, fault.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });
   }

    public void checkIfempty(List e){
        if(e.size()==0){
            pollsList.setVisibility(View.GONE);
            noPolls.setVisibility(View.VISIBLE);
        }
   }
}
