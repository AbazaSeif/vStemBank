/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author alienware
 */
public class DatabaseImplement {

    public List<StudentsList> QStudents() {
        MySqlConnections DB = new MySqlConnections();
        Map<String, String> stuHash = new HashMap<>();
        stuHash.put("isStudent", "1");
        List<List> Results = DB.get("Users", stuHash, java.util.Arrays.asList("id", "name", "isBlock"));
        try {
            List<StudentsList> ResultStudent = new ArrayList<>();
            Results.stream().map((Result) -> {
                StudentsList SL = new StudentsList();
                SL.setName(String.valueOf(Result.get(1)));
                SL.setisBlock((Result.get(2) == "1"));
                return SL;
            }).map((SL) -> {
                SL.setCategory(1);
                return SL;
            }).forEachOrdered((SL) -> {
                ResultStudent.add(SL);
            });
            if (ResultStudent.size() > 0) {
                return ResultStudent;
            } else {
                return null;
            }
        } catch (java.lang.NullPointerException e) {
            return null;
        }
    }

    public List<StudentsList> GetUsersInGroup(String GroupName) {
        int GroupID = this.getGroupID(GroupName);
        MySqlConnections DB = new MySqlConnections();
        List<StudentsList> ResultStudent = new ArrayList<>();

        Map<String, String> stuHash = new HashMap<>();
        stuHash.put("groupid", String.valueOf(GroupID));
        List<List> Results = DB.get("usergroup", stuHash, java.util.Arrays.asList("userid"));
        if (Results != null) {
            for (List itGroup : Results.subList(0, Results.size())) {
                ResultStudent.add(this.GetStudentByID(Integer.parseInt(itGroup.get(0).toString())));
            }
        }

        if (ResultStudent.size() > 0) {
            return ResultStudent;
        } else {
            return null;
        }
    }

    public List<StudentsList> QTeachers() {
        MySqlConnections DB = new MySqlConnections();
        try {
            Map<String, String> stuHash = new HashMap<>();
            stuHash.put("isTecher", "1");
            List<List> Results = DB.get("Users", stuHash, java.util.Arrays.asList("id", "name", "isBlock"));
            List<StudentsList> ResultStudent = new ArrayList<>();
            Results.stream().map((Result) -> {
                StudentsList SL = new StudentsList();
                SL.setUserID(Integer.parseInt(String.valueOf(Result.get(0))));
                SL.setName(String.valueOf(Result.get(1)));
                SL.setisBlock((Result.get(2) == "1"));
                return SL;
            }).map((SL) -> {
                SL.setCategory(2);
                return SL;
            }).forEachOrdered((SL) -> {
                ResultStudent.add(SL);
            });
            if (ResultStudent.size() > 0) {
                return ResultStudent;
            } else {
                return null;
            }
        } catch (java.lang.NullPointerException e) {
            return null;
        }
    }

    public StudentsList GetStudent(String Name) {
        MySqlConnections DB = new MySqlConnections();
        Map<String, String> stuHash = new HashMap<>();
        stuHash.put("isStudent", "1");
        stuHash.put("name", Name);
        List<List> Results = DB.get("Users", stuHash, java.util.Arrays.asList("id", "name", "isBlock", "lastlogin", "created", "phonenumber", "cardid", "birthdate", "parentphone", "parentname", "notes1"));
        for (List Result : Results) {
            stuHash.clear();
            stuHash.put("userid", String.valueOf(Result.get(0)));
            StudentsList SL = new StudentsList();
            List<List> Amount = DB.get("balance", stuHash, java.util.Arrays.asList("amount"));
            List<List> Groups = DB.get("usergroup", stuHash, java.util.Arrays.asList("groupid"));

            SL.setUserID(Integer.parseInt(String.valueOf(Result.get(0))));
            SL.setName(String.valueOf(Result.get(1)));
            SL.setisBlock((Result.get(2).toString().equals("1")));
            SL.setLastLogin(String.valueOf(Result.get(3)));
            SL.setCreatedDate(String.valueOf(Result.get(4)));
            SL.setPhoneNumber(String.valueOf(Result.get(5)));
            SL.setCardID(String.valueOf(Result.get(6)));
            SL.setBirthdate(String.valueOf(Result.get(7)));
            SL.setParentPhone(String.valueOf(Result.get(8)));
            SL.setParentName(String.valueOf(Result.get(9)));
            SL.setNote(String.valueOf(Result.get(10)));
            SL.setCategory(1);
            SL.setAmount(Integer.parseInt((Amount == null ? "0" : (String) Amount.get(0).get(0))));
            if (Groups != null) {
                Groups.stream().map((UserGroup) -> {
                    stuHash.clear();
                    stuHash.put("id", String.valueOf(UserGroup.get(0)));
                    return UserGroup;
                }).map((_item) -> DB.get("groups", stuHash, java.util.Arrays.asList("groupname"))).forEachOrdered((GroupName) -> {
                    SL.setGroup(String.valueOf(GroupName.get(0).get(0)));
                });
            } else {
                SL.setGroup("");
            }
            return SL;
        }
        return null;
    }

