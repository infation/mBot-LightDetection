package Code;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

public class Kennel extends Control {

	private Finch myFinch;
	
	public Kennel(Finch myFinch) {
		super(myFinch);
		this.myFinch = myFinch;
		// TODO Auto-generated constructor stub
	}
	
public void kennelInABox() throws InterruptedException{
		
		//Calibrate thresholds
		Calibration values = new Calibration(myFinch);
		values.calibrate();
		
		//Start a battery thread that discharges the Finch
		Battery battery = new Battery(myFinch);
		Thread batteryThread = new Thread(battery);
		batteryThread.start();
		
		//Find light/shade Until the battery is fully depleted
		while(battery.getBatteryLevel()>0){	
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
	}
}
