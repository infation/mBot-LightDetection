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
		//Calibrate
		Calibration values = new Calibration(myFinch);
		values.calibrateMax();
		values.calibrateMin();
		while(true){
			lookForLight(values.getMaxValue());
			myFinch.stopWheels();
			myFinch.setLED(0, 255 , 0, 2000);
			lookForShade(values.getMinValue());
		}
	}
	
public void lookForLight(int maxValue){
		
		LightData currentData = new LightData(myFinch.getLightSensors());

		while(maxValue>currentData.getSum()+10){
			avoidObstacle(true);
			lightDecisionTest(true, 170);
			currentData = new LightData(myFinch.getLightSensors());
		}

}
public void lookForShade(int value){

	LightData currentData = new LightData(myFinch.getLightSensors());
	Long firstTime = System.currentTimeMillis();
	Long secondTime = System.currentTimeMillis();
	while(value<currentData.getSum()-10||secondTime-firstTime<20000){
		//If the battery is depleted stop shadow rotate
		avoidObstacle(false);
		//lightDecisionTest(false,batteryToSpeed(battery));
		lightDecisionTest(false, 170);
		currentData = new LightData(myFinch.getLightSensors());
		secondTime = System.currentTimeMillis();
	}
}

}


