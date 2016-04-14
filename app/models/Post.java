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
   @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private long id;

   @ManyToOne(fetch = FetchType.EAGER)
   @JsonBackReference("board-post")
   private Board board;

   @ManyToOne(fetch = FetchType.EAGER)
   @JsonBackReference("user-post")
   private User user;

   @Constraints.Required
   @Constraints.MinLength(10)
   private String content;

   private long createdDate;



   public long getId() {
      return id;
   }

   public Board getBoard() {
      return board;
   }

   public void setBoard(Board board) {
      this.board = board;
   }

   public User getUser() {
      return user;
   }

   public void setUser(User user) {
      this.user = user;
   }

   public String getContent() {
      return content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public long getCreatedDate() {
      return createdDate;
   }

   public void setCreatedDate(long createdDate) {
      this.createdDate = createdDate;
   }
}
