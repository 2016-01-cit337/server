package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * Created by chewb on 4/2/2016.
 */
@Entity
public class Post extends Model {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;

   @ManyToOne(fetch = FetchType.EAGER)
   @JsonBackReference
   private Board board;

   @ManyToOne(fetch = FetchType.EAGER)
   @JsonBackReference
   private User owner;

   @Constraints.MinLength(10)
   private String content;
}
