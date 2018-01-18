import org.ardulink.core.serial.rxtx.SerialLinkConfig;

import java.util.List;
import java.util.Scanner;
import java.util.Timer;

public class MainClass {
    static Timer timer = new Timer();

    public static void main(String[] args) {
        SerialLinkConfig configurer = new SerialLinkConfig();
        CpuController cpuController = new CpuController();
        CpuTask cpuTask = new CpuTask();
        Arduino arduino = new Arduino();
        List<String> ports;
        String command = "";

        boolean isSet = false;
        while(true) {
            while (!isSet) {
                try {
                    ports = configurer.listPorts();
                    if (ports.size() < 1) System.out.println("No device connected");
                    else {
                        System.out.print("Choose a port " + ports + ":");
                        String port = new Scanner(System.in).nextLine();

                        System.out.print("Choose a refresh rate (in millis): ");
                        long millis = new Scanner(System.in).nextLong();

                        if (!ports.contains(port)) System.out.println("Wrong port.");
                        else {
                            arduino.connect(port, 9600);

                            while (!arduino.isConnected()) {
                                /** waits arduino to connect **/
                            }

                            cpuController.setArduino(arduino);
                            cpuTask.setDelay(millis);
                            cpuTask.setArduino(arduino);
                            cpuTask.setCpuController(cpuController);
                            cpuTask.start();
                            System.out.println("Thread started.");
                            isSet = true;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            System.out.println("exit: to close, delay: change delay, port: change port");
            command = new Scanner(System.in).next();
            switch(command){
                case "exit":
                    arduino.write("0");
                    System.exit(1);
                case "delay":
                    long delay = new Scanner(System.in).nextLong();
                    cpuTask.setDelay(delay);
                    break;
                case "port":
                    break;
            }
        }
    }
}