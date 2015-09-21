package models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

import javax.persistence.*;

/**
 *  PlayerState Class
 *
 *  @version 1.0
 *  @author fabiomazzone
 */
@Embeddable
public class Settings {
    public boolean sound;
    public boolean music;

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////
    /**
     * Default constructor
     */
    public Settings() {
        this.sound = true ;
        this.music = true ;
    }

//////////////////////////////////////////////////
//  JSON
//////////////////////////////////////////////////

    /**
     *
     * @return returns Settings as JSON
     */
    public ObjectNode getSettings() {
        ObjectNode node = Json.newObject();

        node.put("music", this.music);
        node.put("sound", this.sound);

        return node;
    }

    /**
     *
     * @param json  Settings in JSON format
     * @return  returns true if successful
     */
    public void setSettings(boolean music, boolean sound) {
        this.music = music;
        this.sound = sound;
    }
}
