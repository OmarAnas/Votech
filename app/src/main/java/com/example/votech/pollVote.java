package com.example.votech;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class pollVote extends AppCompatActivity {

    String pollID;
    int pollId;
    TextView title, description,startDate,endDate,yesStat,noStat,neutralStat;
    Button yesButton, noButton, neutralButton, deletePoll;
    ImageView editTitle,editDescription,editEndDate;
    ProgressBar yesBar,neutralBar,noBar;

    DateFormat Fromformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
    DateFormat Toformat = new SimpleDateFormat("dd/MM/yyyy");

    PollUser pollUser;
    Polls p;
    DataQueryBuilder queryBuilder = DataQueryBuilder.create();
    BackendlessUser user = Backendless.UserService.CurrentUser();
    int currentUserId = Integer.parseInt(user.getProperty("id").toString());
    int userType = Integer.parseInt(user.getProperty("UserTypeID").toString());

    AlertDialog dialog;
    AlertDialog.Builder dialogBuilder ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_vote);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        pollUser = new PollUser();
        p = new Polls();
        pollID=getIntent().getStringExtra("pollID");
        pollId = Integer.parseInt(pollID);
        title = findViewById(R.id.Title);
        description = findViewById(R.id.Description);
        yesButton = findViewById(R.id.Yes);
        noButton = findViewById(R.id.No);
        neutralButton = findViewById(R.id.Neutral);

        deletePoll=findViewById(R.id.deletePollBtn);
        editTitle=findViewById(R.id.editpollTitle);
        editDescription=findViewById(R.id.editDescriptionPen);
        editEndDate=findViewById(R.id.editEndDate);
        startDate=findViewById(R.id.startDateText);
        endDate=findViewById(R.id.endDateText);

        yesBar=findViewById(R.id.progressBar3);
        neutralBar=findViewById(R.id.progressNeutral);
        noBar=findViewById(R.id.progressNo);
        yesStat=findViewById(R.id.yesStat);
        noStat=findViewById(R.id.noStat);
        neutralStat=findViewById(R.id.neutralStat);

        Log.i("Poll ID: ",pollId+"");
        if(pollID.equals(null))
        {
            Intent in = new Intent(pollVote.this, Home.class);
            startActivity(in);
            pollVote.this.finish();
            return;
        }
        setBars();
        if (userType==1){ // if instructor
            deletePoll.setVisibility(View.VISIBLE);
            editTitle.setVisibility(View.VISIBLE);
            editDescription.setVisibility(View.VISIBLE);
            editEndDate.setVisibility(View.VISIBLE);

            yesBar.setVisibility(View.VISIBLE);
            neutralBar.setVisibility(View.VISIBLE);
            noBar.setVisibility(View.VISIBLE);
            yesStat.setVisibility(View.VISIBLE);
            noStat.setVisibility(View.VISIBLE);
            neutralStat.setVisibility(View.VISIBLE);

            yesButton.setVisibility(View.INVISIBLE);
            noButton.setVisibility(View.INVISIBLE);
            neutralButton.setVisibility(View.INVISIBLE);
        }

        dialogBuilder= new AlertDialog.Builder(this , R.style.MyDialogTheme);

        //For viewing details on page
        Backendless.Data.of(Polls.class).find(queryBuilder.setWhereClause("id = " + pollId), new AsyncCallback<List<Polls>>() {
            @Override
            public void handleResponse(List<Polls> poll) {
                if(poll.size()==0){
                    Intent in = new Intent(pollVote.this, Home.class);
                    startActivity(in);
                    pollVote.this.finish();
                    return;
                }

                title.setText(poll.get(0).getTitle());
                description.setText(poll.get(0).getDescription());
                try {
                    Date strt=Fromformat.parse(poll.get(0).getStartDate().toString());
                    Date end= Fromformat.parse(poll.get(0).getEndDate().toString());
                    startDate.setText(Toformat.format(strt));
                    endDate.setText(Toformat.format(end));
                } catch (ParseException e) {
                    Log.i("Date error:", e.getMessage());
                }

//                p.setTitle(poll.get(0).getTitle());
//                p.setDescription(poll.get(0).getDescription());
//                p.setId(pollId);
//                p.setInstructorID(poll.get(0).getInstructorID());

            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });

        yesButton.setClickable(false);
        noButton.setClickable(false);
        neutralButton.setClickable(false);

        String whereClause = "pollID = " + pollId + " AND UserID = " + currentUserId;
        Backendless.Data.of(PollUser.class).find(queryBuilder.setWhereClause(whereClause), new AsyncCallback<List<PollUser>>() {
            @Override
            public void handleResponse(List<PollUser> response) {
                if(response.size() > 0)
                {
                    yesButton.setVisibility(View.INVISIBLE);
                    noButton.setVisibility(View.INVISIBLE);
                    neutralButton.setVisibility(View.INVISIBLE);

                    yesBar.setVisibility(View.VISIBLE);
                    neutralBar.setVisibility(View.VISIBLE);
                    noBar.setVisibility(View.VISIBLE);
                    yesStat.setVisibility(View.VISIBLE);
                    noStat.setVisibility(View.VISIBLE);
                    neutralStat.setVisibility(View.VISIBLE);
                }
                else
                {
                    yesButton.setClickable(true);
                    noButton.setClickable(true);
                    neutralButton.setClickable(true);
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

    public void vote(View view) {
        int buttonId = view.getId();


        TextView question = new TextView(this);
        question.setTextColor(Color.WHITE);
        question.setPadding(50, 30, 20, 30);

        TextView title = new TextView(this);
        title.setPadding(20, 30, 20, 30);
        title.setTextSize(20F);
        title.setText("Confirm your Vote");
        title.setTextColor(Color.WHITE);

        if (buttonId == R.id.Yes)
        {        question.setText("Are you sure you want to vote YES ?");

            dialogBuilder.setView(question)
                    .setCustomTitle(title)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .setPositiveButton("I'm Sure" , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            yesButton.setClickable(false);
                            noButton.setClickable(false);
                            neutralButton.setClickable(false);
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
                                            Toast.makeText(pollVote.this, "Voted YES Successfully!", Toast.LENGTH_LONG).show();
                                            makeitVisibile();
                                            Backendless.Data.of(PollUser.class).find(DataQueryBuilder.create().setProperties("Max(id) as id"),new AsyncCallback<List<PollUser>>() {
                                                @Override
                                                public void handleResponse(List <PollUser> response) {
                                                    pollUser.setPollID(pollId);
                                                    pollUser.setId(response.get(0).getId() + 1);
                                                    pollUser.setUserID(currentUserId);
                                                    Backendless.Persistence.of(PollUser.class).save(pollUser, new AsyncCallback<PollUser>() {
                                                        @Override
                                                        public void handleResponse(PollUser response) { }

                                                        @Override
                                                        public void handleFault(BackendlessFault fault) { }
                                                    });
                                                }

                                                @Override
                                                public void handleFault(BackendlessFault fault) {
                                                    Toast.makeText(pollVote.this, "Error in voting", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                            Toast.makeText(pollVote.this, "Error in voting", Toast.LENGTH_LONG).show();
                                            Log.i("Voting Error YES: ",fault.getMessage());
                                        }
                                    });
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {

                                }
                            });
                        }
                    });
        }
        else if(buttonId == R.id.No)
        {
            question.setText("Are you sure you want to vote NO ?");

            dialogBuilder.setView(question)
                    .setCustomTitle(title)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .setPositiveButton("I'm Sure" , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            yesButton.setClickable(false);
                            noButton.setClickable(false);
                            neutralButton.setClickable(false);
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
                                            Toast.makeText(pollVote.this, "Voted NO Successfully!", Toast.LENGTH_LONG).show();
                                            makeitVisibile();
                                            Backendless.Data.of(PollUser.class).find(DataQueryBuilder.create().setProperties("Max(id) as id"),new AsyncCallback<List<PollUser>>() {
                                                @Override
                                                public void handleResponse(List <PollUser> response) {
                                                    pollUser.setPollID(pollId);
                                                    pollUser.setId(response.get(0).getId() + 1);
                                                    pollUser.setUserID(currentUserId);
                                                    Backendless.Persistence.of(PollUser.class).save(pollUser, new AsyncCallback<PollUser>() {
                                                        @Override
                                                        public void handleResponse(PollUser response) { }

                                                        @Override
                                                        public void handleFault(BackendlessFault fault) { }
                                                    });
                                                }

                                                @Override
                                                public void handleFault(BackendlessFault fault) {
                                                    Toast.makeText(pollVote.this, "Error in voting", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                            Toast.makeText(pollVote.this, "Error in voting", Toast.LENGTH_LONG).show();
                                            Log.i("Voting Error NO: ",fault.getMessage());
                                        }
                                    });
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {

                                }
                            });
                        }
                    });
        }
        else if(buttonId == R.id.Neutral)
        {
            question.setText("Are you sure you want to vote NEUTRAL ?");

            dialogBuilder.setView(question)
                    .setCustomTitle(title)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .setPositiveButton("I'm Sure" , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            yesButton.setClickable(false);
                            noButton.setClickable(false);
                            neutralButton.setClickable(false);
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
                                            Toast.makeText(pollVote.this, "Voted Neutral Successfully!", Toast.LENGTH_LONG).show();
                                            makeitVisibile();
                                            Backendless.Data.of(PollUser.class).find(DataQueryBuilder.create().setProperties("Max(id) as id"),new AsyncCallback<List<PollUser>>() {
                                                @Override
                                                public void handleResponse(List <PollUser> response) {
                                                    pollUser.setPollID(pollId);
                                                    pollUser.setId(response.get(0).getId() + 1);
                                                    pollUser.setUserID(currentUserId);
                                                    Backendless.Persistence.of(PollUser.class).save(pollUser, new AsyncCallback<PollUser>() {
                                                        @Override
                                                        public void handleResponse(PollUser response) { }

                                                        @Override
                                                        public void handleFault(BackendlessFault fault) { }
                                                    });
                                                }

                                                @Override
                                                public void handleFault(BackendlessFault fault) {
                                                    Toast.makeText(pollVote.this, "Error in voting", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                            Toast.makeText(pollVote.this, "Error in voting", Toast.LENGTH_LONG).show();
                                            Log.i("Voting Error Neutral: ",fault.getMessage());
                                        }
                                    });
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {

                                }
                            });
                        }
                    });

        }
        dialog = dialogBuilder.create();
        dialog.show();
    }

    public void editEndDate(View view) {
        Calendar calendar = Calendar.getInstance();

        String endDatestr=endDate.getText().toString().trim();
        int endDateday =Integer.parseInt(endDatestr.split("/")[0]);
        int endDatemonth =Integer.parseInt(endDatestr.split("/")[1])-1;
        int endDateyear =Integer.parseInt(endDatestr.split("/")[2]);

        DatePickerDialog datePickerDialog=  new DatePickerDialog(this,R.style.MyDialogTheme,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                endDate.setText(d +"/"+(m+1)+"/"+y);
                Date date=new Date();
                try {
                     date= Toformat.parse(d +"/"+(m+1)+"/"+y);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                p.setEndDate(date);
                Map<String,Object> changes=new  HashMap<>();
                changes.put("endDate", p.getEndDate());
                Backendless.Data.of(Polls.class).update("id = " + pollId, changes, new AsyncCallback<Integer>() {


                    @Override
                    public void handleResponse(Integer response) {
                        Toast.makeText(pollVote.this, "End Date Updated Successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                    }
                });
            }
        }, endDateyear, endDatemonth, endDateday);

        String startDatestr=startDate.getText().toString().trim();
        int startDateday =Integer.parseInt(startDatestr.split("/")[0]);
        int startDatemonth =Integer.parseInt(startDatestr.split("/")[1])-1;
        int startDateyear =Integer.parseInt(startDatestr.split("/")[2]);

        calendar.set(Calendar.YEAR,startDateyear);
        calendar.set(Calendar.MONTH, startDatemonth);
        calendar.set(Calendar.DAY_OF_MONTH, startDateday);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        calendar.add(Calendar.MONTH, 6); //add 6 months for max date
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    public void editDesc(View view) {
        final EditText changeDesc = new EditText(this);
        changeDesc.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        changeDesc.setSingleLine(false);
        changeDesc.setLines(5);
        changeDesc.setText(description.getText().toString());
        changeDesc.setSelection(changeDesc.getText().length());
        changeDesc.setTextColor(Color.WHITE);
        changeDesc.setHint("Poll Description");
        changeDesc.setHintTextColor(Color.parseColor("#646873"));

        TextView title = new TextView(this);
        title.setPadding(20, 30, 20, 30);
        title.setTextSize(20F);
        title.setText("Change Poll Description");
        title.setTextColor(Color.WHITE);
        dialogBuilder.setView(changeDesc)
                .setCustomTitle(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         String desc= changeDesc.getText().toString().replaceAll("\\s{2,}", " ").trim();
                        if (isSpecialCharacter(desc))
                            Toast.makeText(pollVote.this, "Please don't use special characters", Toast.LENGTH_LONG).show();
                        else if (desc.length() < 6 || desc.length() > 800)
                            Toast.makeText(pollVote.this, "Description cannot be less than 6 characters or more than 300 characters", Toast.LENGTH_LONG).show();
                        else{
                         description.setText(desc);
                         p.setDescription(desc);
                        Map<String,Object> changes=new  HashMap<>();
                        changes.put("Description", p.getDescription());
                        Backendless.Data.of(Polls.class).update("id = " + pollId, changes, new AsyncCallback<Integer>() {
                            @Override
                            public void handleResponse(Integer response) {
                                Toast.makeText(pollVote.this, "Description Updated Successfully", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(pollVote.this, fault.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
        dialog = dialogBuilder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        changeDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable))
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                else
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
            }
        });
    }

    public void editTitle(View view) {
        final EditText changeTitle = new EditText(this);
        changeTitle.setText(title.getText().toString());
        changeTitle.setSelection(changeTitle.getText().length());
        changeTitle.setTextColor(Color.WHITE);
        changeTitle.setHint("Poll Title");
        changeTitle.setHintTextColor(Color.parseColor("#646873"));

        final TextView dialogTitle = new TextView(this);
        dialogTitle.setPadding(20, 30, 20, 30);
        dialogTitle.setTextSize(20F);
        dialogTitle.setText("Change Poll Title");
        dialogTitle.setTextColor(Color.WHITE);
        dialogBuilder.setView(changeTitle)
                .setCustomTitle(dialogTitle)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String titolo = changeTitle.getText().toString().replaceAll("\\s{2,}", " ").trim();
                        if (isSpecialCharacter(titolo))
                            Toast.makeText(pollVote.this, "Please don't use special characters", Toast.LENGTH_LONG).show();
                        else if (titolo.length() < 3 || titolo.length() > 50)
                            Toast.makeText(pollVote.this, "Title cannot be less than 3 characters or more than 50 characters", Toast.LENGTH_LONG).show();
                        else{
                            title.setText(titolo);
                        p.setTitle(titolo);
                        Map<String, Object> changes = new HashMap<>();
                        changes.put("Title", p.getTitle());
                        Backendless.Data.of(Polls.class).update("id = " + pollId, changes, new AsyncCallback<Integer>() {
                            @Override
                            public void handleResponse(Integer response) {
                                Toast.makeText(pollVote.this, "Title Updated Successfully", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(pollVote.this, fault.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
        dialog = dialogBuilder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        changeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable))
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                else
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
            }
        });
    }

    public void deletePoll(View view) {
        TextView question = new TextView(this);
        question.setTextColor(Color.WHITE);
        question.setPadding(50, 30, 20, 30);
        question.setText("Are you Sure you want to Delete this Poll? \n " +
                         "NOTE: This action cannot be undone");
        TextView title = new TextView(this);
        title.setPadding(20, 30, 20, 30);
        title.setTextSize(20F);
        title.setText("Delete Poll");
        title.setTextColor(Color.WHITE);

        dialogBuilder.setView(question)
                .setCustomTitle(title)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("I'm Sure" , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent in = new Intent(pollVote.this, Home.class);
                        startActivity(in);
                        pollVote.this.finish();

                     Backendless.Data.of(Polls.class).remove("id= " + pollId, new AsyncCallback<Integer>() {
                         @Override
                         public void handleResponse(Integer response) {
                             Toast.makeText(pollVote.this, "Poll Deleted Successfully!", Toast.LENGTH_SHORT).show();
                             Backendless.Data.of(PollUser.class).remove("pollID = " + pollId, new AsyncCallback<Integer>() {
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
                });
        dialog = dialogBuilder.create();
        dialog.show();
    }

    public void setBars(){
        Backendless.Data.of(Polls.class).find(queryBuilder.setWhereClause("id = " + pollId), new AsyncCallback<List<Polls>>() {
            @Override
            public void handleResponse(List<Polls> poll) {
                if(poll.size()==0){
                    Intent in = new Intent(pollVote.this, Home.class);
                    startActivity(in);
                    pollVote.this.finish();
                    return;
                }


                double totalVotes= poll.get(0).getTotalVotes();
                double YesVotes= poll.get(0).getYes();
                double NeutralVotes=poll.get(0).getNeutral();
                double NoVotes=poll.get(0).getNo();

                double YesPercentage=0;
                double NeutralPercentage=0;
                double NoPercentage=0;

                if(totalVotes==0)
                {
                     YesPercentage=0;
                     NeutralPercentage=0;
                     NoPercentage=0;
                }
                else {
                     YesPercentage= (YesVotes/totalVotes)*100;
                     NeutralPercentage= (NeutralVotes/totalVotes)*100;
                     NoPercentage= (NoVotes/totalVotes)*100;
                }
                YesPercentage= Double.parseDouble(new DecimalFormat("#.#").format(YesPercentage));
                NeutralPercentage= Double.parseDouble(new DecimalFormat("#.#").format(NeutralPercentage));
                NoPercentage= Double.parseDouble(new DecimalFormat("#.#").format(NoPercentage));

                yesBar.setProgress((int)YesPercentage+1);
                neutralBar.setProgress((int)NeutralPercentage+1);
                noBar.setProgress((int) (NoPercentage+1));

                yesStat.setText("Yes ("+(int)YesVotes+"/"+(int)totalVotes+"): "+YesPercentage+"%");
                neutralStat.setText("Neutral ("+(int)NeutralVotes+"/"+(int)totalVotes+"): "+NeutralPercentage+"%");
                noStat.setText("No ("+(int)NoVotes+"/"+(int)totalVotes+"): "+NoPercentage+"%");

            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

    public void makeitVisibile(){
        setBars();
        YoYo.with(Techniques.FadeOut).duration(700).playOn(yesButton);
        YoYo.with(Techniques.FadeOut).duration(700).playOn(noButton);
        YoYo.with(Techniques.FadeOut).duration(700).playOn(neutralButton);


        yesBar.setVisibility(View.VISIBLE);
        neutralBar.setVisibility(View.VISIBLE);
        noBar.setVisibility(View.VISIBLE);
        yesStat.setVisibility(View.VISIBLE);
        noStat.setVisibility(View.VISIBLE);
        neutralStat.setVisibility(View.VISIBLE);

        YoYo.with(Techniques.FadeIn).duration(1000).playOn(yesBar);
        YoYo.with(Techniques.FadeIn).duration(1000).playOn(yesStat);

        YoYo.with(Techniques.FadeIn).duration(1000).playOn(neutralBar);
        YoYo.with(Techniques.FadeIn).duration(1000).playOn(neutralStat);

        YoYo.with(Techniques.FadeIn).duration(1000).playOn(noBar);
        YoYo.with(Techniques.FadeIn).duration(1000).playOn(noStat);

    }

    public boolean isSpecialCharacter(String s) {

        Pattern p = Pattern.compile("[^A-Za-z0-9 _?}{.;:]");
        Matcher m = p.matcher(s);

        boolean b = m.find();
        if (b)
            return true;
        else
            return false;

    }
}
