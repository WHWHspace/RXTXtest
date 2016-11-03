import ComPortHelper.SerialPortHelper;
import ComPortHelper.SerialPortListener;
import gnu.io.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TooManyListenersException;

/**
 * 使用实例
 * Created by 31344 on 2016-09-27.
 */
public class Main {

    public static void main(String[] args){
        new Main().run();
    }

    private void run() {
        ArrayList<String> portNames = SerialPortHelper.findPorts();
//        portNames.forEach(System.out::println);

        SerialPort serialPort = null;

        try {
            serialPort = SerialPortHelper.openPort("COM4",9600, SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
        } catch (NoSuchPortException e) {
            e.printStackTrace();
        } catch (PortInUseException e) {
            e.printStackTrace();
        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
        }

        try {
            SerialPortHelper.sendToPort(serialPort,new byte[]{48,48,48});
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            SerialPortHelper.addListener(serialPort,new SerialPortListener(serialPort));

        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();


        SerialPortHelper.closePort(serialPort);
    }
}
