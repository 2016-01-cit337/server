package models;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Constraints.MinLength(10)
    private String content;

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

    public void setId(long id) {
        this.id = id;
    }
}
