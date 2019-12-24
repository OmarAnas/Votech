package com.example.votech;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class pollVote extends AppCompatActivity {

    String pollID;
    int pollId;
    TextView title, description;
    Button yesButton, noButton, neutralButton;
    PollUser vote;
    Polls p;
    DataQueryBuilder queryBuilder = DataQueryBuilder.create();
    BackendlessUser user = Backendless.UserService.CurrentUser();
    int currentUserId = Integer.parseInt(user.getProperty("id").toString());
    int userType = Integer.parseInt(user.getProperty("UserTypeID").toString());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_vote);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        vote = new PollUser();
        p = new Polls();
        pollID=getIntent().getStringExtra("pollID");
        pollId = Integer.parseInt(pollID);
        title = findViewById(R.id.Title);
        description = findViewById(R.id.Description);
        yesButton = findViewById(R.id.Yes);
        noButton = findViewById(R.id.No);
        neutralButton = findViewById(R.id.Neutral);
        vote.setPollID(pollId);
        String whereClause = "pollID = " + pollId + " AND UserID = " + currentUserId;
        Backendless.Data.of(Polls.class).find(queryBuilder.setWhereClause("id = " + pollId), new AsyncCallback<List<Polls>>() {
            @Override
            public void handleResponse(List<Polls> poll) {
                title.setText(poll.get(0).getTitle());
                description.setText(poll.get(0).getDescription());
                p.setTitle(poll.get(0).getTitle());
                p.setDescription(poll.get(0).getDescription());
                p.setId(pollId);
                p.setInstructorID(poll.get(0).getInstructorID());
                p.setStartDate(poll.get(0).getStartDate());
                p.setEndDate(poll.get(0).getEndDate());
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
        Backendless.Data.of(PollUser.class).find(queryBuilder.setWhereClause(whereClause), new AsyncCallback<List<PollUser>>() {
            @Override
            public void handleResponse(List<PollUser> response) {
                if(response.size() > 0 || userType == 1)
                {
                    yesButton.setClickable(false);
                    noButton.setClickable(false);
                    neutralButton.setClickable(false);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent in = new Intent(this, Home.class);
                startActivity(in);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(this, Home.class);
        startActivity(in);
        this.finish();
    }

    public void vote(View view)
    {
        int buttonId = view.getId();
        Toast.makeText(this, "Voted Successfully!", Toast.LENGTH_LONG).show();
        yesButton.setClickable(false);
        noButton.setClickable(false);
        neutralButton.setClickable(false);
        Backendless.Data.of(PollUser.class).getObjectCount(new AsyncCallback<Integer>() {
            @Override
            public void handleResponse(Integer count) {
                vote.setId(count + 1);
                vote.setUserID(currentUserId);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
        Backendless.Persistence.of(PollUser.class).save(vote, new AsyncCallback<PollUser>() {
            @Override
            public void handleResponse(PollUser response) {

            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
        if (buttonId == R.id.Yes)
        {
            Backendless.Data.of(Polls.class).find(queryBuilder.setWhereClause("id = " + pollId), new AsyncCallback<List<Polls>>() {
                @Override
                public void handleResponse(List<Polls> poll) {
                    int currentYes = poll.get(0).getYes();
                    int currentTotal = poll.get(0).getTotalVotes();
                    p.setYes(currentYes + 1);
                    p.setTotalVotes(currentTotal + 1);
                    Map<String,Object> changes=new  HashMap<>();
                    changes.put("Yes", p.getYes());
                    changes.put("totalVotes", p.getTotalVotes());
                    Backendless.Data.of(Polls.class).update("id = " + pollId, changes, new AsyncCallback<Integer>() {
                        @Override
                        public void handleResponse(Integer response) {


                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                        }
                    });
                }

                @Override
                public void handleFault(BackendlessFault fault) {

                }
            });
        }
        else if(buttonId == R.id.No)
        {
            Backendless.Data.of(Polls.class).find(queryBuilder.setWhereClause("id = " + pollId), new AsyncCallback<List<Polls>>() {
                @Override
                public void handleResponse(List<Polls> poll) {
                    int currentNo = poll.get(0).getNo();
                    int currentTotal = poll.get(0).getTotalVotes();
                    p.setNo(currentNo + 1);
                    p.setTotalVotes(currentTotal + 1);
                    Map<String,Object> changes=new  HashMap<>();
                    changes.put("No", p.getNo());
                    changes.put("totalVotes", p.getTotalVotes());
                    Backendless.Data.of(Polls.class).update("id = " + pollId, changes, new AsyncCallback<Integer>() {
                        @Override
                        public void handleResponse(Integer response) {


                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                        }
                    });
                }

                @Override
                public void handleFault(BackendlessFault fault) {

                }
            });
        }
        else if(buttonId == R.id.Neutral)
        {
            Backendless.Data.of(Polls.class).find(queryBuilder.setWhereClause("id = " + pollId), new AsyncCallback<List<Polls>>() {
                @Override
                public void handleResponse(List<Polls> poll) {
                    int currentNeutral = poll.get(0).getNeutral();
                    int currentTotal = poll.get(0).getTotalVotes();
                    p.setNeutral(currentNeutral + 1);
                    p.setTotalVotes(currentTotal + 1);
                    Map<String,Object> changes=new  HashMap<>();
                    changes.put("Neutral", p.getNeutral());
                    changes.put("totalVotes", p.getTotalVotes());
                   Backendless.Data.of(Polls.class).update("id = " + pollId, changes, new AsyncCallback<Integer>() {
                       @Override
                       public void handleResponse(Integer response) {


                       }

                       @Override
                       public void handleFault(BackendlessFault fault) {

                       }
                   });
                }

                @Override
                public void handleFault(BackendlessFault fault) {

                }
            });
        }

    }
}
