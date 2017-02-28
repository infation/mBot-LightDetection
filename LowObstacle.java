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
		lookForLight(values.getMaxValue());
		myFinch.stopWheels();
		myFinch.setLED(0, 255 , 0, 2000);
	}
	
public void lookForLight(int maxValue){
		
		LightData currentData = new LightData(myFinch.getLightSensors());

		while(maxValue>currentData.getSum()+10){
			avoidObstacle(true);
			lightDecisionTest(true, 170);
			currentData = new LightData(myFinch.getLightSensors());
		}
	}
}
