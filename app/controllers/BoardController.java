package controllers;

import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by chewb on 4/6/2016.
 */
public class BoardController extends Controller {

    @javax.inject.Inject
    FormFactory formFactory;

    @Security.Authenticated(ActionAuthenticator.class)
    public Result createBoard(){
        return ok();
    }

}
