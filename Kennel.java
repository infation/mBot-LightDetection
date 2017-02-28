package Code;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

public class Kennel extends Control {

	private Finch myFinch;
	
	public Kennel(Finch myFinch) {
		super(myFinch);
		this.myFinch = myFinch;
		// TODO Auto-generated constructor stub
	}
	
	
	public void kennelInABox() throws InterruptedException {
		
		Calibration values = new Calibration(myFinch);
		values.calibrate();
		//Start a battery thread that discharges the Finch
		Battery battery = new Battery(myFinch);
		Thread batteryThread = new Thread(battery);
		
		batteryThread.start();
		
		//Find light/shade Until the battery is fully depleted
		
		while(battery.getBatteryLevel() > 0) {
			
			//If the battery is below the threshold, look for light
			if(battery.getBatteryLevel() <= Battery.thresholdLevel){ 
				lookForLight(battery, values);
				myFinch.stopWheels();
				battery.charge(values.getMaxValue());
				batteryThread = new Thread(battery);
				batteryThread.start();
			}
			else{	//Find shade
				lookForShade(batteryThread, values, battery);
				myFinch.stopWheels();
			}
		}
		
		// behavior for when battery is down
		int frequency = 500;
		
		for(int i = 0; i < 10; ++i) {
			
			myFinch.setWheelVelocities(170, -80, 300);
			myFinch.buzz(frequency, 300);
			myFinch.setLED(170, 0, 0, 300);
			myFinch.setWheelVelocities(-80, 170, 300);
			frequency -= 40;
		}
		
		myFinch.stopWheels();
	}
}
	
