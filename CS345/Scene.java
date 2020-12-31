import javax.swing.*;
import java.util.ArrayList;


/*
 * Scene
 *
 * Contains information that might be pertinent to a scene in a game.
 */
public class Scene {
    private final ArrayList<Role> roles;
    private int budget;
    private String caption;
    private String title;
    private String filename;
    private JLabel card;

    public Scene() {
        this.title = "";
        this.caption = "";
        this.filename = "";
        this.budget = 0;
        this.roles = new ArrayList<>();
        this.card = null;
    }

    public ArrayList<Role> getRoles() {
        return this.roles;
    }

    public JLabel getCardLabel(){
        return this.card;
    }

    public int getBudget() {
        return this.budget;
    }

    public String getCaption() {
        return this.caption;
    }

    public String getTitle() {
        return this.title;
    }

    public String getFilename(){
        return this.filename;
    }

    public void setCardLabel(JLabel c){
        this.card = c;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRole(Role role) {
        this.roles.add(role);
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public void setFileName(String filename){
        this.filename = filename;
    }

    public String printScene(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Title: %s\r\n", this.title)).append(String.format("Caption: %s\r\n", this.caption)).append(String.format("Budget: %d Million\r\n\r\n", this.budget));
        sb.append("Roles: \r\n");
        for(int i = 0; i < roles.size(); i++){
            sb.append(String.format("%d. %s, Rank: %d\r\n",i+1, roles.get(i).getName(), roles.get(i).getRank()));
        }
        return sb.toString();
    }

}