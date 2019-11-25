
package com.example.votech;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;
import com.backendless.geo.GeoPoint;

import java.util.List;
import java.util.Date;

public class userType
{
  private String ownerId;
  private Date created;
  private Date updated;
  private Integer id;
  private String objectId;
  private String Type;
  public String getOwnerId()
  {
    return ownerId;
  }

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

  public String getObjectId()
  {
    return objectId;
  }

  public String getType()
  {
    return Type;
  }

  public void setType( String Type )
  {
    this.Type = Type;
  }

                                                    
  public userType save()
  {
    return Backendless.Data.of( userType.class ).save( this );
  }

  public void saveAsync( AsyncCallback<userType> callback )
  {
    Backendless.Data.of( userType.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( userType.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( userType.class ).remove( this, callback );
  }

  public static userType findById( String id )
  {
    return Backendless.Data.of( userType.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<userType> callback )
  {
    Backendless.Data.of( userType.class ).findById( id, callback );
  }

  public static userType findFirst()
  {
    return Backendless.Data.of( userType.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<userType> callback )
  {
    Backendless.Data.of( userType.class ).findFirst( callback );
  }

  public static userType findLast()
  {
    return Backendless.Data.of( userType.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<userType> callback )
  {
    Backendless.Data.of( userType.class ).findLast( callback );
  }

  public static List<userType> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( userType.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<userType>> callback )
  {
    Backendless.Data.of( userType.class ).find( queryBuilder, callback );
  }
}