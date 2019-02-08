/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import com.alee.extended.statusbar.WebStatusLabel;
import javax.swing.JFrame;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author alienware
 */
public class KeybordEvent implements NativeKeyListener {

    private WebStatusLabel status;
    private WebStatusLabel AccountNumber = null;
    private static JFrame Main = null;

    public KeybordEvent(WebStatusLabel lblstatus, WebStatusLabel lblaccountnumber) {
        this.status = lblstatus;
        this.AccountNumber = lblaccountnumber;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {

        if (e.getKeyCode() == NativeKeyEvent.VC_1) {
            try {
                GlobalScreen.unregisterNativeHook();
                this.AccountNumber.setText("2105910749179");
                this.status.setText("Emargancy Status");
            } catch (NativeHookException ex) {
            }
        }
        if (e.getKeyCode() == NativeKeyEvent.VC_2) {
            try {
                GlobalScreen.unregisterNativeHook();
                this.AccountNumber.setText("31231232131221332312");
                this.status.setText("Emargancy Status");
            } catch (NativeHookException ex) {
            }
        }
        if (e.isActionKey()) {
            new helper().ShowNotification("Пожалуйста, введите свои карты на RFID Место");
            KeybordEvent.Main.setFocusable(true);
            KeybordEvent.Main.setAlwaysOnTop(true);
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
    }

    public void KeyboardAction(JFrame mainpage) {
        try {
            KeybordEvent.Main = mainpage;
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
        }
        GlobalScreen.addNativeKeyListener(new KeybordEvent(this.status, this.AccountNumber));
    }

}
