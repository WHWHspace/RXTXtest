package ComPortHelper;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;

/**
 * Created by 31344 on 2016-09-27.
 */
public class SerialPortHelper {

    /**
     * 查找本机所有可用的端口
     * @return 所有串口名称列表
     */
    public static ArrayList<String> findPorts(){
        //获得所有可用串口
        Enumeration<CommPortIdentifier> ports = CommPortIdentifier.getPortIdentifiers();
        ArrayList<String> portNames = new ArrayList<String>();
        //读取所有串口的串口名称
        while (ports.hasMoreElements()){
            String name = ports.nextElement().getName();
            portNames.add(name);
        }
        return portNames;
    }

    /**
     * 打开串口
     * @param portName 端口名 COM3
     * @param baudRate 波特率 9600
     * @param dataBits 数据位宽 SerialPort.DATABITS_8
     * @param stopBit 停止位 SerialPort.STOPBITS_1
     * @param parity 奇偶校验 SerialPort.PARITY_NONE
     * @return 串口对象
     * @throws NoSuchPortException 无串口异常
     * @throws PortInUseException 端口占用异常
     * @throws UnsupportedCommOperationException 串口设置异常
     */
    public static SerialPort openPort(String portName,int baudRate,int dataBits,int stopBit, int parity) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
        //通过端口名识别端口
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        //打开端口，并给端口名字和一个timeout（打开操作的超时时间）
        CommPort commPort = portIdentifier.open(portName,2000);
        //判断是不是串口
        if(commPort instanceof SerialPort){
            SerialPort serialPort = (SerialPort) commPort;
            //设置串口参数
            serialPort.setSerialPortParams(baudRate, dataBits, stopBit, parity);
            return serialPort;
        }
        //不是串口，返回null
        return null;
    }

    /**
     * 向串口发送指令
     * @param serialPort 串口对象
     * @param order 指令
     * @throws IOException 发送数据错误
     */
    public static void sendToPort(SerialPort serialPort, byte[] order) throws IOException {
        OutputStream out;
        out = serialPort.getOutputStream();
        out.write(order);
        out.flush();

        out.close();
    }

    /**
     * 读取串口数据
     * @param serialPort 串口对象
     * @return 数据
     * @throws IOException 读取数据时出错
     */
    public static byte[] readFromPort(SerialPort serialPort) throws IOException {
        InputStream in;
        byte[] bytes = null;

        in = serialPort.getInputStream();
        //获取数据长度
        int bufferLength = in.available();
        //初始化bytes并读取数据
        while(bufferLength != 0){
            bytes = new byte[bufferLength];
            in.read(bytes);
            bufferLength = in.available();
        }
        in.close();

        return bytes;
    }

    /**
     * 添加监听
     * @param serialPort 串口对象
     * @param listener 监听器
     * @throws TooManyListenersException 监听器对象过多异常
     */
    public static void addListener(SerialPort serialPort, SerialPortListener listener) throws TooManyListenersException {
        serialPort.addEventListener(listener);

        //设置当有数据到达时唤醒监听接收线程
        serialPort.notifyOnDataAvailable(true);
        //设置当通信中断时唤醒中断线程
        serialPort.notifyOnBreakInterrupt(true);
    }

    /**
     * 关闭串口
     * @param serialPort 串口对象
     */
    public static void closePort(SerialPort serialPort){
        if (serialPort != null){
            serialPort.close();
        }
    }


}
