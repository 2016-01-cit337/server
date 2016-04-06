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

    @Constraints.MinLength(2)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private User owner;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "board", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Post> replies;

    @Constraints.MinLength(10)
    private String content;
}
