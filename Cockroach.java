package Code;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;
	
public class Cockroach extends Control{
	private Finch myFinch;
	private int shade;
	private int lightOff;
	private int lightOn;
	private int loopCount = 0;
	private boolean lastObstacle = false;
	private boolean isInLoop = false;
	private int oneSensorLoop = 0;
	
	
	public Cockroach(Finch myFinch) {
		super(myFinch);
		this.myFinch = myFinch;
		Calibration values = new Calibration(myFinch);
		values.calibrateMin();
		values.calibrateAverage();
		values.calibrateMax();
		shade = values.getMinValue();
		lightOff = values.getAverageValue();
		lightOn = values.getMaxValue();
		// TODO Auto-generated constructor stub
	}
	
	public void cockroach(){
		
		
		while(true){
			LightData currentData = new LightData(myFinch.getLightSensors());
			//LightData currentData = new LightData(myFinch.getLightSensors());
			
			//If the light has been turned off roam slowly around the room
			if(lightOff < currentData.getSum()+20){
				lookForLight();
			}
			
			currentData = new LightData(myFinch.getLightSensors());			

			//Quickly find shade
			if(shade < currentData.getSum() - 10 || shade > currentData.getSum()-10 ){
				myFinch.stopWheels();
				lookForShade();
				//myFinch.sleep(200);
				//myFinch.setLED(0, 255, 0);
			}
			
			currentData = new LightData(myFinch.getLightSensors());
			
			//If the shade has been found stay there until the external light has been turned off or there is
			//change in the environment and the shade is not the safe spot anymore
			while(shade > currentData.getSum()-10 && lightOff < currentData.getSum()-20){
				currentData = new LightData(myFinch.getLightSensors());
				myFinch.setLED(0, 0, 255);
			}
			
	
	
				//If the external light is turned off and the finch is safe to roam
					 //If the shade is not the safe spot anymore since there is light coming in
		}
		
	}
	
	public void backLeft() {
		myFinch.setWheelVelocities(-60, -170, 600);
		myFinch.stopWheels();
		myFinch.sleep(100);
	}
	
	public void backRight() {
		myFinch.setWheelVelocities(-170, -60, 600);
		myFinch.stopWheels();
		myFinch.sleep(100);
	}	

	public boolean isShade(){
		return false;
	}
	
	public boolean isLightOn(){
		return false;
	}
	
	public boolean isLightOff(){
		return false;
	}
	
	public void lookForLight(){

		LightData currentData = new LightData(myFinch.getLightSensors());

		while(lightOn>currentData.getSum()+50){
			avoidObstacle(true);
			lightDecisionTest(true, 70);
			currentData = new LightData(myFinch.getLightSensors());
			myFinch.setLED(0, 255, 0);
		}
		
	}

	//A function to run away from the light and find the darkest place
	public void lookForShade(){
		boolean switchLook = false;
		LightData currentData = new LightData(myFinch.getLightSensors());
		Long timeBefore = System.currentTimeMillis();
		Long timeAfter = System.currentTimeMillis();
		while(lightOff < currentData.getSum()-20){
			
			/*if(currentData.getSum() < maxValue - 20 ) {
				return;
			}*/
			/*if(((currentData.getLeft()>currentData.getRight()+30)&&(currentData.getRight()*2-30<shade))
				||((currentData.getLeft()+30<currentData.getRight())&&(currentData.getLeft()*2-30<shade))&&(lightOff < currentData.getSum()-20)&&lightOn<currentData.getSum()+20){
				myFinch.stopWheels();
				myFinch.sleep(1000);
				//findLightOrShade(false);
				for(int i = 0; i < 3 ; i++){
					lightDecision(false);
				}
				myFinch.setWheelVelocities(200, 200, 300);
				//myFinch.setLED(0, 0, 255, 3000);
				return;
			}*/
			
			

			//Switch gears every 15 seconds
			timeAfter = System.currentTimeMillis();
			if(timeAfter - timeBefore > 15000){
				switchLook = !switchLook;
				timeBefore = System.currentTimeMillis();
				timeAfter = System.currentTimeMillis();
			}
			if(avoidObstacle(switchLook)){
				break;
			}
			//currentData = new LightData(myFinch.getLightSensors());


			lightDecisionTest(switchLook, 170);
			currentData = new LightData(myFinch.getLightSensors());
			myFinch.setLED(255, 0, 0);
			
		}
	}
	
