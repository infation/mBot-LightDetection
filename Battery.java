package Code;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

public class Battery implements Runnable {
	
	public static int thresholdLevel = 30;
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
	
	
	public void charge(int maxLight) {
		
		while (this.batteryLevel < 100) {
			
			LightData currentData = new LightData(myFinch.getLightSensors());
			
			if(currentData.getSum() + 20 < maxLight) {
				return;
			}
			
			
			myFinch.setLED(255, 0, 0, 400);
			myFinch.buzz(500, 250);
			myFinch.sleep(200);
			batteryLevel+=4;
			System.out.println("Charging" +batteryLevel+"%");
			
		}
	}
	
	
	public void discharge() {
		batteryLevel--;
		System.out.println("Discharging" + batteryLevel+"%");
	}

	public void run() {
			
		while(batteryLevel > Battery.thresholdLevel) {
			discharge();
				
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
