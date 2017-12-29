public class CpuTask extends Thread{
    private long delay;
    private Arduino arduino;
    private CpuController cpuController;

    public CpuTask(){
    }

    public void setCpuController(CpuController cpuController){
        this.cpuController = cpuController;
    }

    public void setArduino(Arduino arduino) {
        this.arduino = arduino;
    }

    public void setDelay(long delay){
        this.delay = delay;
    }

    @Override
    public void run() {
        while(true){
            try {
                cpuController.run();
                Thread.sleep(delay);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}