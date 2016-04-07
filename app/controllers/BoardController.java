package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.Board;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import scala.util.parsing.json.JSONObject;

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

}
