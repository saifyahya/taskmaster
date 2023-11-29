package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.core.model.annotations.BelongsTo;


import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Task type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Tasks", authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "byTeam", fields = {"teamId","title"})
public final class Task implements Model {
  public static final QueryField ID = field("Task", "id");
  public static final QueryField TITLE = field("Task", "title");
  public static final QueryField BODY = field("Task", "body");
  public static final QueryField END_DATE = field("Task", "endDate");
  public static final QueryField STATE = field("Task", "state");
  public static final QueryField IMAGE = field("Task", "image");
  public static final QueryField LOCATION_LONGITUDE = field("Task", "locationLongitude");
  public static final QueryField LOCATION_LATITUDE = field("Task", "locationLatitude");
  public static final QueryField TEAM_PERSON = field("Task", "teamId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String title;
  private final @ModelField(targetType="String") String body;
  private final @ModelField(targetType="AWSDateTime") Temporal.DateTime endDate;
  private final @ModelField(targetType="TaskStateEnum") TaskStateEnum state;
  private final @ModelField(targetType="String") String image;
  private final @ModelField(targetType="String") String locationLongitude;
  private final @ModelField(targetType="String") String locationLatitude;
  private final @ModelField(targetType="Team") @BelongsTo(targetName = "teamId", type = Team.class) Team teamPerson;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  /** @deprecated This API is internal to Amplify and should not be used. */
  @Deprecated
   public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public String getTitle() {
      return title;
  }
  
  public String getBody() {
      return body;
  }
  
  public Temporal.DateTime getEndDate() {
      return endDate;
  }
  
  public TaskStateEnum getState() {
      return state;
  }
  
  public String getImage() {
      return image;
  }
  
  public String getLocationLongitude() {
      return locationLongitude;
  }
  
  public String getLocationLatitude() {
      return locationLatitude;
  }
  
  public Team getTeamPerson() {
      return teamPerson;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Task(String id, String title, String body, Temporal.DateTime endDate, TaskStateEnum state, String image, String locationLongitude, String locationLatitude, Team teamPerson) {
    this.id = id;
    this.title = title;
    this.body = body;
    this.endDate = endDate;
    this.state = state;
    this.image = image;
    this.locationLongitude = locationLongitude;
    this.locationLatitude = locationLatitude;
    this.teamPerson = teamPerson;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Task task = (Task) obj;
      return ObjectsCompat.equals(getId(), task.getId()) &&
              ObjectsCompat.equals(getTitle(), task.getTitle()) &&
              ObjectsCompat.equals(getBody(), task.getBody()) &&
              ObjectsCompat.equals(getEndDate(), task.getEndDate()) &&
              ObjectsCompat.equals(getState(), task.getState()) &&
              ObjectsCompat.equals(getImage(), task.getImage()) &&
              ObjectsCompat.equals(getLocationLongitude(), task.getLocationLongitude()) &&
              ObjectsCompat.equals(getLocationLatitude(), task.getLocationLatitude()) &&
              ObjectsCompat.equals(getTeamPerson(), task.getTeamPerson()) &&
              ObjectsCompat.equals(getCreatedAt(), task.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), task.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getTitle())
      .append(getBody())
      .append(getEndDate())
      .append(getState())
      .append(getImage())
      .append(getLocationLongitude())
      .append(getLocationLatitude())
      .append(getTeamPerson())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Task {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("title=" + String.valueOf(getTitle()) + ", ")
      .append("body=" + String.valueOf(getBody()) + ", ")
      .append("endDate=" + String.valueOf(getEndDate()) + ", ")
      .append("state=" + String.valueOf(getState()) + ", ")
      .append("image=" + String.valueOf(getImage()) + ", ")
      .append("locationLongitude=" + String.valueOf(getLocationLongitude()) + ", ")
      .append("locationLatitude=" + String.valueOf(getLocationLatitude()) + ", ")
      .append("teamPerson=" + String.valueOf(getTeamPerson()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static TitleStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Task justId(String id) {
    return new Task(
      id,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      title,
      body,
      endDate,
      state,
      image,
      locationLongitude,
      locationLatitude,
      teamPerson);
  }
  public interface TitleStep {
    BuildStep title(String title);
  }
  

  public interface BuildStep {
    Task build();
    BuildStep id(String id);
    BuildStep body(String body);
    BuildStep endDate(Temporal.DateTime endDate);
    BuildStep state(TaskStateEnum state);
    BuildStep image(String image);
    BuildStep locationLongitude(String locationLongitude);
    BuildStep locationLatitude(String locationLatitude);
    BuildStep teamPerson(Team teamPerson);
  }
  

  public static class Builder implements TitleStep, BuildStep {
    private String id;
    private String title;
    private String body;
    private Temporal.DateTime endDate;
    private TaskStateEnum state;
    private String image;
    private String locationLongitude;
    private String locationLatitude;
    private Team teamPerson;
    public Builder() {
      
    }
    
    private Builder(String id, String title, String body, Temporal.DateTime endDate, TaskStateEnum state, String image, String locationLongitude, String locationLatitude, Team teamPerson) {
      this.id = id;
      this.title = title;
      this.body = body;
      this.endDate = endDate;
      this.state = state;
      this.image = image;
      this.locationLongitude = locationLongitude;
      this.locationLatitude = locationLatitude;
      this.teamPerson = teamPerson;
    }
    
    @Override
     public Task build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Task(
          id,
          title,
          body,
          endDate,
          state,
          image,
          locationLongitude,
          locationLatitude,
          teamPerson);
    }
    
    @Override
     public BuildStep title(String title) {
        Objects.requireNonNull(title);
        this.title = title;
        return this;
    }
    
    @Override
     public BuildStep body(String body) {
        this.body = body;
        return this;
    }
    
    @Override
     public BuildStep endDate(Temporal.DateTime endDate) {
        this.endDate = endDate;
        return this;
    }
    
    @Override
     public BuildStep state(TaskStateEnum state) {
        this.state = state;
        return this;
    }
    
    @Override
     public BuildStep image(String image) {
        this.image = image;
        return this;
    }
    
    @Override
     public BuildStep locationLongitude(String locationLongitude) {
        this.locationLongitude = locationLongitude;
        return this;
    }
    
    @Override
     public BuildStep locationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
        return this;
    }
    
    @Override
     public BuildStep teamPerson(Team teamPerson) {
        this.teamPerson = teamPerson;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String title, String body, Temporal.DateTime endDate, TaskStateEnum state, String image, String locationLongitude, String locationLatitude, Team teamPerson) {
      super(id, title, body, endDate, state, image, locationLongitude, locationLatitude, teamPerson);
      Objects.requireNonNull(title);
    }
    
    @Override
     public CopyOfBuilder title(String title) {
      return (CopyOfBuilder) super.title(title);
    }
    
    @Override
     public CopyOfBuilder body(String body) {
      return (CopyOfBuilder) super.body(body);
    }
    
    @Override
     public CopyOfBuilder endDate(Temporal.DateTime endDate) {
      return (CopyOfBuilder) super.endDate(endDate);
    }
    
    @Override
     public CopyOfBuilder state(TaskStateEnum state) {
      return (CopyOfBuilder) super.state(state);
    }
    
    @Override
     public CopyOfBuilder image(String image) {
      return (CopyOfBuilder) super.image(image);
    }
    
    @Override
     public CopyOfBuilder locationLongitude(String locationLongitude) {
      return (CopyOfBuilder) super.locationLongitude(locationLongitude);
    }
    
    @Override
     public CopyOfBuilder locationLatitude(String locationLatitude) {
      return (CopyOfBuilder) super.locationLatitude(locationLatitude);
    }
    
    @Override
     public CopyOfBuilder teamPerson(Team teamPerson) {
      return (CopyOfBuilder) super.teamPerson(teamPerson);
    }
  }
  


  
}
