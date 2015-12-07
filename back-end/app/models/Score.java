package models;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * @author fabiomazzone
 */
@Embeddable
public class Score {
    private int                         totalScore;
    private int                         playedTime;
    private int                         playedRuns;
    private int                         totalCoins;
    private int                         doneSQL;
    private int                         solvedSQL;
    private int                         successRate;

    @Transient
    private int                         ownRank;


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  Getter & Setter
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getPlayedTime() {
        return playedTime;
    }

    public void setPlayedTime(int playedTime) {
        this.playedTime = playedTime;
    }

    public int getPlayedRuns() {
        return playedRuns;
    }

    public void setPlayedRuns(int playedRuns) {
        this.playedRuns = playedRuns;
    }

    public int getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(int totalCoins) {
        this.totalCoins = totalCoins;
    }

    public int getDoneSQL() {
        return doneSQL;
    }

    public void setDoneSQL(int doneSQL) {
        this.doneSQL = doneSQL;
    }

    public int getSolvedSQL() {
        return solvedSQL;
    }

    public void setSolvedSQL(int solvedSQL) {
        this.solvedSQL = solvedSQL;
    }

    public int getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(int successRate) {
        this.successRate = successRate;
    }

    public int getOwnRank() {
        return ownRank;
    }

    public void setOwnRank(int ownRank) {
        this.ownRank = ownRank;
    }
}
