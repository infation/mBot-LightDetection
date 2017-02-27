package Code;

import java.util.Random;
import java.util.Vector;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

public class Control {
	private Finch myFinch;
	private int loopCount = 0;
	private boolean lastObstacle = false;
	
	public Control(Finch myFinch){
		this.myFinch = myFinch;
	}
	
	//Rotate finch for 1.5 sec 
	public static void randomRotate(Finch myFinch) {
		Random rand = new Random();
		int randSpeed = (rand.nextInt() % 210) + 40;
		myFinch.setWheelVelocities(randSpeed, -randSpeed, 1500);
	}
	
	
	/**************TWO LIGHTS IN A BOX FUNCTIONS START****************
	 ***************************************************************
	 ***************************************************************
	 ***************************************************************
	 */
	public void twoLightsInBox(){
	      LightData initialData = new LightData(myFinch.getLightSensors());
	      
	      lightRotate(initialData);
	      int oneLight = getLight();
	      // headToNextLight();
	      myFinch.setLED(0, 255, 0, 2000);
	      myFinch.setWheelVelocities(-200, -200, 500);
	      shadowRotate(initialData);
	      myFinch.setLED(255, 0, 0, 2000);
	      lightRotate(initialData);
	      myFinch.setLED(0, 255, 0, 2000);
	      int secondLight = getLight();
	      int avg = (oneLight + secondLight) / 2;
	      //goToMiddle(avg);
	      shadowRotate(initialData);
	      myFinch.quit();
	      System.exit(0);
	}
	
	public int getLight() {
		myFinch.stopWheels();
		LightData newData = new LightData(myFinch.getLightSensors());
		return newData.getSum();
	}
	
	public void goToMiddle(Finch myFinch, int avg){
		LightData newData = new LightData(myFinch.getLightSensors());
		while( (avg < newData.getSum() - 5) && (avg > newData.getSum() + 5) ){
			if(avg < newData.getSum() - 5){
				
			}
			else if(avg > newData.getSum() + 5){
				
			}
		}
	}
	
	public boolean lightRotate(LightData initialData){
		initialData.printData();
		for(int i = 0; i<25; i++){
			//
	    	if(lightDecision()) {
	    		myFinch.setWheelVelocities(100, -100, 200); // right rotate
	    	}
	    	else {
	    		myFinch.setWheelVelocities(-100, 100, 200); // left rotate
	    	}

	    	//myFinch.sleep(200);
	    	if(checkForObstacle())
    			initialData.setSum(initialData.getSum() - 20);
	    	LightData newData = new LightData(myFinch.getLightSensors());
	    	newData.printData();
	    	int count = 0;
	    	while(true){
	    		if(initialData.getSum()<newData.getSum()) {
	    			newData.printData();
	    			initialData.setData(newData);
	    			newData = new LightData(myFinch.getLightSensors());
	    			myFinch.setWheelVelocities(250, 250, 200);
	    			if(checkForObstacle())
		    		initialData.setSum(initialData.getSum() - 20);
	    			i-=3;
	    			count++;
	    			}
	    		else {
	    		
	    			break;
	    		}
	    	}
		}
		return false;
	}
	
	public boolean shadowRotate(LightData initialData){
		initialData.printData();
		for(int i = 0; i<25; i++){
			//
	    	if(!lightDecision()) {
	    		myFinch.setWheelVelocities(100, -100, 200); // right rotate
	    	}
	    	else {
	    		myFinch.setWheelVelocities(-100, 100, 200); // left rotate
	    	}

	    	//myFinch.sleep(200);
	    	if(checkForObstacle())
	    		initialData.setSum(initialData.getSum() +20);
	    	LightData newData = new LightData(myFinch.getLightSensors());
	    	newData.printData();
	    	while(true){
	    		if(initialData.getSum()>newData.getSum()) {
	    			newData.printData();
	    			initialData.setData(newData);
	    			newData = new LightData(myFinch.getLightSensors());
	    			myFinch.setWheelVelocities(250, 250, 200);
	    			if(checkForObstacle())
	    				initialData.setSum(initialData.getSum() +20);
	    			i-=3;	    		
	    		}
	    		else {
	    			break;
	    		}
	    	}
		}
		return false;
	}
	
	public boolean checkForObstacle(){
	
		int speed = -170;
		int duration = 400;
		boolean[] obstSensors = new boolean[2];
		obstSensors = myFinch.getObstacleSensors();
		
		//If any of the obstacle sensors detect an object go backwards
		if(obstSensors[0]||obstSensors[1]||(myFinch.isTapped()&&myFinch.isTapped())) {
		    	myFinch.stopWheels();
		    	myFinch.sleep(100);
		    	myFinch.setWheelVelocities(speed, speed, duration);
		    //while(!(myFinch.isTapped()&&myFinch.isTapped()));
		    //myFinch.stopWheels();
		    //myFinch.sleep(500);
			return true;
		}
		/*else{
			if(myFinch.isTapped() && myFinch.isTapped()){
				myFinch.stopWheels();
	    		myFinch.sleep(100);
	    		myFinch.setWheelVelocities(255, 255, 500);
	    		System.out.println("It was tapped ... ");
	    		return true;
			}
		}*/
		return false;
	}
	
	
	public boolean lightDecision() { // deciding if light is stronger from left or right
		int[] lightVals = new int[2];
		lightVals = myFinch.getLightSensors();
		
		if(lightVals[0] < lightVals[1]) {
			return true;
		}
		
		return false;
	}
	
