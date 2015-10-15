package models;

import play.api.libs.ws.ssl.Algorithms;

import javax.persistence.*;

/**
 *  Settings Class
 *
 *  @version 1.0
 *  @author fabiomazzone
 */
@Embeddable
public class Settings {
    private boolean sound;
    private boolean music;

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////
    /**
     * Default constructor
     */
    public Settings(boolean sound, boolean music) {
        this.sound = sound;
        this.music = music;
    }

    public boolean isSound() {
        return sound;
    }

    public boolean isMusic() {
        return music;
    }
}
