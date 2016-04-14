package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.FetchConfig;
import com.fasterxml.jackson.databind.JsonNode;
import models.Board;
import models.Post;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import scala.util.parsing.json.JSONObject;

import java.time.Instant;
import java.util.List;

/**
 * Created by chewb on 4/6/2016.
 */
public class BoardController extends ControllerBase {

    @javax.inject.Inject
    FormFactory formFactory;

    @Security.Authenticated(ActionAuthenticator.class)
    public Result createBoard(){

        Board board = play.libs.Json.fromJson(request().body().asJson(), Board.class);

        if(board == null){
            return badRequest();
        }

        Form<Board> form = formFactory.form(Board.class).bindFromRequest();
        JsonNode node = validateCreateBoard(form);

        if(node != null){
            return badRequest(play.libs.Json.toJson(node));
        }

        User user = getAuthenicatedUser();
        board.setUser(user);
        board.setCreatedDate(Instant.now().getEpochSecond());

        Ebean.save(board);

        class RetBoard {
            public long id;
        }

        RetBoard ret = new RetBoard();
        ret.id = board.getId();

        return created(play.libs.Json.toJson(ret));
    }

    private JsonNode validateCreateBoard(Form<Board> form){
        if(form.hasErrors()){
            JsonNode node = form.errorsAsJson();

            Board error = new Board();
            if(form.error("title") != null){
                error.setTitle(node.get("title").get(0).asText());
            }
            if(form.error("content") != null){
                error.setContent(node.get("content").get(0).asText());
            }
            return play.libs.Json.toJson(error);
        }

        return null;
    }

    //@Security.Authenticated(ActionAuthenticator.class)
    public Result getAllBoards(){
        List<Board> boards = Ebean.find(Board.class)
                .select("id, title")
                .fetch("user", "id, firstName, lastName")
                .findList();

        return ok(Ebean.json().toJson(boards));
    }

    public Result getBoard(long id){
        Board board = Ebean.find(Board.class)
                .where().idEq(id)
                .select("id, title, content, createdDate")
                .fetch("posts")
                .fetch("posts.user", "id, firstName, lastName")
                .fetch("user", "id, firstName, lastName")
                .findUnique();

        if(board == null){
            return badRequest();
        }

        return ok(Ebean.json().toJson(board));
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result createPost(long boardId){
        Post post = play.libs.Json.fromJson(request().body().asJson(), Post.class);

        if(post == null){
            return badRequest();
        }

        Form<Post> form = formFactory.form(Post.class).bindFromRequest();
        JsonNode node = validateCreatePost(form);

        if(node != null){
            return badRequest(play.libs.Json.toJson(node));
        }

        Board board = Board.findById(boardId);

        if(board == null){
            return badRequest();
        }

        User user = getAuthenicatedUser();
        post.setUser(user);
        post.setBoard(board);
        post.setCreatedDate(Instant.now().getEpochSecond());

        Ebean.save(post);

        return created(Ebean.json().toJson(post));
    }

    private JsonNode validateCreatePost(Form<Post> form){
        if(form.hasErrors()){
            JsonNode node = form.errorsAsJson();

            Post error = new Post();
            if(form.error("content") != null){
                error.setContent(node.get("content").get(0).asText());
            }
            return play.libs.Json.toJson(error);
        }

        return null;
    }
}
