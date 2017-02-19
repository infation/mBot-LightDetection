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
		LightData initialData = new LightData(myFinch.getLightSensors());
		
		System.out.println("Initial light when charging  " + initialData.getSum());
		
		while (this.batteryLevel < 100) {
			
			LightData currentData = new LightData(myFinch.getLightSensors());
			
			if(currentData.getSum() < (initialData.getSum() / 1.5)) {
				System.out.println("Light stopped at  " + currentData.getSum());
				return;
			}
			
			
			myFinch.setLED(255, 0, 0, 200);
			myFinch.buzz(500, 300);
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
			
		while(batteryLevel > 30) {
			discharge();
				
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
