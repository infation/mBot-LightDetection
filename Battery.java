package Code;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

public class Battery {
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
			myFinch.setLED(255, 0, 0, 300);
			myFinch.sleep(200);
			batteryLevel++;
			System.out.println("Charging" +batteryLevel+"%");
			
		}
	}
	
	
	public void discharge() {
		batteryLevel--;
		System.out.println("Discharging" + batteryLevel+"%");
	}
	
}
