/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

/**
 *
 * @author alienware
 */
public class GroupList {

    private int GroupID;
    private String GroupName;
    private String GroupDescription;
    private int TecherGroup;
    private boolean GroupActive;

    public void setGroupID(int GroupID) {
        this.GroupID = GroupID;
    }

    public int getGroupID() {
        return this.GroupID;
    }

    public void setGroupName(String Name) {
        this.GroupName = Name;
    }

    public String getGroupName() {
        return this.GroupName;
    }

    public void setGroupTecher(int TecherID) {
        this.TecherGroup = TecherID;
    }

    public int getGroupTecher() {
        return this.TecherGroup;
    }

    public void setGroupDescription(String GroupDescription) {
        this.GroupDescription = GroupDescription;
    }

    public String getGroupDescription() {
        return this.GroupDescription;
    }
    
    public void setGroupActive(boolean Active){
        this.GroupActive = Active;
    }
    
    public boolean getGroupActive(){
        return this.GroupActive;
    }
}
