
package com.example.votech;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;
import com.backendless.geo.GeoPoint;

import java.util.List;
import java.util.Date;

public class Polls
{
  private Date created;
  private String ownerId;
  private String Title;
  private Integer totalVotes;
  private String Description;
  private Integer Yes;
  private Integer No;
  private Integer Neutral;
  private Date startDate;
  private Date updated;
  private String objectId;
  private Integer instructorID;
  private Integer id;
  private Date endDate;
  public Date getCreated()
  {
    return created;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public String getTitle()
  {
    return Title;
  }

  public void setTitle( String Title )
  {
    this.Title = Title;
  }

  public Integer getTotalVotes()
  {
    return totalVotes;
  }

  public void setTotalVotes( Integer totalVotes )
  {
    this.totalVotes = totalVotes;
  }

  public String getDescription()
  {
    return Description;
  }

  public void setDescription( String Description )
  {
    this.Description = Description;
  }

  public Integer getYes()
  {
    return Yes;
  }

  public void setYes( Integer Yes )
  {
    this.Yes = Yes;
  }

  public Integer getNo()
  {
    return No;
  }

  public void setNo( Integer No )
  {
    this.No = No;
  }

  public Integer getNeutral()
  {
    return Neutral;
  }

  public void setNeutral( Integer Neutral )
  {
    this.Neutral = Neutral;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public void setStartDate( Date startDate )
  {
    this.startDate = startDate;
  }

  public Date getUpdated()
  {
    return updated;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public Integer getInstructorID()
  {
    return instructorID;
  }

  public void setInstructorID( Integer instructorID )
  {
    this.instructorID = instructorID;
  }

  public Integer getId()
  {
    return id;
  }

  public void setId( Integer id )
  {
    this.id = id;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public void setEndDate( Date endDate )
  {
    this.endDate = endDate;
  }

                                                    
  public Polls save()
  {
    return Backendless.Data.of( Polls.class ).save( this );
  }

  public void saveAsync( AsyncCallback<Polls> callback )
  {
    Backendless.Data.of( Polls.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Polls.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Polls.class ).remove( this, callback );
  }

  public static Polls findById( String id )
  {
    return Backendless.Data.of( Polls.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<Polls> callback )
  {
    Backendless.Data.of( Polls.class ).findById( id, callback );
  }

  public static Polls findFirst()
  {
    return Backendless.Data.of( Polls.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<Polls> callback )
  {
    Backendless.Data.of( Polls.class ).findFirst( callback );
  }

  public static Polls findLast()
  {
    return Backendless.Data.of( Polls.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<Polls> callback )
  {
    Backendless.Data.of( Polls.class ).findLast( callback );
  }

  public static List<Polls> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( Polls.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<Polls>> callback )
  {
    Backendless.Data.of( Polls.class ).find( queryBuilder, callback );
  }
}