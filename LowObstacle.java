package Code;

import java.util.Scanner;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

public class LowObstacle extends Control{
	
	private Finch myFinch;
	
	public LowObstacle(Finch myFinch) {
		super(myFinch);
		this.myFinch = myFinch;
		// TODO Auto-generated constructor stub
	}
	
	public void lowObstacle(){
		Scanner s = new Scanner(System.in);
		//Calibrate
		Calibration values = new Calibration(myFinch);
		System.out.println("Place the finch below the light");
		System.out.print("Press enter to begin! ");
		s.nextLine();
		values.calibrateMax(150);
		System.out.print("Press enter to begin! ");
		s.nextLine();
		//Find light/shade Until the battery is fully depleted
				//If the battery is below the threshold, look for light
		lookForLight(values.getMaxValue());
		myFinch.stopWheels();
					
	}
	
public void lookForLight(int maxValue){
		
		//int timesToCheck = 10;
		LightData currentData = new LightData(myFinch.getLightSensors());
		//DischargeWhileLookingForLight discharge = new DischargeWhileLookingForLight(battery, this.myFinch);
		//discharge.run();
		int count = 0;
		while(maxValue>currentData.getSum()+10){
			avoidObstacle(true);
			lightDecisionTest(true, 170);
			currentData = new LightData(myFinch.getLightSensors());
		}
	}
}
