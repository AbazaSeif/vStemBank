/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rfid;

import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.tree.WebTreeModel;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;
import database.DatabaseImplement;
import database.MySqlConnections;
import database.StudentsList;
import helper.helper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author alienware
 */
public class UsersDataDir extends com.alee.laf.desktoppane.WebInternalFrame {

    private final String Header = "School";
    private final String Students = "Students";
    private final String Teachers = "Teachers";
    private DatabaseImplement DBI = null;

    /**
     * Creates new form UsersDataDir
     */
    public UsersDataDir() {
        initComponents();
        lblID.setVisible(false);
        helper helping = new helper();
        chBlock.setRound(11);
        chBlock.setLeftComponent(helping.createSwitchIcon(helping.loadIcon("on.png"), 4, 0));
        chBlock.setRightComponent(helping.createSwitchIcon(helping.loadIcon("off.png"), 0, 4));
        TooltipManager.setTooltip(chBlock, loadIcon("exit.png"), "Заблокирован", TooltipWay.down, 0);
    }

    private void ClearAllInputs() {
        Boolean Status = false;
        btnIncBalance.setEnabled(Status);
        btnDencBalance.setEnabled(Status);
        webButton1.setEnabled(Status);
        webButton2.setEnabled(Status);
        chBlock.setEnabled(Status);
        lblID.setText("");
        txtName.setText("");
        txtBalance.setText("");
        txtBirthdate.setText("");
        txtStart.setText("");
        txtEnd.setText("");
        txtCardID.setText("");
        txtPhone.setText("");
        txtParnName.setText("");
        txtParnPhone.setText("");
        txtNote.setText("");
        cmbGroup.removeAllItems();
        lblID.setText("");

    }

