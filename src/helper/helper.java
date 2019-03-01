/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.managers.notification.NotificationManager;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import rfid.ScreenView;

/**
 *
 * @author alienware
 */
public class helper {
    
    private Pattern VALID_IPV4_PATTERN = null;
    private final String ipv4Pattern = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
    
    public void ShowNotification(String Message) {
        CloseAllNotifications();
        NotificationManager.setLocation(NotificationManager.CENTER);
        NotificationManager.showNotification(Message);
        NotificationManager.updateNotificationLayouts();
    }
    
    public void CloseAllNotifications() {
        NotificationManager.hideAllNotifications();
    }
    
    public void setWarningMsg(Component OnComponans, String text) {
        WebOptionPane.showMessageDialog(OnComponans, text, "Warning", WebOptionPane.WARNING_MESSAGE);
    }
    
    public void setErrorMsg(Component OnComponans, String text) {
        WebOptionPane.showMessageDialog(OnComponans, text, "Error", WebOptionPane.ERROR_MESSAGE);
    }
    
    public String GetTimeNow() {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(dt);
    }
    
    public URL getIconResource(String path) {
        return ScreenView.class.getResource("icons/" + path);
    }
    
    public ImageIcon loadIcon(String path) {
        return new ImageIcon(this.getIconResource(path));
    }
    
    public BufferedImage LoadImage(final String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(ScreenView.class.getResource("image/" + path));
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public WebLabel createSwitchIcon(ImageIcon icon, final int left, final int right) {
        final WebLabel rightComponent = new WebLabel(icon, WebLabel.CENTER);
        rightComponent.setMargin(2, left, 2, right);
        return rightComponent;
    }
    
    public boolean isIpAddress(String ipAddress) {
        String lipAddress = ipAddress.replace("/", "");
        if (lipAddress.equals("127.0.0.1")) {
            return false;
        }
        VALID_IPV4_PATTERN = Pattern.compile(ipv4Pattern, Pattern.CASE_INSENSITIVE);
        Matcher m1 = VALID_IPV4_PATTERN.matcher(lipAddress);
        return m1.matches();
    }
    
    public void shutdown() throws RuntimeException, IOException {
        String shutdownCommand;
        String operatingSystem = System.getProperty("os.name");
        
        if ("Linux".equals(operatingSystem) || "Mac OS X".equals(operatingSystem)) {
            shutdownCommand = "shutdown -h now";
        } else if ("Windows".equals(operatingSystem)) {
            shutdownCommand = "shutdown.exe -s -t 0";
        } else if (operatingSystem.contentEquals("Windows")) {
            shutdownCommand = "shutdown.exe -s -t 0";
        } else {
            throw new RuntimeException("Unsupported operating system.");
        }
        
        Runtime.getRuntime().exec(shutdownCommand);
        System.exit(0);
    }
    
}
