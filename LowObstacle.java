package Code;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

public class LowObstacle extends Control{
	
	private Finch myFinch;
	
	public LowObstacle(Finch myFinch) {
		super(myFinch);
		this.myFinch = myFinch;
		// TODO Auto-generated constructor stub
	}
	
	public void lowObstacle(){
		int numCalibrations = 150;
		Calibration values = new Calibration(myFinch);
		values.calibrateMax(numCalibrations);
		//Find light/shade Until the battery is fully depleted
				//If the battery is below the threshold, look for light
		lookForLight(values);
		myFinch.stopWheels();
					
	}
	
public void lookForLight(Calibration values){
		
		//int timesToCheck = 10;
		LightData currentData = new LightData(myFinch.getLightSensors());
		//DischargeWhileLookingForLight discharge = new DischargeWhileLookingForLight(battery, this.myFinch);
		//discharge.run();
		int count = 0;
		while(values.getMaxValue()>currentData.getSum()+10){
			avoidObstacle(true);
			lightDecisionTest(true, 170);
			currentData = new LightData(myFinch.getLightSensors());
		}
	}
}
