package controllers;

import models.User;
import play.mvc.Controller;

/**
 * Created by chewb on 4/7/2016.
 */
public class ControllerBase extends Controller {

    public User getAuthenicatedUser(){
        return ActionAuthenticator.getUser(ctx());
    }

}
