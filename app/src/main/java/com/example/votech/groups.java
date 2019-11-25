
package com.example.votech;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;
import com.backendless.geo.GeoPoint;

import java.util.List;
import java.util.Date;

public class groups
{
  private Date created;
  private Date updated;
  private Integer id;
  private Integer FacultyID;
  private String objectId;
  private String Name;
  private String ownerId;
  public Date getCreated()
  {
    return created;
  }

  public Date getUpdated()
  {
    return updated;
  }

  public Integer getId()
  {
    return id;
  }

  public void setId( Integer id )
  {
    this.id = id;
  }

  public Integer getFacultyID()
  {
    return FacultyID;
  }

  public void setFacultyID( Integer FacultyID )
  {
    this.FacultyID = FacultyID;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public String getName()
  {
    return Name;
  }

  public void setName( String Name )
  {
    this.Name = Name;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

                                                    
  public groups save()
  {
    return Backendless.Data.of( groups.class ).save( this );
  }

  public void saveAsync( AsyncCallback<groups> callback )
  {
    Backendless.Data.of( groups.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( groups.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( groups.class ).remove( this, callback );
  }

  public static groups findById( String id )
  {
    return Backendless.Data.of( groups.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<groups> callback )
  {
    Backendless.Data.of( groups.class ).findById( id, callback );
  }

  public static groups findFirst()
  {
    return Backendless.Data.of( groups.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<groups> callback )
  {
    Backendless.Data.of( groups.class ).findFirst( callback );
  }

  public static groups findLast()
  {
    return Backendless.Data.of( groups.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<groups> callback )
  {
    Backendless.Data.of( groups.class ).findLast( callback );
  }

  public static List<groups> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( groups.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<groups>> callback )
  {
    Backendless.Data.of( groups.class ).find( queryBuilder, callback );
  }
}