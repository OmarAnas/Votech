
package com.example.votech;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;
import com.backendless.geo.GeoPoint;

import java.util.List;
import java.util.Date;

public class pollOptions
{
  private Integer optionCount;
  private Date updated;
  private String ownerId;
  private Integer pollID;
  private String optionName;
  private Integer id;
  private String objectId;
  private Date created;
  public Integer getOptionCount()
  {
    return optionCount;
  }

  public void setOptionCount( Integer optionCount )
  {
    this.optionCount = optionCount;
  }

  public Date getUpdated()
  {
    return updated;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public Integer getPollID()
  {
    return pollID;
  }

  public void setPollID( Integer pollID )
  {
    this.pollID = pollID;
  }

  public String getOptionName()
  {
    return optionName;
  }

  public void setOptionName( String optionName )
  {
    this.optionName = optionName;
  }

  public Integer getId()
  {
    return id;
  }

  public void setId( Integer id )
  {
    this.id = id;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public Date getCreated()
  {
    return created;
  }

                                                    
  public pollOptions save()
  {
    return Backendless.Data.of( pollOptions.class ).save( this );
  }

  public void saveAsync( AsyncCallback<pollOptions> callback )
  {
    Backendless.Data.of( pollOptions.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( pollOptions.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( pollOptions.class ).remove( this, callback );
  }

  public static pollOptions findById( String id )
  {
    return Backendless.Data.of( pollOptions.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<pollOptions> callback )
  {
    Backendless.Data.of( pollOptions.class ).findById( id, callback );
  }

  public static pollOptions findFirst()
  {
    return Backendless.Data.of( pollOptions.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<pollOptions> callback )
  {
    Backendless.Data.of( pollOptions.class ).findFirst( callback );
  }

  public static pollOptions findLast()
  {
    return Backendless.Data.of( pollOptions.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<pollOptions> callback )
  {
    Backendless.Data.of( pollOptions.class ).findLast( callback );
  }

  public static List<pollOptions> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( pollOptions.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<pollOptions>> callback )
  {
    Backendless.Data.of( pollOptions.class ).find( queryBuilder, callback );
  }
}