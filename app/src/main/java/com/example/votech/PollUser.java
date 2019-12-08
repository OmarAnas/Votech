
package com.example.votech;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;
import com.backendless.geo.GeoPoint;

import java.util.List;
import java.util.Date;

public class PollUser
{
  private String objectId;
  private Date created;
  private String ownerId;
  private Date updated;
  private Integer pollID;
  private Integer id;
  private Integer UserID;
  public String getObjectId()
  {
    return objectId;
  }

  public Date getCreated()
  {
    return created;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public Date getUpdated()
  {
    return updated;
  }

  public Integer getPollID()
  {
    return pollID;
  }

  public void setPollID( Integer pollID )
  {
    this.pollID = pollID;
  }

  public Integer getId()
  {
    return id;
  }

  public void setId( Integer id )
  {
    this.id = id;
  }

  public Integer getUserID()
  {
    return UserID;
  }

  public void setUserID( Integer UserID )
  {
    this.UserID = UserID;
  }

                                                    
  public PollUser save()
  {
    return Backendless.Data.of( PollUser.class ).save( this );
  }

  public void saveAsync( AsyncCallback<PollUser> callback )
  {
    Backendless.Data.of( PollUser.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( PollUser.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( PollUser.class ).remove( this, callback );
  }

  public static PollUser findById( String id )
  {
    return Backendless.Data.of( PollUser.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<PollUser> callback )
  {
    Backendless.Data.of( PollUser.class ).findById( id, callback );
  }

  public static PollUser findFirst()
  {
    return Backendless.Data.of( PollUser.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<PollUser> callback )
  {
    Backendless.Data.of( PollUser.class ).findFirst( callback );
  }

  public static PollUser findLast()
  {
    return Backendless.Data.of( PollUser.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<PollUser> callback )
  {
    Backendless.Data.of( PollUser.class ).findLast( callback );
  }

  public static List<PollUser> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( PollUser.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<PollUser>> callback )
  {
    Backendless.Data.of( PollUser.class ).find( queryBuilder, callback );
  }
}