/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rfid;

import com.alee.extended.statusbar.WebStatusLabel;
import com.fazecast.jSerialComm.SerialPort;
import java.nio.charset.Charset;

/**
 *
 * @author alienware
 */
public class Scaning implements Runnable {

    public String Status;
    private volatile boolean stop = false;
    private volatile Thread workingThread;
    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    private WebStatusLabel status;
    private WebStatusLabel AccountNumber = null;
    protected int TimeHold = 0;
    protected int TimeCheck = 0;
    private SerialPort comPort = null;

    private final String WAIT_YOUR_CARD = "Ожидание вашей карты";
    private final String DEV_NOT_CONNECTED = "устройство не подключено";
    private final String SESSION_START = "работа сессии";
    private final String SESSION_CLOSE = "сессия закрыта";

    Scaning(WebStatusLabel lblstatus, WebStatusLabel HoldAccountNumber) {
        this.status = lblstatus;
        this.AccountNumber = HoldAccountNumber;
        ScanDevice(status);
    }

    /* Signal the Task should stop as soon as it is safe to do so. */
    public void stop() {
        if (this.workingThread != null) {
            this.workingThread.interrupt();
        }
    }

    String decodeUTF8(byte[] bytes) {
        return new String(bytes, UTF8_CHARSET);
    }

    byte[] encodeUTF8(String string) {
        return string.getBytes(UTF8_CHARSET);
    }

    public void ScanDevice(WebStatusLabel status) {
        do {
            SerialPort[] DeviceRFID = SerialPort.getCommPorts();
            if (DeviceRFID.length > 0) {
                String Name = DeviceRFID[0].getPortDescription();
                if (Name.equals("Arduino Leonardo")) {
                    this.status.setText(WAIT_YOUR_CARD);
                    if (!DeviceRFID[0].isOpen()) {
                        DeviceRFID[0].openPort(1500);
                    }
                    if (this.comPort != null) {
                        this.comPort.closePort();
                        this.comPort = null;
                    }
                    this.comPort = DeviceRFID[0];
                    this.comPort.setBreak();
                    this.comPort.setFlowControl(SerialPort.FLOW_CONTROL_RTS_ENABLED | SerialPort.FLOW_CONTROL_CTS_ENABLED);
                    this.comPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 2000, 0);
                }
            } else {
                this.status.setText(DEV_NOT_CONNECTED);
                if (this.comPort != null) {
                    if (this.comPort.isOpen()) {
                        this.comPort.closePort();
                    }
                    this.comPort = null;
                }
            }
        } while (this.comPort == null);
    }

    @Override
    public void run() {
        if (this.stop) {
            return;
        }
        this.workingThread = Thread.currentThread();
        while (!this.workingThread.isInterrupted()) {
            if (this.comPort != null) {
                try {
                    byte[] readBuffer = new byte[1024];
                    int numRead = this.comPort.readBytes(readBuffer, readBuffer.length);
                    if (numRead > 4) {
                        String DataString = decodeUTF8(readBuffer).trim().toString();
                        if (DataString.contains("TRUE")) {
                            String[] Serial = DataString.split(":");
                            if (Serial.length > 0) {
                                this.TimeHold = 1;
                                this.AccountNumber.setText(Serial[1].replaceAll("\\D+", ""));
                                this.status.setText(SESSION_START);
                            }
                        } else if (DataString.contains("FALSE")) {
                            if ((this.TimeHold == 0)) {
                                this.status.setText(WAIT_YOUR_CARD);
                                this.AccountNumber.setText("---");
                            } else {
                                this.AccountNumber.setText("---");
                                this.status.setText(SESSION_CLOSE);
                                this.TimeHold++;
                                if (this.TimeHold == 3) {
                                    if (this.comPort.isOpen()) {
                                        this.status.setText(WAIT_YOUR_CARD);
                                        this.TimeHold = 0;
                                        this.TimeCheck = 0;
                                    } else {
                                        this.status.setText(DEV_NOT_CONNECTED);
                                        ScanDevice(status);
                                    }
                                }
                            }
                        }
                    }
                    Thread.sleep(2500);
                    if (numRead == 0) {
                        this.TimeCheck++;
                    } else {
                        this.TimeCheck = 0;
                    }
                    if (this.TimeCheck >= 2) {
                        do {
                            this.status.setText(DEV_NOT_CONNECTED);
                            ScanDevice(status);
                        } while (comPort.isOpen());
                        this.TimeCheck = 0;
                    }
                } catch (java.lang.ArrayIndexOutOfBoundsException | InterruptedException e) {

                }
            }
        }
    }
}
