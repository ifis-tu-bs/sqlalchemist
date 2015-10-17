package models;

import javax.persistence.*;

/**
 *  Settings Class
 *
 *  @version 1.0
 *  @author fabiomazzone
 */
@Embeddable
public class Settings {
    private final boolean sound;
    private final boolean music;

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