	public void goBack(){
		int speed = -200;
		int duration = 400;
		/*Long timeA = System.currentTimeMillis();
		while(true){
			myFinch.setWheelVelocities(speed, speed+3);
			if(checkForObstacle()){ 
				break;
			}
		}
		return (int) (System.currentTimeMillis()-timeA-500);*/
		myFinch.setWheelVelocities(speed, speed+3, duration);
	}
	
	public void goStraight(){
		int speed = 200;
		int duration = 400;
		/*Long timeA = System.currentTimeMillis();
		while(true){
			myFinch.setWheelVelocities(speed, speed+3);
			if(checkForObstacle()){ 
				break;
			}
		}
		return (int) (System.currentTimeMillis()-timeA-500);*/
		myFinch.setWheelVelocities(speed, speed+3, duration);
	}
	
	public boolean avoidObstacle(boolean light) {
		boolean[] sensors = myFinch.getObstacleSensors();
		LightData data = new LightData(myFinch.getLightSensors());
		
		//A loop where only one sensor detects an obstacle
		if(oneSensorLoop > 3){
			//goStraight();
			oneSensorLoop = 0;
			return false;
		}
	
		//Loop count is for right and left sensors alternating
		if(loopCount>3) {
			randomRotate();
			//goStraight();
			loopCount = 0;
			return false;
			//return;
		}
		
		if(sensors[0]) {
			goStraight();
			data = new LightData(myFinch.getLightSensors());
			if(shade > data.getSum() - 10 && lightOff < data.getSum()-20){
				myFinch.stopWheels();
				myFinch.sleep(100);
				
				for(int i = 0; i < 3 ; i++){
					lightDecision(false);
				}
				//myFinch.setWheelVelocities(200, 200, 300);
				//myFinch.setLED(0,0,255,3000);
				return true;
			}
			else{
				goBack();
			}
			
			if(lastObstacle){
				loopCount++;
				oneSensorLoop = 0;
			}
			else{
				//loopCount--;
				oneSensorLoop++;
			}
			
			
			lastObstacle = false;
			
			myFinch.stopWheels();
			myFinch.sleep(100);
			
			if(light){
				
				if((data.getLeft()>data.getRight() + 15)) {
					backRight();
					return false;
				}
			}
			else{
				if((data.getLeft() < data.getRight() - 15)) {
					backRight();
					return false;
				}
			}
			backLeft();
			return false;
			
		}
		else if (sensors[1]) {
			goStraight();
			data = new LightData(myFinch.getLightSensors());
			if(shade > data.getSum() - 10 && lightOff < data.getSum()-20){
				
				myFinch.stopWheels();
				myFinch.sleep(100);
				//findLightOrShade(false);
				for(int i = 0; i < 3 ; i++){
					lightDecision(false);
				}
				//myFinch.setWheelVelocities(200, 200, 300);
				//myFinch.setLED(0,0,255,3000);
				return true;
			}
			else{
				goBack();
			}
			
			if(!lastObstacle){
				loopCount++;
				oneSensorLoop = 0;
			}
			else{
				oneSensorLoop++;
			}
			
			lastObstacle = true;
			
			myFinch.stopWheels();
			myFinch.sleep(100);
			
			if(light){
				if((data.getLeft() + 15 < data.getRight())) {
					backLeft();
					return false;
				}
			}
			else{
				if((data.getLeft() - 15 > data.getRight())) {
					backLeft();
					return false;
				}
			}
			backRight();
			return false;
		}
		return false;
	}
}
