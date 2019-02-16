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
public class StudentsList {

    private int UserID;
    private String Name;
    private String CardID;
    private String Birthdate;
    private String ParentName;
    private String ParentPhone;
    private String PhoneNumber;
    private int Category;
    private boolean isBlock;
    private String Start;
    private String End;
    private String LastLogin;
    private String Created;
    private String GroupName;
    private String Notes;
    private int Amount;

    public void setUserID(int ID) {
        this.UserID = ID;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setCardID(String ID) {
        this.CardID = ID;
    }

    public void setPhoneNumber(String Phone) {
        this.PhoneNumber = Phone;
    }

    public void setCategory(int Category) {
        this.Category = Category;
    }

    public void setisBlock(boolean Blocked) {
        this.isBlock = Blocked;
    }

    public void setStartDate(String Start) {
        this.Start = Start;
    }

    public void setEndDate(String End) {
        this.End = End;
    }

    public void setLastLogin(String LastLogin) {
        this.LastLogin = LastLogin;
    }

    public void setCreatedDate(String Created) {
        this.Created = Created;
    }

    public void setGroup(String GroupName) {
        if (this.GroupName != null) {
            if (this.GroupName.isEmpty()) {
                this.GroupName = GroupName;
            } else {
                this.GroupName += "," + GroupName;
            }
        } else {
            this.GroupName = GroupName;
        }
    }

    public void setAmount(int Amount) {
        this.Amount = Amount;
    }

    public void setBirthdate(String Birthdate) {
        this.Birthdate = Birthdate;
    }

    public void setParentName(String ParentName) {
        this.ParentName = ParentName;
    }

    public void setParentPhone(String ParentPhone) {
        this.ParentPhone = ParentPhone;
    }

    public void setNote(String Note) {
        this.Notes = Note;
    }

    public int getUserID() {
        return this.UserID;
    }

    public String getName() {
        return this.Name;
    }

    public String getCardID() {
        return this.CardID;
    }

    public String getPhoneNumber() {
        return this.PhoneNumber;
    }

    public int getCategory() {
        return this.Category;
    }

    public boolean getisBlocked() {
        return this.isBlock;
    }

    public String getStartDate() {
        return this.Start;
    }

    public String getEndDate() {
        return this.End;
    }

    public String getLastLogin() {
        return this.LastLogin;
    }

    public String getCreatedDate() {
        return this.Created;
    }

    public String getGroup() {
        return this.GroupName;
    }

    public int getAmount() {
        return this.Amount;
    }

    public String getBirthdate() {
        return this.Birthdate;
    }

    public String getParentName() {
        return this.ParentName;
    }

    public String getParentPhone() {
        return this.ParentPhone;
    }

    public String getNote() {
        return this.Notes;
    }
}
