package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import play.data.validation.Constraints;

import javax.persistence.*;
import javax.validation.Constraint;
import java.util.List;

/**
 * Created by chewb on 4/2/2016.
 */
@Entity
public class Board extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Constraints.Required
    @Constraints.MinLength(2)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference("user-board")
    private User user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "board", cascade = CascadeType.ALL)
    @JsonManagedReference("board-post")
    private List<Post> posts;

    @Constraints.Required
    @Constraints.MinLength(2)
    private String content;

    private long createdDate;

    public static Board findById(long id){
        return Ebean.find(Board.class, id);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public List<Post> getPosts() {
        return posts;
    }
}
