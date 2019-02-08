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
import helper.KeybordEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author alienware
 */
public class ScreenView extends javax.swing.JFrame {
    
    private MySqlConnections MySQLDB = null;
    private ControlRoom CR = null;
    static private UserPopup Stud = null;
    private String AccountNumber = null;
    private Timer timer = null;
    private static Boolean isMemberBlocked = false;

    /**
     * Creates new form ScreenView
     */
    public ScreenView() {
        initComponents();
        KeybordEvent keybordEvent = new helper.KeybordEvent(lblstatus, lblaccountnumber);
        keybordEvent.KeyboardAction(this);
        Thread ThrDrvice = new Thread(new Scaning(lblstatus, lblaccountnumber));
        lblaccount.setVisible(false);
        setTitle("");
        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        lblaccountnumber.setVisible(false);
        WebMemoryBar memoryBar = new WebMemoryBar();
        memoryBar.setPreferredWidth(memoryBar.getPreferredSize().width + 20);
        statusBar.add(memoryBar, ToolbarLayout.END);
        timer = new Timer();
        long delay = 1500L;
        long period = 1500L;
        timer.scheduleAtFixedRate(task, delay, period);
        MySQLDB = new MySqlConnections();
        background.setImage(LoadImage("bg.jpg"));
        if (isNetworkReacheble()) {
            if (isInternetReachable()) {
                ThrDrvice.start();
            }
        }
    }
    
    private boolean isNetworkReacheble() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface interf = interfaces.nextElement();
                if (interf.isUp() && !interf.isLoopback()) {
                    return true;
                }
            }
        } catch (SocketException ex) {
            new helper.helper().ShowNotification("Существует проблема в сети связи");
            return false;
        }
        return false;
    }
    
    private boolean isInternetReachable() {
        try {
            URL url = new URL("http://www.google.com");
            HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
            Thread.sleep(20);
            Object objData = urlConnect.getContent();
            return true;
        } catch (IOException | InterruptedException e) {
            new helper.helper().ShowNotification("Интернет не подключен");
            return false;
        }
    }
    
    private void Login() {
        Map<String, String> map = new HashMap<>();
        map.put("cardid", AccountNumber);
        List<List> Result = MySQLDB.get("Users", map, java.util.Arrays.asList("name", "id", "isStudent", "isAdmin", "isTecher", "isBlock"));
        if (Result != null) {
            List<String> ResultData = Result.get(0);
            lblaccount.setVisible(true);
            lblaccount.setText("загрузка " + ResultData.get(0) + "...");
            int isBlocked = Integer.parseInt(ResultData.get(5));
            int Student = Integer.parseInt(ResultData.get(2));
            int Techer = Integer.parseInt(ResultData.get(4));
            int Admin = Integer.parseInt(ResultData.get(3));
            if (Admin == 1) {
                if (isBlocked == 0) {
                    map.clear();
                    Map<String, String> mapWhere = new HashMap<>();
                    String currentTime = new helper.helper().GetTimeNow();
                    map.put("lastlogin", currentTime);
                    mapWhere.put("cardid", AccountNumber);
                    MySQLDB.update("Users", map, mapWhere);
                    
                    if (CR == null) {
                        CR = new ControlRoom();
                        CR.setTitle("Административный контроль");
                        CR.setResizable(false);
                        CR.setExtendedState(JFrame.MAXIMIZED_BOTH);
                        CR.setVisible(true);
                        CR.setResizable(false);
                        CR.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    }
                    task.cancel();
                    dispose();
                } else {
                    isMemberBlocked = true;
                    new helper.helper().ShowNotification("Вы заблокированы, пожалуйста поговорите с супер администрацией");
                    lblaccount.setVisible(true);
                    lblaccount.setText("Аккаунт заблокирован");
                    AccountNumber = null;
                }
            }
            if (Student == 1) {
                if (isBlocked == 0) {
                    map.clear();
                    Map<String, String> mapWhere = new HashMap<>();
                    String currentTime = new helper.helper().GetTimeNow();
                    map.put("lastlogin", currentTime);
                    mapWhere.put("cardid", AccountNumber);
                    MySQLDB.update("Users", map, mapWhere);
                    
                    if ((Stud == null) && (AccountNumber != null)) {
                        Stud = new UserPopup();
                        Stud.setVisible(true);
                        setVisible(false);
                    } else if ((Stud != null) && (AccountNumber == null)) {
                        Stud.setVisible(false);
                        Stud = null;
                    }
                } else {
                    isMemberBlocked = true;
                    new helper.helper().ShowNotification("Вы заблокированы, пожалуйста поговорите с администрацией");
                    lblaccount.setVisible(true);
                    lblaccount.setText("Аккаунт заблокирован");
                    AccountNumber = null;
                }
            }
            if (Techer == 1) {
                if (isBlocked == 0) {
                    map.clear();
                    Map<String, String> mapWhere = new HashMap<>();
                    String currentTime = new helper.helper().GetTimeNow();
                    map.put("lastlogin", currentTime);
                    mapWhere.put("cardid", AccountNumber);
                    MySQLDB.update("Users", map, mapWhere);
                    
                } else {
                    isMemberBlocked = true;
                    new helper.helper().ShowNotification("Вы заблокированы, пожалуйста поговорите с администрацией");
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
            CR = null;
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
        setAlwaysOnTop(true);
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
            .addComponent(statusBar, javax.swing.GroupLayout.DEFAULT_SIZE, 960, Short.MAX_VALUE)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(statusBar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getAccessibleContext().setAccessibleDescription("");
        getAccessibleContext().setAccessibleParent(this);

        setSize(new java.awt.Dimension(960, 770));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void backgroundKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_backgroundKeyPressed
    }//GEN-LAST:event_backgroundKeyPressed
    
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (lblaccountnumber.getText().equals("---")) {
                isMemberBlocked = false;
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
                new helper.helper().CloseAllNotifications();
                lblaccount.setVisible(false);
                lblaccount.setText("");
            } else if (!lblaccountnumber.getText().equals("---")) {
                AccountNumber = lblaccountnumber.getText();
                lblaccount.setVisible(true);
                if (!isMemberBlocked) {
                    Login();
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
            e.printStackTrace();
        }
        return null;
    }
    
}
