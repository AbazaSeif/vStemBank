/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adaptor;

import com.fazecast.jSerialComm.SerialPort;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 *
 * @author alienware
 */
class PortScan extends Thread {

    private SerialPort comPort = null;
    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    protected int TimeHold = 0;
    protected int TimeCheck = 0;
    private String AccountNumber = null;
    private String PathFile = null;

    PortScan(String Path) {
        if (Path.isEmpty()) {
            try {
                CrunchifyGetPropertyValues properties = new CrunchifyGetPropertyValues();
                Map<String, String> stuHashT = properties.getPropValues();
                PathFile = stuHashT.get("path");
            } catch (IOException ex) {
            }
        } else {
            PathFile = Path;
        }
    }

    String decodeUTF8(byte[] bytes) {
        return new String(bytes, UTF8_CHARSET);
    }

    @Override
    public void run() {
        scaning();
    }

    private void WriteToFile(String Data) {
        try (PrintWriter writer = new PrintWriter(PathFile, "UTF-8")) {
            writer.println(Data);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
        }
    }

    private void scaning() {
        while (true) {
            try {
                if (this.comPort == null) {
                    do {
                        SerialPort[] DeviceRFID = SerialPort.getCommPorts();
                        if (DeviceRFID.length > 0) {
                            String Name = DeviceRFID[0].getPortDescription();
                            if (Name.equals("Arduino Leonardo")) {
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
                            if (this.comPort != null) {
                                if (this.comPort.isOpen()) {
                                    this.comPort.closePort();
                                }
                                this.comPort = null;
                            }
                        }
                    } while (this.comPort == null);
                }
                if (this.comPort != null) {
                    byte[] readBuffer = new byte[1024];
                    int numRead = this.comPort.readBytes(readBuffer, readBuffer.length);
                    if (numRead > 4) {
                        String DataString = decodeUTF8(readBuffer).trim();
                        if (DataString.contains("TRUE")) {
                            String[] Serial = DataString.split(":");
                            if (Serial.length > 0) {
                                this.TimeHold = 1;
                                AccountNumber = Serial[1].replaceAll("\\D+", "");
                                WriteToFile(AccountNumber);
                                this.comPort.writeBytes("C".getBytes(), 1);
                            }
                        } else if (DataString.contains("FALSE")) {
                            if ((this.TimeHold == 0)) {
                                AccountNumber = null;
                                WriteToFile("");
                            } else {
                                AccountNumber = null;
                                WriteToFile("");
                                this.TimeHold++;
                                if (this.TimeHold == 3) {
                                    if (this.comPort.isOpen()) {
                                        this.TimeHold = 0;
                                        this.TimeCheck = 0;
                                    } else {
                                        //DEV_NOT_CONNECTED
                                    }
                                }
                            }
                        }
                    }
                    Thread.sleep(1000);
                }
            } catch (InterruptedException ex) {
            }
        }
    }
}

public class Adaptor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String Path = args[0];
        if (Path.isEmpty()) {
            System.exit(0);
        }
        Thread thread = new PortScan(Path);
        thread.start();

    }

}