    public StudentsList GetStudentByCardID(String CardID) {
        MySqlConnections DB = new MySqlConnections();
        Map<String, String> stuHash = new HashMap<>();
        stuHash.put("isStudent", "1");
        stuHash.put("cardid", CardID);
        List<List> Results = DB.get("Users", stuHash, java.util.Arrays.asList("id", "name", "isBlock", "lastlogin", "created", "phonenumber", "cardid", "birthdate", "parentphone", "parentname", "notes1"));
        for (List Result : Results) {
            stuHash.clear();
            stuHash.put("userid", String.valueOf(Result.get(0)));
            StudentsList SL = new StudentsList();
            List<List> Amount = DB.get("balance", stuHash, java.util.Arrays.asList("amount"));
            List<List> Groups = DB.get("usergroup", stuHash, java.util.Arrays.asList("groupid"));

            SL.setUserID(Integer.parseInt(String.valueOf(Result.get(0))));
            SL.setName(String.valueOf(Result.get(1)));
            SL.setisBlock((Result.get(2).toString().equals("1")));
            SL.setLastLogin(String.valueOf(Result.get(3)));
            SL.setCreatedDate(String.valueOf(Result.get(4)));
            SL.setPhoneNumber(String.valueOf(Result.get(5)));
            SL.setCardID(String.valueOf(Result.get(6)));
            SL.setBirthdate(String.valueOf(Result.get(7)));
            SL.setParentPhone(String.valueOf(Result.get(8)));
            SL.setParentName(String.valueOf(Result.get(9)));
            SL.setNote(String.valueOf(Result.get(10)));
            SL.setCategory(1);
            SL.setAmount(Integer.parseInt((Amount == null ? "0" : (String) Amount.get(0).get(0))));
            if (Groups != null) {
                Groups.stream().map((UserGroup) -> {
                    stuHash.clear();
                    stuHash.put("id", String.valueOf(UserGroup.get(0)));
                    return UserGroup;
                }).map((_item) -> DB.get("groups", stuHash, java.util.Arrays.asList("groupname"))).forEachOrdered((GroupName) -> {
                    SL.setGroup(String.valueOf(GroupName.get(0).get(0)));
                });
            } else {
                SL.setGroup("");
            }
            return SL;
        }
        return null;
    }

    public StudentsList GetStudentByID(int ID) {
        MySqlConnections DB = new MySqlConnections();
        Map<String, String> stuHash = new HashMap<>();
        stuHash.put("isStudent", "1");
        stuHash.put("id", String.valueOf(ID));
        List<List> Results = DB.get("Users", stuHash, java.util.Arrays.asList("id", "name", "isBlock", "astart", "aend", "lastlogin", "created", "phonenumber", "cardid", "birthdate", "parentphone", "parentname", "notes1"));
        for (List Result : Results) {
            stuHash.clear();
            stuHash.put("userid", String.valueOf(Result.get(0)));
            StudentsList SL = new StudentsList();
            List<List> Amount = DB.get("balance", stuHash, java.util.Arrays.asList("amount"));
            List<List> Groups = DB.get("usergroup", stuHash, java.util.Arrays.asList("groupid"));

            SL.setUserID(Integer.parseInt(String.valueOf(Result.get(0))));
            SL.setName(String.valueOf(Result.get(1)));
            SL.setisBlock((Result.get(2).toString().equals("1")));
            SL.setStartDate(String.valueOf(Result.get(3)));
            SL.setEndDate(String.valueOf(Result.get(4)));
            SL.setLastLogin(String.valueOf(Result.get(5)));
            SL.setCreatedDate(String.valueOf(Result.get(6)));
            SL.setPhoneNumber(String.valueOf(Result.get(7)));
            SL.setCardID(String.valueOf(Result.get(8)));
            SL.setBirthdate(String.valueOf(Result.get(9)));
            SL.setParentPhone(String.valueOf(Result.get(10)));
            SL.setParentName(String.valueOf(Result.get(11)));
            SL.setNote(String.valueOf(Result.get(12)));
            SL.setCategory(1);
            SL.setAmount(Integer.parseInt((Amount == null ? "0" : (String) Amount.get(0).get(0))));
            if (Groups != null) {
                Groups.stream().map((UserGroup) -> {
                    stuHash.clear();
                    stuHash.put("id", String.valueOf(UserGroup.get(0)));
                    return UserGroup;
                }).map((_item) -> DB.get("groups", stuHash, java.util.Arrays.asList("groupname"))).forEachOrdered((GroupName) -> {
                    SL.setGroup(String.valueOf(GroupName.get(0).get(0)));
                });
            } else {
                SL.setGroup("");
            }
            return SL;
        }
        return null;
    }

