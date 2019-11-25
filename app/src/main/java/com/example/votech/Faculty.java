
package com.example.votech;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;
import com.backendless.geo.GeoPoint;

import java.util.List;
import java.util.Date;

public class Faculty
{
  private String ownerId;
  private Date updated;
  private String Name;
  private Date created;
  private Integer id;
  private String objectId;
  public String getOwnerId()
  {
    return ownerId;
  }

  public Date getUpdated()
  {
    return updated;
  }

  public String getName()
  {
    return Name;
  }

  public void setName( String Name )
  {
    this.Name = Name;
  }

  public Date getCreated()
  {
    return created;
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

                                                    
  public Faculty save()
  {
    return Backendless.Data.of( Faculty.class ).save( this );
  }

  public void saveAsync( AsyncCallback<Faculty> callback )
  {
    Backendless.Data.of( Faculty.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Faculty.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Faculty.class ).remove( this, callback );
  }

  public static Faculty findById( String id )
  {
    return Backendless.Data.of( Faculty.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<Faculty> callback )
  {
    Backendless.Data.of( Faculty.class ).findById( id, callback );
  }

  public static Faculty findFirst()
  {
    return Backendless.Data.of( Faculty.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<Faculty> callback )
  {
    Backendless.Data.of( Faculty.class ).findFirst( callback );
  }

  public static Faculty findLast()
  {
    return Backendless.Data.of( Faculty.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<Faculty> callback )
  {
    Backendless.Data.of( Faculty.class ).findLast( callback );
  }

  public static List<Faculty> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( Faculty.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<Faculty>> callback )
  {
    Backendless.Data.of( Faculty.class ).find( queryBuilder, callback );
  }
}