import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import java.util.TimerTask;

public class CpuController extends TimerTask {
    private Sigar sigar = new Sigar();
    private Arduino arduino;

    public CpuController(){
    }

    public void setArduino(Arduino arduino){
        this.arduino = arduino;
    }
    public Arduino getArduino(){
        return this.arduino;
    }

    public void run(){
        try {
            CpuPerc cpu = sigar.getCpuPerc();

            Double val = cpu.getCombined()*100;
            String value = val.intValue()+"\n";
            arduino.write(value);
        }catch (SigarException e){
            e.printStackTrace();
        }
    }
}