    public void Start() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(Header);
        TreeModel rootstud = new WebTreeModel(root);
        sysdir.removeAll();
        sysdir.setModel(rootstud);
        initTree(root);
        sysdir.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        try {
            sysdir.addTreeSelectionListener((TreeSelectionEvent e) -> {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) sysdir.getLastSelectedPathComponent();
                if (selectedNode != null) {
                    GetData(sysdir.getSelectedNode().getParent().toString(), sysdir.getSelectedNode().toString());
                }
            });
        } catch (NullPointerException e) {
        }
        ClearAllInputs();
    }

    private void initTree(DefaultMutableTreeNode root) {
        DefaultTreeModel model = (DefaultTreeModel) sysdir.getModel();
        // Find node to which new node is to be added
        TreePath path = sysdir.getPathForNode(root);
        MutableTreeNode node = (MutableTreeNode) path.getLastPathComponent();

        DefaultMutableTreeNode studin = new DefaultMutableTreeNode(Students);
        List<StudentsList> listStud = DBI.QStudents();
        if (listStud != null) {
            for (int i = 0; i < listStud.size(); i++) {
                StudentsList get = listStud.get(i);
                studin.add(new DefaultMutableTreeNode(get.getName()));
            }
        }
        // Insert new node as last child of node
        model.insertNodeInto(studin, node, node.getChildCount());
        sysdir.expandPath(path);

        DefaultMutableTreeNode techer = new DefaultMutableTreeNode(Teachers);
        List<StudentsList> listTetchers = DBI.QTeachers();
        if (listTetchers != null) {
            for (int i = 0; i < listTetchers.size(); i++) {
                StudentsList get = listTetchers.get(i);
                techer.add(new DefaultMutableTreeNode(get.getName()));
            }
        }
        model.insertNodeInto(techer, node, node.getChildCount());
        sysdir.expandPath(path);
        sysdir.updateAllVisibleNodes();
        sysdir.updateUI();
    }

    private void GetData(String Root, String Name) {
        MySqlConnections DB = new MySqlConnections();
        ClearAllInputs();
        boolean Status = false;
        try {
            switch (Root) {
                case Students:
                    Status = true;
                    chBlock.setEnabled(true);
                    jLabel13.setVisible(Status);
                    jLabel4.setVisible(Status);
                    jLabel7.setVisible(Status);
                    txtBalance.setVisible(Status);
                    txtParnName.setVisible(Status);
                    txtParnPhone.setVisible(Status);
                    btnIncBalance.setVisible(Status);
                    btnDencBalance.setVisible(Status);
                    btnIncBalance.setEnabled(Status);
                    btnDencBalance.setEnabled(Status);
                    StudentsList SL = new DatabaseImplement().GetStudent(Name);
                    webButton1.setEnabled(true);
                    webButton2.setEnabled(true);
                    chBlock.setSelected(SL.getisBlocked(), true);
                    txtName.setText(SL.getName());
                    txtBalance.setText(String.valueOf(SL.getAmount()));
                    txtBirthdate.setText(SL.getBirthdate());
                    txtCardID.setText(SL.getCardID());
                    txtStart.setText(SL.getStartDate());
                    txtEnd.setText(SL.getEndDate());
                    txtPhone.setText(SL.getPhoneNumber());
                    txtParnName.setText(SL.getParentName());
                    txtParnPhone.setText(SL.getParentPhone());
                    txtNote.setText(SL.getNote());
                    String Groups = SL.getGroup();
                    if (Groups.contains(",")) {
                        String Item[] = Groups.split(",");
                        for (String ite : Item) {
                            cmbGroup.addItem(ite);
                        }
                    } else if (!Groups.isEmpty()) {
                        cmbGroup.addItem(Groups);
                    }
                    lblID.setText(String.valueOf(SL.getUserID()));
                    break;
                case Teachers:
                    Status = false;
                    chBlock.setEnabled(true);
                    jLabel13.setVisible(Status);
                    jLabel4.setVisible(Status);
                    jLabel7.setVisible(Status);
                    txtBalance.setVisible(Status);
                    txtParnName.setVisible(Status);
                    txtParnPhone.setVisible(Status);
                    btnIncBalance.setVisible(Status);
                    btnDencBalance.setVisible(Status);
                    btnIncBalance.setEnabled(Status);
                    btnDencBalance.setEnabled(Status);
                    StudentsList SLT = new DatabaseImplement().GetTetcher(Name);
                    webButton1.setEnabled(true);
                    webButton2.setEnabled(true);
                    chBlock.setSelected(SLT.getisBlocked(), true);
                    txtName.setText(SLT.getName());
                    txtBirthdate.setText(SLT.getBirthdate());
                    txtCardID.setText(SLT.getCardID());
                    txtStart.setText(SLT.getStartDate());
                    txtEnd.setText(SLT.getEndDate());
                    txtPhone.setText(SLT.getPhoneNumber());
                    txtNote.setText(SLT.getNote());
                    String TGroups = SLT.getGroup();
                    if (TGroups.contains(",")) {
                        String Item[] = TGroups.split(",");
                        for (String ite : Item) {
                            cmbGroup.addItem(ite);
                        }
                    } else if (!TGroups.isEmpty()) {
                        cmbGroup.addItem(TGroups);
                    }
                    lblID.setText(String.valueOf(SLT.getUserID()));

                    break;
            }
        } catch (NullPointerException e) {

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        webMultiLineLabel1 = new com.alee.extended.label.WebMultiLineLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        chBlock = new com.alee.extended.button.WebSwitch();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtNote = new com.alee.laf.text.WebTextPane();
        webButton1 = new com.alee.laf.button.WebButton();
        webButton2 = new com.alee.laf.button.WebButton();
        txtName = new com.alee.laf.text.WebTextField();
        txtCardID = new com.alee.laf.text.WebTextField();
        txtBirthdate = new com.alee.extended.date.WebDateField();
        cmbGroup = new com.alee.laf.combobox.WebComboBox();
        txtParnName = new com.alee.laf.text.WebTextField();
        txtParnPhone = new com.alee.laf.text.WebTextField();
        txtPhone = new com.alee.laf.text.WebTextField();
        txtBalance = new com.alee.laf.label.WebLabel();
        jLabel13 = new javax.swing.JLabel();
        lblID = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtStart = new com.alee.extended.date.WebDateField();
        txtEnd = new com.alee.extended.date.WebDateField();
        btnIncBalance = new com.alee.laf.button.WebButton();
        btnDencBalance = new com.alee.laf.button.WebButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        sysdir = new com.alee.laf.tree.WebTree();
        webLabel2 = new com.alee.laf.label.WebLabel();

        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameActivated(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameDeactivated(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rfid/image/emblem-generic.png"))); // NOI18N

        webMultiLineLabel1.setText("Поиск списков и отчетов о членах и возможность изменения их полномочий");
        webMultiLineLabel1.setDrawShade(true);
        webMultiLineLabel1.setFont(webMultiLineLabel1.getFont());
        webMultiLineLabel1.setIconTextGap(6);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(webMultiLineLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
            .addComponent(webMultiLineLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel5.setBorder(null);

        jLabel2.setText("ФИО");

        jLabel3.setText("Карта");

        jLabel4.setText("ФИО родителя");

        jLabel5.setText("Дата рождения");

        jLabel6.setText("Группа");

        jLabel7.setText("Телефон родителя");

        jLabel8.setText("Телефон");

        jLabel9.setText("Комментарий");

        chBlock.setToolTipText("");

        jScrollPane2.setViewportView(txtNote);

        webButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rfid/icons/on.png"))); // NOI18N
        webButton1.setText("обновить данные");
        webButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                webButton1ActionPerformed(evt);
            }
        });

        webButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rfid/icons/off.png"))); // NOI18N
        webButton2.setText("Удалить");
        webButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                webButton2ActionPerformed(evt);
            }
        });

        txtName.setRound(9);

        txtCardID.setEditable(false);
        txtCardID.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCardID.setText("00000000000000000");
        txtCardID.setDrawBackground(false);
        txtCardID.setRound(9);

        txtBirthdate.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtParnName.setRound(9);

        txtParnPhone.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtParnPhone.setText("+000000000");
        txtParnPhone.setRound(9);

        txtPhone.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPhone.setText("+000000000");
        txtPhone.setRound(9);

        txtBalance.setText("0.00");

        jLabel13.setText("Баланс");

        lblID.setText("ID");
        lblID.setFocusable(false);
        lblID.setInheritsPopupMenu(false);
        lblID.setRequestFocusEnabled(false);
        lblID.setVerifyInputWhenFocusTarget(false);

        jLabel10.setText("Дата начала");

        jLabel14.setText("Дата окончания");

        btnIncBalance.setBorder(null);
        btnIncBalance.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rfid/icons/list-add.png"))); // NOI18N
        btnIncBalance.setToolTipText("Зачислить");
        btnIncBalance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIncBalanceActionPerformed(evt);
            }
        });

        btnDencBalance.setBorder(null);
        btnDencBalance.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rfid/icons/list-remove.png"))); // NOI18N
        btnDencBalance.setToolTipText("Списать");
        btnDencBalance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDencBalanceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(chBlock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblID)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(webButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(webButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jScrollPane2))
            .addGroup(javax.swing.GroupLayout.Alignment.CENTER, jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(355, 355, 355))
            .addGroup(javax.swing.GroupLayout.Alignment.CENTER, jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(txtBirthdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(355, 355, 355))
            .addGroup(javax.swing.GroupLayout.Alignment.CENTER, jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(txtParnName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(355, 355, 355))
            .addGroup(javax.swing.GroupLayout.Alignment.CENTER, jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(txtStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(355, 355, 355))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(374, 374, 374)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtBalance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnIncBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDencBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(txtEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtParnPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCardID, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel9))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel4))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel2))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel5))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel10)
                        .addGap(266, 266, 266)
                        .addComponent(jLabel14))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(374, 374, 374)
                        .addComponent(jLabel6))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(374, 374, 374)
                        .addComponent(jLabel7))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(373, 373, 373)
                        .addComponent(jLabel3)))
                .addContainerGap())
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {webButton1, webButton2});

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cmbGroup, txtCardID, txtEnd, txtParnPhone});

        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCardID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtBirthdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIncBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnDencBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtParnName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtParnPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel13)
                            .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chBlock, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(webButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(webButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblID)))
                .addContainerGap())
        );

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        sysdir.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane1.setViewportView(sysdir);

        webLabel2.setText("редактирование данных");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(webLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(webLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        DBI = new DatabaseImplement();
        Start();
    }//GEN-LAST:event_formInternalFrameActivated

    private void formInternalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeactivated
    }//GEN-LAST:event_formInternalFrameDeactivated


    private void webButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webButton1ActionPerformed
        String UserID = lblID.getText();
        int Result = WebOptionPane.showConfirmDialog(this, "Вы уверены, что хотите обновить эти данные?", "подтвердить", WebOptionPane.YES_NO_OPTION, WebOptionPane.QUESTION_MESSAGE);
        if (Result == WebOptionPane.YES_OPTION) {
            Map<String, String> map = new HashMap<>();
            Map<String, String> mapWhere = new HashMap<>();
            mapWhere.put("id", UserID);
            map.put("name", txtName.getText());
            map.put("phonenumber", txtPhone.getText());
            map.put("birthdate", txtBirthdate.getText());
            map.put("parentphone", txtParnPhone.getText());
            map.put("parentname", txtParnName.getText());
            map.put("notes1", txtNote.getText());
            map.put("isBlock", String.valueOf(chBlock.isSelected()));
            map.put("astart", txtStart.getText());
            map.put("aend", txtEnd.getText());
            MySqlConnections MySQLDB = new MySqlConnections();
            if (MySQLDB.update("Users", map, mapWhere)) {
                map.clear();
                map.put("amount", txtBalance.getText());
                mapWhere.clear();
                mapWhere.put("userid", UserID);
                MySQLDB.update("balance", map, mapWhere);
                ClearAllInputs();
                DefaultTreeModel model = (DefaultTreeModel) sysdir.getModel();
                TreePath[] paths = sysdir.getSelectionPaths();
                if (paths != null) {
                    for (TreePath path : paths) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                        if (node.getParent() != null) {
                            model.removeNodeFromParent(node);
                        }
                    }
                }
                Start();
                WebOptionPane.showMessageDialog(this, "Данные пользователя обновлены", "Выполнено", WebOptionPane.INFORMATION_MESSAGE);
            } else {
                WebOptionPane.showMessageDialog(this, "Существует ошибка", "ошибка", WebOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_webButton1ActionPerformed

    private void webButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webButton2ActionPerformed
        String UserID = lblID.getText();
        int Result = WebOptionPane.showConfirmDialog(this, "Вы уверены, что хотите удалить этого пользователя?", "подтвердить", WebOptionPane.YES_NO_OPTION, WebOptionPane.QUESTION_MESSAGE);
        if (Result == WebOptionPane.YES_OPTION) {
            Map<String, String> mapWhere = new HashMap<>();
            mapWhere.put("id", UserID);
            MySqlConnections MySQLDB = new MySqlConnections();
            if (MySQLDB.Delete("Users", mapWhere)) {
                mapWhere.clear();
                mapWhere.put("userid", UserID);
                MySQLDB.Delete("balance", mapWhere);
                MySQLDB.Delete("usergroup", mapWhere);
                ClearAllInputs();
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) sysdir.getLastSelectedPathComponent();
                selectedNode.removeAllChildren();
                Start();
                sysdir.updateAllVisibleNodes();
                sysdir.updateUI();
                WebOptionPane.showMessageDialog(this, "Пользователь удален", "Выполнено", WebOptionPane.INFORMATION_MESSAGE);
            } else {
                WebOptionPane.showMessageDialog(this, "Существует ошибка", "ошибка", WebOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_webButton2ActionPerformed

    private void btnIncBalanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncBalanceActionPerformed
        int Amount = Integer.parseInt(txtBalance.getText());
        Amount += 1;
        txtBalance.setText(String.valueOf(Amount));
    }//GEN-LAST:event_btnIncBalanceActionPerformed

    private void btnDencBalanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDencBalanceActionPerformed
        int Amount = Integer.parseInt(txtBalance.getText());
        if (Amount != 0) {
            Amount -= 1;
        }
        txtBalance.setText(String.valueOf(Amount));
    }//GEN-LAST:event_btnDencBalanceActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.alee.laf.button.WebButton btnDencBalance;
    private com.alee.laf.button.WebButton btnIncBalance;
    private com.alee.extended.button.WebSwitch chBlock;
    private com.alee.laf.combobox.WebComboBox cmbGroup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblID;
    private com.alee.laf.tree.WebTree sysdir;
    private com.alee.laf.label.WebLabel txtBalance;
    private com.alee.extended.date.WebDateField txtBirthdate;
    private com.alee.laf.text.WebTextField txtCardID;
    private com.alee.extended.date.WebDateField txtEnd;
    private com.alee.laf.text.WebTextField txtName;
    private com.alee.laf.text.WebTextPane txtNote;
    private com.alee.laf.text.WebTextField txtParnName;
    private com.alee.laf.text.WebTextField txtParnPhone;
    private com.alee.laf.text.WebTextField txtPhone;
    private com.alee.extended.date.WebDateField txtStart;
    private com.alee.laf.button.WebButton webButton1;
    private com.alee.laf.button.WebButton webButton2;
    private com.alee.laf.label.WebLabel webLabel2;
    private com.alee.extended.label.WebMultiLineLabel webMultiLineLabel1;
    // End of variables declaration//GEN-END:variables

    private ImageIcon loadIcon(String searchpng) {
        helper hlp = new helper();
        return hlp.loadIcon(searchpng);
    }
}
