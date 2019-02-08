/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rfid;

import database.MySqlConnections;
import database.StudentsList;
import helper.helper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alienware
 */
public class ListOfUsers extends com.alee.laf.desktoppane.WebInternalFrame {

    /**
     * Creates new form ListOfUsers
     */
    public ListOfUsers() {
        initComponents();
        helper helping = new helper();
        chBlock.setRound(11);
        chBlock.setLeftComponent(helping.createSwitchIcon(helping.loadIcon("on.png"), 4, 0));
        chBlock.setRightComponent(helping.createSwitchIcon(helping.loadIcon("off.png"), 0, 4));
        LoadStudientTable();
    }

    private void LoadStudientTable() {
        tbStudints.setAutoCreateColumnsFromModel(true);

        MySqlConnections DB = new MySqlConnections();
        Map<String, String> stuHash = new HashMap<>();
        stuHash.put("isStudent", "1");
        List<List> Results = DB.get("Users", stuHash, java.util.Arrays.asList("id", "name", "isBlock", "astart", "aend", "lastlogin", "created", "phonenumber", "cardid"));
        List<StudentsList> ResultStudent = new ArrayList<>();
        for (List Result : Results) {
            stuHash.clear();
            stuHash.put("userid", String.valueOf(Result.get(0)));
            StudentsList SL = new StudentsList();

            List<List> Amount = DB.get("balance", stuHash, java.util.Arrays.asList("amount"));
            List<List> Groups = DB.get("usergroup", stuHash, java.util.Arrays.asList("groupid"));

            SL.setName(String.valueOf(Result.get(1)));
            SL.setisBlock(Boolean.getBoolean((String) Result.get(2)));
            SL.setStartDate(String.valueOf(Result.get(3)));
            SL.setEndDate(String.valueOf(Result.get(4)));
            SL.setLastLogin(String.valueOf(Result.get(5)));
            SL.setCreatedDate(String.valueOf(Result.get(6)));
            SL.setPhoneNumber(String.valueOf(Result.get(7)));
            SL.setCardID(String.valueOf(Result.get(8)));
            SL.setCategory(1);
            SL.setAmount(Integer.parseInt((String) Amount.get(0).get(0)));
            Groups.stream().map((UserGroup) -> {
                stuHash.clear();
                stuHash.put("id", String.valueOf(UserGroup.get(0)));
                return UserGroup;
            }).map((_item) -> DB.get("groups", stuHash, java.util.Arrays.asList("groupname"))).forEachOrdered((GroupName) -> {
                SL.setGroup(String.valueOf(GroupName.get(0).get(0)));
            });

            ResultStudent.add(SL);
        }
        
        
        
        
        
        Object header[] = new Object[]{
            "ФИО", "Телефон", "Баланс", "Группа", "Зачислить", "Списать", "Блокировка"
        };
        final Class[] columnClass = new Class[]{
            String.class, String.class, Double.class, String.class, String.class, String.class, Boolean.class
        };
        DefaultTableModel DTM = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnClass[columnIndex];
            }
        };

        DTM.setColumnIdentifiers(header);

        for (int i = 0; i < ResultStudent.size(); i++) {
            Vector<Object> data = new Vector<>();
            StudentsList get = ResultStudent.get(i);
            data.add(get.getName());
            data.add(get.getPhoneNumber());
            data.add(get.getAmount());
            data.add(get.getGroup());
            data.add(get.getStartDate());
            data.add(get.getEndDate());
            data.add(!get.getisBlocked());
            data.add(get.getName());
            DTM.addRow(data);
        }
        tbStudints.setModel(DTM);
        tbStudints.updateUI();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        webPanel2 = new com.alee.laf.panel.WebPanel();
        webMultiLineLabel1 = new com.alee.extended.label.WebMultiLineLabel();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        webTextField1 = new com.alee.laf.text.WebTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbStudints = new com.alee.laf.table.WebTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        webTextField2 = new com.alee.laf.text.WebTextField();
        webTextField3 = new com.alee.laf.text.WebTextField();
        webTextField4 = new com.alee.laf.text.WebTextField();
        webTextField5 = new com.alee.laf.text.WebTextField();
        webTextField6 = new com.alee.laf.text.WebTextField();
        chBlock = new com.alee.extended.button.WebSwitch();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        webTextPane1 = new com.alee.laf.text.WebTextPane();
        webButton1 = new com.alee.laf.button.WebButton();
        webButton2 = new com.alee.laf.button.WebButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();

        setTitle("List Of Users");

        webMultiLineLabel1.setText("Поиск списков и отчетов о членах и возможность изменения их полномочий");
        webMultiLineLabel1.setDrawShade(true);
        webMultiLineLabel1.setFont(webMultiLineLabel1.getFont());
        webMultiLineLabel1.setIconTextGap(6);
        webPanel2.add(webMultiLineLabel1, java.awt.BorderLayout.CENTER);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rfid/image/emblem-generic.png"))); // NOI18N
        webPanel2.add(jLabel1, java.awt.BorderLayout.LINE_START);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("поиск"));

        webTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        webTextField1.setToolTipText("Поиск по любым параметрам");
        webTextField1.setRound(10);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(webTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(webTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tbStudints.setAutoCreateRowSorter(true);
        tbStudints.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tbStudints.setEditable(false);
        tbStudints.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tbStudints);
        tbStudints.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("редактирование данных"));

        jLabel2.setText("ФИО");

        jLabel3.setText("Карта");

        jLabel4.setText("ФИО родителя");

        jLabel5.setText("Дата рождения");

        jLabel6.setText("Группа");

        jLabel7.setText("Телефон родителя");

        jLabel8.setText("Телефон");

        jLabel9.setText("Комментарий");

        webTextField2.setText("webTextField2");

        webTextField3.setText("webTextField3");

        webTextField4.setText("webTextField4");

        webTextField5.setText("webTextField5");

        webTextField6.setText("webTextField6");

        jLabel10.setText("Заблокирован");

        jScrollPane1.setViewportView(webTextPane1);

        webButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rfid/icons/on.png"))); // NOI18N
        webButton1.setText("обновить данные");

        webButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rfid/icons/off.png"))); // NOI18N
        webButton2.setText("Удалить");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(webTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(webTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(webTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(webTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(webTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(chBlock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(webButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(webButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5)
                    .addComponent(jLabel8)
                    .addComponent(webTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(webTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6)
                    .addComponent(webTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel7)
                        .addComponent(webTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(webTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10))
                    .addComponent(chBlock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(webButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(webButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1145, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Студенты", jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1145, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 794, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Учителя", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1145, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 794, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Администраторы", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(webPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(3, 3, 3))
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(webPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.alee.extended.button.WebSwitch chBlock;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.alee.laf.table.WebTable tbStudints;
    private com.alee.laf.button.WebButton webButton1;
    private com.alee.laf.button.WebButton webButton2;
    private com.alee.extended.label.WebMultiLineLabel webMultiLineLabel1;
    private com.alee.laf.panel.WebPanel webPanel2;
    private com.alee.laf.text.WebTextField webTextField1;
    private com.alee.laf.text.WebTextField webTextField2;
    private com.alee.laf.text.WebTextField webTextField3;
    private com.alee.laf.text.WebTextField webTextField4;
    private com.alee.laf.text.WebTextField webTextField5;
    private com.alee.laf.text.WebTextField webTextField6;
    private com.alee.laf.text.WebTextPane webTextPane1;
    // End of variables declaration//GEN-END:variables
}