	/*******TWO LIGHTS IN A BOX FUNCTIONS END****************
	 ***************************************************************
	 ***************************************************************
	 ***************************************************************
	 */
	
	/*******KENNEL IN A BOX FUNCTIONS STARTS
	 * @throws InterruptedException ****************
	 ***************************************************************
	 ***************************************************************
	 ***************************************************************
	 */
	
	public void kennelInABox(Calibration values) throws InterruptedException{
		
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
	
	/* A function that rotates the Finch once.
	 * The passed parameter findLight decides where to rotate the Finch:
	 * true = it rotates the Finch left or right depending on which sensor had read MORE light
	 * false = it rotates the Finch left or right depending on which sensor had read LESS light
	 */
	public boolean lightDecision(boolean findLight) { // deciding if light is stronger from left or right
		
		int speed = 70;
		int duration = 300;
		int[] lightVals = new int[2];
		lightVals = myFinch.getLightSensors();
		
		//If looking for light
		if(findLight){
		
			if(lightVals[0] < lightVals[1]) { //If right sensor has more light
				myFinch.setWheelVelocities(speed, -speed, duration);	//Turn right
				return true;
			}
			
			myFinch.setWheelVelocities(-speed, speed, duration); 	//Turn left
			return false;
		}
		//If looking for shade
		else{
			
			if(lightVals[0] < lightVals[1]){	//If left sensor has less light
				myFinch.setWheelVelocities(-speed, speed, duration); //Turn left
				return false;
			}
			
			myFinch.setWheelVelocities(speed, -speed, duration);	//Turn right
			return true;
		}
	}
	
public boolean lightDecisionTest(boolean findLight, int speed) { // deciding if light is stronger from left or right
		
		int speedLeft = speed;
		int speedRight = speed+3;
		int[] lightVals = myFinch.getLightSensors();
		
		//lightVals = myFinch.getLightSensors();
		
		//If looking for light
		if(findLight){
			if(lightVals[0] < lightVals[1]){
				myFinch.setWheelVelocities(speedLeft+(lightVals[1]-lightVals[0])+10, speedRight);	//Turn right
				return true;
			}
			else{
				myFinch.setWheelVelocities(speedLeft, speedRight+(lightVals[0]-lightVals[1])+10); 	//Turn left
				return false;
			}
		}
		//If looking for shade
		else{
			if(lightVals[0] < lightVals[1]){
				myFinch.setWheelVelocities(speedLeft, speedRight+(lightVals[1]-lightVals[0])+10); 	//Turn left
				return false;
			}
			else{
				myFinch.setWheelVelocities(speedLeft+(lightVals[0]-lightVals[1])+10, speedRight);	//Turn right
				return true;
			}
		}
	}
	
	/*A function to rotate the finch in place until it finds the
	* the maximum or minimum light (which depends on the light parameter
	* true = find more light
	* false = find more shade
	*/
	public void findLightOrShade(boolean light) {

		int speed = 70;
		int duration = 300;
		
		boolean whichSensor, lastSensor = false;
		
		//Get initial light
		LightData initialLight = new LightData(myFinch.getLightSensors());
		
		//Rotate to the side with more light 
		whichSensor = lightDecision(light);
		
		//Save the data and compare
		LightData currentLight = new LightData(myFinch.getLightSensors());
		

		if(light){	//Find the MAX light around the finch
		
			while(initialLight.getOneSensor(whichSensor) < currentLight.getOneSensor(whichSensor)) {	
				initialLight.setData(currentLight);
			   	lastSensor = lightDecision(light);
			   	currentLight = new LightData(myFinch.getLightSensors());
			}
		
		}
		else{	//Find the MIN light around the finch
			while(initialLight.getOneSensor(whichSensor) > currentLight.getOneSensor(whichSensor)) {
				initialLight.setData(currentLight);
				lastSensor = lightDecision(light);
				currentLight = new LightData(myFinch.getLightSensors());
			}
		
		}
		
		//Rotate back to where the light is at it's maximum or minimum
		if(lastSensor){
			myFinch.setWheelVelocities(-speed, speed, duration);	//Turn left
		}
		else{
			myFinch.setWheelVelocities(speed, -speed, duration);	//Turn right
		}
	}
	
	//Function to go straight
	public void goStraight(){
		int speed = 200;
		int duration = 300;
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
	
	//Set the initial light, go straight and get the current light readings
	public void setInitGetCurrent(LightData initial, LightData current){

			initial.setData(current);
			goStraight();
			checkForObstacle();
			current = new LightData(myFinch.getLightSensors());
			
	}
	
	public void rotate90(){
		LightData currentData = new LightData(myFinch.getLightSensors());
		if(lastObstacle) {
			myFinch.setWheelVelocities(70, -70, 1600); // goes right
		}
		else {
			myFinch.setWheelVelocities(-70, 70, 1600); // goes left
		}
	}
	
	public int batteryToSpeed(Battery battery){
		int speed = 210;
		return speed-battery.getBatteryLevel();
	}
	
	public void backLeft() {
		myFinch.setWheelVelocities(-60, -170, 1000);
		myFinch.stopWheels();
		myFinch.sleep(100);
	}
	
	public void backRight() {
		myFinch.setWheelVelocities(-170, -60, 1000);
		myFinch.stopWheels();
		myFinch.sleep(100);
	}
	
	public void avoidObstacle(boolean light) {
		boolean[] sensors = myFinch.getObstacleSensors();
		LightData data = new LightData(myFinch.getLightSensors());
		
		if(loopCount>4){
			rotate90();
			loopCount = 0;
			return;
		}
		
		if (sensors[0]) {
			
			if(lastObstacle){
				loopCount++;
			}
			lastObstacle = false;
			
			myFinch.stopWheels();
			myFinch.sleep(100);
			
			if(light){
				
				if(data.getLeft()>data.getRight() + 15) {
					backRight();
					return;
				}
			}
			else{
				if(data.getLeft() < data.getRight() - 15) {
					backRight();
					return;
				}
			}
			backLeft();
			
		}
		else if (sensors[1]) {
			
			if(!lastObstacle){
				loopCount++;
			}
			lastObstacle = true;
			
			myFinch.stopWheels();
			myFinch.sleep(100);
			
			if(light){
				if(data.getLeft() + 15 < data.getRight()) {
					backLeft();
					return;
				}
			}
			else{
				if(data.getLeft() - 15 > data.getRight()) {
					backLeft();
					return;
				}
			}
			backRight();
		}
	}
	
	/*public void map(){
		int width = goStraight();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		int length = goStraight();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		myFinch.setWheelVelocities(150, 150, width);
		rotate90();
		myFinch.setWheelVelocities(150, 150, length);
		rotate90();
		myFinch.setWheelVelocities(150, 150, width);
		rotate90();
		myFinch.setWheelVelocities(150, 150, length);
		rotate90();
		
	}
	*/
	
	//A function to look for and ge to the light source
	public void lookForLight(Battery battery, Calibration values){
		
		boolean lastObstacle = false;
		//int timesToCheck = 10;
		LightData currentData = new LightData(myFinch.getLightSensors());
		LightData maxData = new LightData(myFinch.getLightSensors());
		//DischargeWhileLookingForLight discharge = new DischargeWhileLookingForLight(battery, this.myFinch);
		//discharge.run();
		int count = 0;
		while(values.getMaxValue()>currentData.getSum()+10){
		
			//if(currentData.getSum()<)
			//battery.discharge();
			
			avoidObstacle(true);
			lightDecisionTest(true, batteryToSpeed(battery));
			
			/*if(!avoidObstacle()){
				//findLightOrShade(true);
				lightDecisionTest(true);
			}
			else{
				//rotate90();
				avoidObstacle();
				//findLightOrShade(false);
				count --;
			}*/
			
			/*if(checkForObstacle()){
				//randomRotate(myFinch);
				count++;
			}*/
			//goStraight();
			/*if(checkForObstacle()){
				//randomRotate(myFinch);
				count++;
			}*/
			currentData = new LightData(myFinch.getLightSensors());
	
		}
	}

	//A function to run away from the light and find the darkest place
	public void lookForShade(Thread batteryThread, Calibration values, Battery battery){
	
		//int timesToCheck = 10;
		LightData currentData = new LightData(myFinch.getLightSensors());
		
		//DischargeWhileLookingForLight discharge = new DischargeWhileLookingForLight(battery, this.myFinch);
		//discharge.run();
		int count = 0;
		
		while(values.getMinValue()<currentData.getSum()-10){
			//If the battery is depleted stop shadow rotate
			
			
			if(!batteryThread.isAlive()){
				return;
			}
			
			avoidObstacle(false);
			lightDecisionTest(false,batteryToSpeed(battery));
			/*if(!checkForObstacle()){
				lightDecisionTest(false);
			}
			else{
				//rotate90();
				avoidObstacle();
				count--;
			}*/
		
			/*if(checkForObstacle()){
				//randomRotate(myFinch);
				count++;
			}
			//goStraight();
			/*if(checkForObstacle()){
				count++;
				//randomRotate(myFinch);
			}*/
			
			currentData = new LightData(myFinch.getLightSensors());
		}
	}
	
	public void printLight(){
		//myFinch.showLightSensorGraph();
		myFinch.showAccelerometerGraph();
		while(true){
			LightData initialData = new LightData(myFinch.getLightSensors());
			myFinch.updateAccelerometerGraph(myFinch.getXAcceleration(),0.1, 0.5);
			//myFinch.updateLightSensorGraph(myFinch.getLeftLightSensor(), myFinch.getRightLightSensor());
			initialData.printData();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/*******KENNEL IN A BOX FUNCTIONS END****************
	 ***************************************************************
	 ***************************************************************
	 ***************************************************************
	 */
	
}
