package Code;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

public class TwoLights extends Control {
	private Finch myFinch;
	
	public TwoLights(Finch myFinch) {
		super(myFinch);
		this.myFinch = myFinch;
		// TODO Auto-generated constructor stub
	}
	
	public void twoLightsInBox(){
		
		//Calibrate
		Calibration values = new Calibration(myFinch);
		values.calibrate();
	    
	      //Look for first light
	      lookForLight(values.getMaxValue());
	      myFinch.setLED(0, 255, 0, 2000);
	      
	      //Escape the first light
	      lookForShade(values.getMinValue());
	      
	      //Look for second light
	      lookForLight(values.getMinValue());
	      myFinch.setLED(0, 255, 0, 2000);
	      
	      //Find the middle
	      lookForShade(values.getAverageValue());
	      System.exit(0);
}

	public void lookForLight(int value){

		LightData currentData = new LightData(myFinch.getLightSensors());

		while(value>currentData.getSum()+10){
		
			avoidObstacle(true);
			lightDecisionTest(true, 170);
			currentData = new LightData(myFinch.getLightSensors());
		}
	}

	//A function to run away from the light and find the darkest place
	public void lookForShade(int value){
	
		LightData currentData = new LightData(myFinch.getLightSensors());
		
		while(value<currentData.getSum()-5){
			avoidObstacle(false);
			lightDecisionTest(false, 170);
			currentData = new LightData(myFinch.getLightSensors());
		}
}

}