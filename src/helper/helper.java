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
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import rfid.ScreenView;

/**
 *
 * @author alienware
 */
public class helper {

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
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
}
