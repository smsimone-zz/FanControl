import gnu.io.*;

import java.io.*;

public class Arduino
{
    private OutputStream out;
    private BufferedReader in;
    private SerialPort serialPort;

    private String isReady = "";
    public boolean ready = false;

    Arduino()
    {
        super();
    }

    void connect ( String portName, int baudRate ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);

            if ( commPort instanceof SerialPort)
            {
                serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(baudRate,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                in = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
                out = serialPort.getOutputStream();
                serialPort.addEventListener(new SerialPortEventListener() {
                    @Override
                    public void serialEvent(SerialPortEvent serialPortEvent) {
                        if(serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE){
                            try{
                                isReady = in.readLine();
                                System.out.println(isReady);
                                ready = isReady.equals("Arduino ready");
                            }catch(IOException e){
                                System.out.println("ioException: " + e.getMessage());
                            }
                        }
                    }
                });
                serialPort.notifyOnDataAvailable(true);
            }
        }
    }

    public boolean isConnected(){
        return serialPort!=null;
    }

    public synchronized void close(){
        if(serialPort!=null){
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    void write(String data){
        try{
            this.out.write(data.getBytes());
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}