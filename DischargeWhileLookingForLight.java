package Code;

public class DischargeWhileLookingForLight implements Runnable {
	
	private Battery battery;
	private volatile boolean exit = false;
	
	public DischargeWhileLookingForLight(Battery battery) {
		this.battery = battery;
	}
	
	public void run() {
		while (battery.getBatteryLevel() > 0) {
			
			if(exit == true) {
				return;
			}
			
			battery.discharge();
			
			//System.out.println("Discharging" + batteryLevel+"%");
			
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}	
	
	public void stopThread() {
		exit = true;
	}

}
