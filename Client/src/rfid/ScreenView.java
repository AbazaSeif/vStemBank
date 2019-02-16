/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rfid;

import com.alee.extended.layout.ToolbarLayout;
import com.alee.extended.statusbar.WebMemoryBar;
import com.alee.laf.WebLookAndFeel;
import database.MySqlConnections;
import helper.helper;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author alienware
 */
public class ScreenView extends javax.swing.JFrame {

    private MySqlConnections MySQLDB = null;
    static private UserPopup Stud = null;
    static public String AccountNumber = null;
    private Timer timer = null;
    private static Boolean isMemberBlocked = false;
    private helper HELPCLASS = null;
    private static String ComputerIP = null;
    private static String LessonLabel = null;
    private static int UserID = 0;
    private static int UserFactor = 0;

    /**
     * Creates new form ScreenView
     */
    public ScreenView() {
        try {
            initComponents();
            Thread ThrDrvice = new Thread(new Scaning(lblstatus, lblaccountnumber));
            lblaccount.setVisible(false);
            setTitle("");
            setResizable(false);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            lblaccountnumber.setVisible(false);
            WebMemoryBar memoryBar = new WebMemoryBar();
            memoryBar.setPreferredWidth(memoryBar.getPreferredSize().width + 20);
            statusBar.add(memoryBar, ToolbarLayout.END);
            MySQLDB = new MySqlConnections();
            background.setImage(LoadImage("bg.jpg"));
            HELPCLASS = new helper();
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)) {
                Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    if (HELPCLASS.isIpAddress(inetAddress.toString())) {
                        ComputerIP = inetAddress.toString().replace("/", "");
                        Map<String, String> Record = new HashMap<>();
                        Record.put("ipaddress", ComputerIP);
                        List<List> Result = MySQLDB.get("computercontrol", Record, java.util.Arrays.asList("id"));
                        if (Result == null) {
                            Record.put("mode", "1");
                            Record.put("name", ComputerIP);
                            MySQLDB.set("computercontrol", Record);
                        } else {
                            Map<String, String> Where = new HashMap<>();
                            Where.put("ipaddress", ComputerIP);
                            Record.put("mode", "1");
                            MySQLDB.update("computercontrol", Record, Where);
                        }
                        break;
                    }
                }
            }
            timer = new Timer();
            long delay = 1500L;
            long period = 1500L;
            timer.scheduleAtFixedRate(task, delay, period);
            ThrDrvice.start();
        } catch (SocketException ex) {
            Logger.getLogger(ScreenView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void Login() {
        Map<String, String> map = new HashMap<>();
        map.put("cardid", AccountNumber);
        List<List> Result = MySQLDB.get("Users", map, java.util.Arrays.asList("name", "id", "isStudent", "isBlock", "factor"));
        if (Result != null) {
            List<String> ResultData = Result.get(0);
            lblaccount.setVisible(true);
            lblaccount.setText("загрузка " + ResultData.get(0) + "...");
            UserID = Integer.parseInt(ResultData.get(1));
            int isBlocked = Integer.parseInt(ResultData.get(3));
            UserFactor = Integer.parseInt(ResultData.get(4));
            int Student = Integer.parseInt(ResultData.get(2));
            if (Student == 1) {
                if (isBlocked == 0) {
                    map.clear();
                    Map<String, String> mapWhere = new HashMap<>();
                    String currentTime = HELPCLASS.GetTimeNow();
                    map.put("lastlogin", currentTime);
                    map.put("online", "1");
                    mapWhere.put("cardid", AccountNumber);
                    MySQLDB.update("Users", map, mapWhere);

                    map.clear();
                    map.put("userid", String.valueOf(UserID));
                    List<List> UserGroup = MySQLDB.get("usergroup", map, java.util.Arrays.asList("groupid"));
                    if (UserGroup != null) {
                        for (List entry : UserGroup.subList(0, UserGroup.size())) {
                            String gid = entry.get(0).toString();
                            map.clear();
                            map.put("groupid", gid);
                            map.put("status", "1");
                            List<List> UserWorkingGroup = MySQLDB.get("workinggroup", map, java.util.Arrays.asList("id", "timestart", "label"));
                            if (UserWorkingGroup != null) {
                                try {
                                    int WGID = Integer.parseInt(UserWorkingGroup.get(0).get(0).toString());
                                    LessonLabel = UserWorkingGroup.get(0).get(2).toString();
                                    String dateStart = UserWorkingGroup.get(0).get(1).toString();
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                    String dateStop = HELPCLASS.GetTimeNow();

                                    Date d1 = null;
                                    Date d2 = null;
                                    d2 = format.parse(dateStop);
                                    d1 = format.parse(dateStart);

                                    long diff = d2.getTime() - d1.getTime();
                                    long diffMinutes = diff / (60 * 1000) % 60;
                                    map.clear();
                                    map.put("id", "1");
                                    List<List> Setting = MySQLDB.get("settings", map, java.util.Arrays.asList("sessiondelayed"));
                                    int Delay = Integer.parseInt(Setting.get(0).get(0).toString());

                                    boolean isdelay = false;
                                    if (diffMinutes > Delay) {
                                        isdelay = true;
                                    } else {
                                        isdelay = false;
                                    }

                                    map.clear();
                                    map.put("workinggroupid", String.valueOf(WGID));
                                    map.put("groupid", gid);
                                    map.put("userid", String.valueOf(UserID));
                                    if (isdelay) {
                                        map.put("delay", "1");
                                    } else {
                                        map.put("delay", "0");
                                    }

                                    MySQLDB.set("exestusers", map);

                                } catch (ParseException ex) {
                                }
                            }
                        }
                    }

                    if ((Stud == null) && (AccountNumber != null)) {
                        Stud = new UserPopup(AccountNumber, UserFactor, LessonLabel);
                        Stud.setVisible(true);
                        setVisible(false);
                    } else if ((Stud != null) && (AccountNumber == null)) {
                        Stud.setVisible(false);
                        Stud = null;
                        UserID = 0;
                    }
                } else {
                    isMemberBlocked = true;
                    HELPCLASS.ShowNotification("Вы заблокированы, пожалуйста поговорите с администрацией");
                    lblaccount.setVisible(true);
                    lblaccount.setText("Аккаунт заблокирован");
                    AccountNumber = null;
                }
            }
            map.clear();
            Result.clear();
        } else {
            lblaccount.setVisible(true);
            lblaccount.setText("Аккаунт не найден");
            AccountNumber = null;
            Stud = null;
            if (!isVisible()) {
                setResizable(false);
                setExtendedState(JFrame.MAXIMIZED_BOTH);
                setVisible(true);
            }
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

        statusBar = new com.alee.extended.statusbar.WebStatusBar();
        lblstatus = new com.alee.extended.statusbar.WebStatusLabel();
        lblaccount = new com.alee.extended.statusbar.WebStatusLabel();
        lblaccountnumber = new com.alee.extended.statusbar.WebStatusLabel();
        background = new com.alee.extended.image.WebImage();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ScreenLock");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFocusTraversalPolicyProvider(true);
        setLocationByPlatform(true);
        setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        setName("screen"); // NOI18N
        setUndecorated(true);
        setResizable(false);

        statusBar.setLayout(new com.alee.extended.layout.ToolbarLayout());

        lblstatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rfid/icons/info.png"))); // NOI18N
        lblstatus.setText("Status");
        statusBar.add(lblstatus);

        lblaccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rfid/icons/disabled.png"))); // NOI18N
        statusBar.add(lblaccount);

        lblaccountnumber.setText("---");
        statusBar.add(lblaccountnumber);

        background.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                backgroundKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusBar, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(statusBar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getAccessibleContext().setAccessibleDescription("");
        getAccessibleContext().setAccessibleParent(this);

        setSize(new java.awt.Dimension(521, 334));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void backgroundKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_backgroundKeyPressed
    }//GEN-LAST:event_backgroundKeyPressed

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Map<String, String> Record = new HashMap<>();
            Record.put("ipaddress", ComputerIP);
            List<List> Result = MySQLDB.get("computercontrol", Record, java.util.Arrays.asList("mode"));
            List<String> ResultData = Result.get(0);
            int Status = Integer.parseInt(ResultData.get(0));
            if (Status == 0) {
                try {
                    HELPCLASS.shutdown();
                } catch (RuntimeException | IOException ex) {
                }
            }
            if (lblaccountnumber.getText().equals("---")) {
                isMemberBlocked = false;
                if (AccountNumber != null) {
                    Map<String, String> mapWhere = new HashMap<>();
                    Map<String, String> map = new HashMap<>();
                    map.put("online", "0");
                    mapWhere.put("id", String.valueOf(UserID));
                    mapWhere.put("cardid", AccountNumber);
                    MySQLDB.update("Users", map, mapWhere);
                }
                AccountNumber = null;
                if (Stud != null) {
                    Stud.setVisible(false);
                    Stud = null;
                }
                if (!isVisible()) {
                    setResizable(false);
                    setExtendedState(JFrame.MAXIMIZED_BOTH);
                    setVisible(true);
                }
                HELPCLASS.CloseAllNotifications();
                lblaccount.setVisible(false);
                lblaccount.setText("");
            } else if (!lblaccountnumber.getText().equals("---")) {
                AccountNumber = lblaccountnumber.getText();
                lblaccount.setVisible(true);
                if (!isMemberBlocked) {
                    if (Stud == null) {
                        Login();
                    }
                }
            }
        }
    };

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        WebLookAndFeel.install();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new ScreenView().setVisible(true);
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.alee.extended.image.WebImage background;
    private com.alee.extended.statusbar.WebStatusLabel lblaccount;
    private com.alee.extended.statusbar.WebStatusLabel lblaccountnumber;
    private com.alee.extended.statusbar.WebStatusLabel lblstatus;
    private com.alee.extended.statusbar.WebStatusBar statusBar;
    // End of variables declaration//GEN-END:variables

    private URL getIconResource(String path) {
        return getClass().getResource("icons/" + path);
    }

    public ImageIcon loadIcon(String path) {
        return new ImageIcon(getIconResource(path));
    }

    public BufferedImage LoadImage(final String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResource("image/" + path));
            return image;
        } catch (IOException e) {
        }
        return null;
    }

    public boolean VotingStart() {
        Map<String, String> map = new HashMap<>();
        map.put("userid", String.valueOf(UserID));
        List<List> UserGroup = MySQLDB.get("usergroup", map, java.util.Arrays.asList("groupid"));
        if (UserGroup != null) {
            for (List entry : UserGroup.subList(0, UserGroup.size())) {
                String gid = entry.get(0).toString();
                map.clear();
                map.put("groupid", gid);
                map.put("status", "1");
                List<List> UserWorkingGroup = MySQLDB.get("workinggroup", map, java.util.Arrays.asList("id", "timestart"));
                if (UserWorkingGroup != null) {
                    try {
                        String dateStart = UserWorkingGroup.get(0).get(1).toString();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        String dateStop = HELPCLASS.GetTimeNow();

                        Date d1 = null;
                        Date d2 = null;
                        d2 = format.parse(dateStop);
                        d1 = format.parse(dateStart);

                        long diff = d2.getTime() - d1.getTime();
                        long diffMinutes = diff / (60 * 1000) % 60;
                        map.clear();
                        map.put("id", "1");
                        List<List> Setting = MySQLDB.get("settings", map, java.util.Arrays.asList("conductsurvey"));
                        int Delay = Integer.parseInt(Setting.get(0).get(0).toString());
                        if (diffMinutes >= Delay) {
                            return true;
                        } else {
                            return false;
                        }

                    } catch (ParseException ex) {
                    }
                }
            }
        }
        return false;
    }
}
