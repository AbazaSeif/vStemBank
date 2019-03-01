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
    private static boolean key1 = false;
    private static boolean key2 = false;
    private static boolean key3 = false;
    private static boolean key4 = false;
    private static JFrame Main = null;

    public KeybordEvent(WebStatusLabel lblstatus, WebStatusLabel lblaccountnumber) {
        this.status = lblstatus;
        this.AccountNumber = lblaccountnumber;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        switch (e.getKeyCode()) {
            case 44://Z
                key1 = true;
                break;
            case 45://X
                key2 = true;
                break;
            case 25://P
                key3 = true;
                break;
            case 43://\
                key4 = true;
                break;
            default:
                key1 = key2 = key3 = key4 = false;
                System.out.println("RESET");
                break;
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        if (key1 == true) {
            if (key2 == true) {
                if (key3 == true) {
                    if (key4 == true) {
                        System.out.println("Key is DONE");
                        System.exit(0);
                    }
                }
            }
        }
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
