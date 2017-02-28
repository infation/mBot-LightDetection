package Code;

import java.util.Scanner;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

public class TwoLights extends Control {
	private Finch myFinch;
	
	public TwoLights(Finch myFinch) {
		super(myFinch);
		this.myFinch = myFinch;
		// TODO Auto-generated constructor stub
	}
	
	public void twoLightsInBox(){
		Scanner s = new Scanner(System.in);
		//Calibrate
		Calibration values = new Calibration(myFinch);
		System.out.println("Place the finch under light");
		System.out.print("Press enter to begin! ");
		s.nextLine();
		values.calibrateMax(150);
		System.out.println("Place the finch in the middle of field");
		System.out.print("Press enter to begin! ");
		s.nextLine();
		values.calibrateAverage(150);
		
		
	      //Look for first light
	      lookForLight(values.getMaxValue());
	      myFinch.stopWheels();
	      myFinch.setLED(0, 255, 0, 2000);
	      myFinch.setWheelVelocities(-200, -200, 1500);
	      rotate90();
	      goStraight();
	      
	      //Escape the first light
	      //lookForShade(values.getMinValue());
	      //myFinch.stopWheels();
	      //lookForAverage(values.getAverageValue());
	      //myFinch.stopWheels();
	      //myFinch.setLED(0, 255, 0, 2000);
	      
	      //Look for second light
	      lookForLight(values.getMaxValue());
	      myFinch.stopWheels();
	      myFinch.setLED(0, 255, 0, 2000);
	      myFinch.setWheelVelocities(-200, -200, 1500);
	      rotate90();
	      goStraight();
	      
	      //Find the middle
	      lookForAverage(values.getAverageValue());
	      myFinch.stopWheels();
	      myFinch.setLED(0, 255, 0, 2000);
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
	public void lookForAverage(int value){
	
		LightData currentData = new LightData(myFinch.getLightSensors());
		
		while(value < currentData.getSum() + 20){
			avoidObstacle(false);
			lightDecisionTest(false, 170);
			currentData = new LightData(myFinch.getLightSensors());
		}
	}

	
}