    public StudentsList GetTetcher(String Name) {
        MySqlConnections DB = new MySqlConnections();
        Map<String, String> stuHashT = new HashMap<>();
        stuHashT.put("isTecher", "1");
        stuHashT.put("name", Name);
        List<List> ResultsT = DB.get("Users", stuHashT, java.util.Arrays.asList("id", "name", "isBlock", "astart", "aend", "lastlogin", "created", "phonenumber", "cardid", "birthdate", "notes1"));
        for (List Result : ResultsT) {
            stuHashT.clear();
            stuHashT.put("techid", String.valueOf(Result.get(0)));
            stuHashT.put("active", "0");
            StudentsList SL = new StudentsList();
            List<List> Groups = DB.get("groups", stuHashT, java.util.Arrays.asList("groupname"));

            SL.setUserID(Integer.parseInt(String.valueOf(Result.get(0))));
            SL.setName(String.valueOf(Result.get(1)));
            SL.setisBlock((Result.get(2).toString().equals("1")));
            SL.setStartDate(String.valueOf(Result.get(3)));
            SL.setEndDate(String.valueOf(Result.get(4)));
            SL.setLastLogin(String.valueOf(Result.get(5)));
            SL.setCreatedDate(String.valueOf(Result.get(6)));
            SL.setPhoneNumber(String.valueOf(Result.get(7)));
            SL.setCardID(String.valueOf(Result.get(8)));
            SL.setBirthdate(String.valueOf(Result.get(9)));
            SL.setNote(String.valueOf(Result.get(10)));
            SL.setCategory(1);
            if (Groups != null) {
                for (int LoopGroup = 0; LoopGroup <= Groups.size() - 1; LoopGroup++) {
                    SL.setGroup(String.valueOf(Groups.get(LoopGroup).get(0)));
                }
            } else {
                SL.setGroup("");
            }

            return SL;
        }
        return null;
    }

    public StudentsList GetTetcherByCardID(String CardID) {
        MySqlConnections DB = new MySqlConnections();
        Map<String, String> stuHashT = new HashMap<>();
        stuHashT.put("isTecher", "1");
        stuHashT.put("cardid", CardID);
        List<List> ResultsT = DB.get("Users", stuHashT, java.util.Arrays.asList("id", "name", "isBlock", "astart", "aend", "lastlogin", "created", "phonenumber", "cardid", "birthdate", "notes1"));
        for (List Result : ResultsT) {
            stuHashT.clear();
            stuHashT.put("techid", String.valueOf(Result.get(0)));
            stuHashT.put("active", "0");
            StudentsList SL = new StudentsList();
            List<List> Groups = DB.get("groups", stuHashT, java.util.Arrays.asList("groupname"));

            SL.setUserID(Integer.parseInt(String.valueOf(Result.get(0))));
            SL.setName(String.valueOf(Result.get(1)));
            SL.setisBlock((Result.get(2).toString().equals("1")));
            SL.setStartDate(String.valueOf(Result.get(3)));
            SL.setEndDate(String.valueOf(Result.get(4)));
            SL.setLastLogin(String.valueOf(Result.get(5)));
            SL.setCreatedDate(String.valueOf(Result.get(6)));
            SL.setPhoneNumber(String.valueOf(Result.get(7)));
            SL.setCardID(String.valueOf(Result.get(8)));
            SL.setBirthdate(String.valueOf(Result.get(9)));
            SL.setNote(String.valueOf(Result.get(10)));
            SL.setCategory(1);
            if (Groups != null) {
                for (int LoopGroup = 0; LoopGroup <= Groups.size() - 1; LoopGroup++) {
                    SL.setGroup(String.valueOf(Groups.get(LoopGroup).get(0)));
                }
            } else {
                SL.setGroup("");
            }

            return SL;
        }
        return null;
    }

    public List<GroupList> getGroups() {
        try {
            List<GroupList> List = new ArrayList<>();
            MySqlConnections DB = new MySqlConnections();
            ResultSet Results = DB.getAll("groups");
            while (Results.next()) {
                GroupList Data = new GroupList();
                Data.setGroupID(Results.getInt("id"));
                Data.setGroupName(Results.getString("groupname"));
                Data.setGroupTecher(Results.getInt("techid"));
                Data.setGroupDescription(Results.getString("description"));
                Data.setGroupActive((Results.getInt("active") == 0 ? true : false));
                List.add(Data);
            }
            return List;
        } catch (SQLException ex) {
        }
        return null;
    }

    public int getGroupID(String Name) {
        try {
            MySqlConnections DB = new MySqlConnections();
            ResultSet Results = DB.getAll("groups");
            while (Results.next()) {
                if (Name.equals(Results.getString("groupname"))) {
                    return Results.getInt("id");
                }
            }
            return 0;
        } catch (SQLException ex) {
        }
        return 0;
    }
}
