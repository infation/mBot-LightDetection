package Code;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

public class Battery implements Runnable {
	private int batteryLevel;
	private Finch myFinch;
	
	public Battery(Finch myFinch){
		batteryLevel = 100;
		this.myFinch = myFinch;
	}

	public int getBatteryLevel() {
		return batteryLevel;
	}

	public void setBatteryLevel(int batteryLevel) {
		this.batteryLevel = batteryLevel;
	}
	
	
	public void charge() {
		while (this.batteryLevel < 100) {
			myFinch.setLED(255, 0, 0, 100);
			//myFinch.sleep(200);
			batteryLevel++;
			System.out.println("Charging" +batteryLevel+"%");
			
		}
	}
	
	
	public void discharge() {
		batteryLevel--;
		System.out.println("Discharging" + batteryLevel+"%");
	}

	public void run() {
		// TODO Auto-generated method stub
			
		while(batteryLevel > 20) {
			discharge();
				
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
