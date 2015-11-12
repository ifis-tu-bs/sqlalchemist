package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import java.io.File;


/**
 * Created by Invisible on 12.11.2015.
 */
public class DownloadController extends Controller {

    public Result download(String filename){
        File downloadFile = new File("download/" + filename);

        if (downloadFile.exists()) {
            return ok(downloadFile);
        }

        return badRequest("File does not exist");
    }

}
