
package com.example.votech;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;
import com.backendless.geo.GeoPoint;

import java.util.List;
import java.util.Date;

public class PollGroups
{
  private Date created;
  private String objectId;
  private String ownerId;
  private Integer id;
  private Integer groupID;
  private Date updated;
  private Integer pollID;
  public Date getCreated()
  {
    return created;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public Integer getId()
  {
    return id;
  }

  public void setId( Integer id )
  {
    this.id = id;
  }

  public Integer getGroupID()
  {
    return groupID;
  }

  public void setGroupID( Integer groupID )
  {
    this.groupID = groupID;
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

                                                    
  public PollGroups save()
  {
    return Backendless.Data.of( PollGroups.class ).save( this );
  }

  public void saveAsync( AsyncCallback<PollGroups> callback )
  {
    Backendless.Data.of( PollGroups.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( PollGroups.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( PollGroups.class ).remove( this, callback );
  }

  public static PollGroups findById( String id )
  {
    return Backendless.Data.of( PollGroups.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<PollGroups> callback )
  {
    Backendless.Data.of( PollGroups.class ).findById( id, callback );
  }

  public static PollGroups findFirst()
  {
    return Backendless.Data.of( PollGroups.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<PollGroups> callback )
  {
    Backendless.Data.of( PollGroups.class ).findFirst( callback );
  }

  public static PollGroups findLast()
  {
    return Backendless.Data.of( PollGroups.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<PollGroups> callback )
  {
    Backendless.Data.of( PollGroups.class ).findLast( callback );
  }

  public static List<PollGroups> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( PollGroups.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<PollGroups>> callback )
  {
    Backendless.Data.of( PollGroups.class ).find( queryBuilder, callback );
  }
}