package de.tu_bs.cs.ifis.sqlgame.xmlparse;

import java.util.ArrayList;

/**
 * class for the subtasks in the exercise
 *
 * @author Tobias Gruenhagen, Philip Holzhueter, Tobias Runge
 */
public class Exercise {

    /**
     * Id of the subtask.
     */
    private int subtaskid;
    
    /**
     * ArrayList with the task texts.
     */
    private ArrayList<String> tasktexts;
    
    /**
     * Reference statement of this exercise.
     */
    private String referencestatement;
    
    /**
     * Evaluation strategy of this exercise.
     */
    private String evaluationstrategy;
    
    /**
     * ArrayList with the terms that the exercise contains.
     */
    private ArrayList<String> term;
    
    /**
     * Number of the points of this exercise.
     */
    private int points;
    
    /**
     * Language of this exercise.
     */
    private String language;

    /**
     * Constructor Exercise.
     * 
     * Set up defaults.
     */
    public Exercise() {
        this.tasktexts = new ArrayList<>();
        this.term = new ArrayList<>();
    }

    /**
     * get-method for the subtaskid. The id of this subtask.
     * 
     * @return substaskid
     */
    public int getSubTaskId() {
        return this.subtaskid;
    }
    
    /**
     * set-method for the subtaskid. The id of this subtask.
     *
     * @param subtaskid int, id of the subtask
     */
    public void setSubTaskId(int subtaskid) {
        this.subtaskid = subtaskid;
    }
    
    /**
     * get-method for the tasktext. The defintion of the problem, 
     * that has to be solved
     *
     * @return tasktext
     */
    public ArrayList<String> getTasktexts() {
        return tasktexts;
    }

    /**
     * set-method for the tasktext. The defintion of the problem, 
     * that has to be solved
     *
     * @param tasktexts String, the text of the task
     */
    public void setTasktexts(String tasktexts) {
        this.tasktexts.add(tasktexts);
    }

    /**
     * get-method for the referencestatement (the solution of the task)
     *
     * @return referencestatement
     */
    public String getReferencestatement() {
        return referencestatement;
    }

    /**
     * set-method for the referencestatement (the solution of the task)
     *
     * @param referencestatement String, statement for the solution
     */
    public void setReferencestatement(String referencestatement) {
        this.referencestatement = referencestatement;
    }

    /**
     * get-method for the evaluationstrategy (Set or List)
     *
     * @return evaluationstrategy
     */
    public String getEvaluationstrategy() {
        return evaluationstrategy;
    }

    /**
     * set-method for the evaluationstrategy (Set or List)
     *
     * @param evaluationstrategy String with Set or List
     */
    public void setEvaluationstrategy(String evaluationstrategy) {
        this.evaluationstrategy = evaluationstrategy;
    }

    /**
     * get-method for the requied terms, which the user need for this task
     *
     * @return all terms as a string
     */
    public String getTerm() {
        String s = "";
        for (String term : this.term) {
            s += term + "\n";
        }
        return s;
    }

    /**
     * set-method for the requied terms, which the user need for this task
     *
     * @param term String, the term that should be added to the array
     */
    public void setTerm(String term) {
        this.term.add(term);
    }

    /**
     * get-method for the points of the task
     *
     * @return points
     */
    public int getPoints() {
        return points;
    }

    /**
     * set-method for the points of the task
     *
     * @param points int, the points of the task
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * get-method for the langugage of the task
     *
     * @return language of the task
     */
    public String getLanguage() {
        return language;
    }

    /**
     * set-method for the language of the task
     *
     * @param language String, language of the tast
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Method toString.
     * 
     * Print out the exercise.
     *
     * @return String string with all the content
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Task ").append(this.subtaskid).append("\n");
        
        for (String taskText : this.tasktexts) {
            sb.append("Tasktext:").append(taskText).append("\n");
        }
        
        sb.append("Referencestatement:").append(this.getReferencestatement()).append("\n");
        sb.append("Evaluationstrategy:").append(this.getEvaluationstrategy()).append("\n");
        sb.append("Requiredterms:").append(this.getTerm()).append("\n");
        sb.append("Points:").append(this.getPoints()).append("\n");

        return sb.toString();
    }
}
