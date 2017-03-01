package Code;

import java.util.Collections;
import java.util.Scanner;
import java.util.Vector;

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
		/*values.calibrateMin();
		values.calibrateMax();*/
		System.out.println("Enter number of lights: ");
		Scanner in = new Scanner(System.in);
		int numLights = in.nextInt();
		
		Vector<Integer> lightValues = new Vector<Integer>();
		
		for(int i = 0; i < numLights; ++i) {
			lightValues.add(values.calibrateLight());
		}
		
		Collections.sort(lightValues);
		System.out.println(lightValues.elementAt(0)+" "+lightValues.elementAt(1));
		
		
		//Start a battery thread that discharges the Finch
		Battery battery = new Battery(myFinch);
		Thread batteryThread = new Thread(battery);	
		batteryThread.start();
		
		//Find light/shade Until the battery is fully depleted
		
		int index = 0;
		
		while(battery.getBatteryLevel() > 0) {
			
			//If the battery is below the threshold, look for light
			if(battery.getBatteryLevel() <= Battery.thresholdLevel){ 
				lookForLightRecursive(battery, lightValues, index);
				myFinch.stopWheels();
				battery.charge(lightValues.get(0));
				index = 0;
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
	
	
	public void lookForLightRecursive(Battery battery, Vector<Integer> vals, int index) {
		
		System.out.println("Recursive function entered!");
		
		DischargeWhileLookingForLight lowBattery = new DischargeWhileLookingForLight(battery);
		Thread lowBatteryThread = new Thread(lowBattery);
		
		if (index == 0) {
			lowBatteryThread.start();
		}
		
		if (index >= vals.size()) return;
		
		LightData currentData = new LightData(myFinch.getLightSensors());
		
		while(vals.elementAt(index) > currentData.getSum() + 10) {
		
			System.out.println("while loop entered");
			
			avoidObstacle(true);
			//lightDecisionTest(true, batteryToSpeed(battery));
			lightDecisionTest(true, 190);

			currentData = new LightData(myFinch.getLightSensors());
			
			if(battery.getBatteryLevel() <= 0) {
				//lowBattery.stopThread();
				return;
			}
		}
		
		myFinch.stopWheels();
		myFinch.setLED(255, 0, 0, 1000);
		goStraightKennel();
		LightData newData = new LightData(myFinch.getLightSensors());
		boolean[] obstacle = myFinch.getObstacleSensors();
		if(newData.getSum() > vals.elementAt(index) - 10) {
			if(obstacle[0]||obstacle[1]){
				lowBattery.stopThread();
				myFinch.setWheelVelocities(-200, -200, 200);
				return;
			}
			index++;
			lookForLightRecursive(battery, vals, index);
		}
			
		lowBattery.stopThread();
		myFinch.setWheelVelocities(-200, -200, 200);
		
	}
	
	public void goStraightKennel(){
		int speed = 240;
		int duration = 500;
		myFinch.setWheelVelocities(speed, speed+3, duration);
	}
}
	
