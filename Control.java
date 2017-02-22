package Code;

import java.util.Random;
import java.util.Vector;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

public class Control {
	private Finch myFinch;
	
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
		if(obstSensors[0]||obstSensors[1]) {
		    	myFinch.stopWheels();
		    	myFinch.sleep(100);
		    	myFinch.setWheelVelocities(speed, speed, duration);
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
	
	public void kennelInABox() throws InterruptedException{
		
		//Start a battery thread that discharges the Finch
		Battery battery = new Battery(myFinch);
		Thread batteryThread = new Thread(battery);
		batteryThread.start();
		
		//Find light/shade Until the battery is fully depleted
		while(battery.getBatteryLevel()>0){	
			//If the battery is below the threshold, look for light
			if(battery.getBatteryLevel() <= Battery.thresholdLevel){ 
				
				lookForLight(battery);
				battery.charge();
				batteryThread = new Thread(battery);
				batteryThread.start();
			
			}
			else{	//Find shade
				lookForShade(batteryThread);
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
		int duration = 500;
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
	
	/*A function to rotate the finch in place until it finds the
	* the maximum or minimum light (which depends on the light parameter
	* true = find more light
	* false = find more shade
	*/
	public void findLightOrShade(boolean light) {

		boolean whichSensor;
		
		//Get initial light
		LightData initialLight = new LightData(myFinch.getLightSensors());
		
		//Rotate to the side with more light 
		whichSensor = lightDecision(light);
		
		//Save the data and compare
		LightData currentLight = new LightData(myFinch.getLightSensors());
		

		if(light){	//Find the MAX light around the finch
		
			while(initialLight.getOneSensor(whichSensor) < currentLight.getOneSensor(whichSensor)) {	
				initialLight.setData(currentLight);
			   	lightDecision(light);
			   	currentLight = new LightData(myFinch.getLightSensors());
			}
		
		}
		else{	//Find the MIN light around the finch
			while(initialLight.getOneSensor(whichSensor) > currentLight.getOneSensor(whichSensor)) {
				initialLight.setData(currentLight);
				lightDecision(light);
				currentLight = new LightData(myFinch.getLightSensors());
			}
		
		}
		
		//Rotate back to where the light is at it's maximum or minimum
		lightDecision(light);
	}

	//Function to go straight
	public void goStraight(){
		
		int speed = 140;
		int duration = 500;
		myFinch.setWheelVelocities(speed, speed, duration);
	
	}
	
	//Set the initial light, go straight and get the current light readings
	public void setInitGetCurrent(LightData initial, LightData current){

			initial.setData(current);
			goStraight();
			checkForObstacle();
			current = new LightData(myFinch.getLightSensors());
			
	}
	
	//A function to look for and ge to the light source
	public void lookForLight(Battery battery){
		
		int timesToCheck = 10;
		LightData initialData = new LightData(myFinch.getLightSensors());
		
		for(int i = 0; i < timesToCheck; i++){
		
			battery.discharge();
			
			findLightOrShade(true);
			
			//Check for obstacle and manually decrease the maximum data
			if(checkForObstacle()) {
				//avoidObstacle();
				//LightData newData = new LightData(myFinch.getLightSensors()); 
    			//initialData.setSum(initialData.getSum() - 5);
			}
			
			LightData newData = new LightData(myFinch.getLightSensors()); 
	    	
			//Go straight ahead until reaching the maximum light for the run
			while(initialData.getSum()<newData.getSum()){
	    			
	    			//Set the maximum light data move straight and get the next light data
	    			//setInitGetCurrent(initialData, newData);
	    			
				initialData.setData(newData);
				goStraight();
				checkForObstacle();
				newData = new LightData(myFinch.getLightSensors());
				
	    			//If obstacle after going straight, reset the light readings
	    			/*if(checkForObstacle() && (initialData.getSum() > newData.getSum())){
		    			//initialData.setSum(initialData.getSum() - 25);
		    			//break;
	    				return;
	    			}*/
	    			
	    			//Reset the for loop
		    		i=5;
	    	}
		}
	}

	//A function to run away from the light and find the darkest place
	public void lookForShade(Thread batteryThread){
	
		int timesToCheck = 15;
		LightData initialData = new LightData(myFinch.getLightSensors());
		
		for(int i = 0; i < timesToCheck; i++){
			
			//If the battery is depleted stop shadow rotate
			if(!batteryThread.isAlive()){
				return;
			}
			
			//Rotate where there is most shade
			findLightOrShade(false);
			
			//Check for obstacle and manually increase the minimum data
	    	if(checkForObstacle())
    			initialData.setSum(initialData.getSum() + 5);
	    	
	    	LightData newData = new LightData(myFinch.getLightSensors());
	    	
			//Go straight ahead until reaching the minimum light for the run
	    	while(true){
	    		//If the battery is not depleted
	    		if(batteryThread.isAlive()){
	    			if(initialData.getSum()>newData.getSum()) {
		    		
	    				initialData.printData();
	    				//Set the minimum light data move straight and get the next light data
	    				setInitGetCurrent(initialData, newData);
	    				
	    				newData.printData();
		    			//Check for obstacle and reset the minimum light data
		    			if(checkForObstacle()){
			    			initialData.setSum(initialData.getSum() + 5);		  
			    			break;
		    			}
		    			
		    			//Reset the "for" loop
		    			i=5;
		    			
		    		}
		    		else {
		    			//Check for obstacle and reset the light data
		    			/*if(checkForObstacle())
		    				initialData.setSum(initialData.getSum() + 50);	*/
		    			break;
		    		}
	    			
	    		}
	    		//If the battery is depleted stop immediately
	    		else{
	    			return;
	    		}
	    	}
		}
	}
	
	/*******KENNEL IN A BOX FUNCTIONS END****************
	 ***************************************************************
	 ***************************************************************
	 ***************************************************************
	 */
	
}
