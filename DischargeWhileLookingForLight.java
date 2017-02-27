package Code;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

public class DischargeWhileLookingForLight implements Runnable {
	
	private int batteryLevel;
	private Finch myFinch;
	
	public DischargeWhileLookingForLight(Battery battery, Finch myFinch) {
		batteryLevel = battery.getBatteryLevel();
		this.myFinch = myFinch;
	}
	
	public void run() {
		while (batteryLevel > 0) {
			batteryLevel--;
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		myFinch.stopWheels();
	} 

}
