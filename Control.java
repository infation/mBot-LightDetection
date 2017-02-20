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
		boolean[] obstSensors = new boolean[2];
		obstSensors = myFinch.getObstacleSensors();
		if(obstSensors[0]||obstSensors[1]) {
		    	myFinch.stopWheels();
		    	myFinch.sleep(100);
		    	myFinch.setWheelVelocities(-255, -255, 400);
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
		Battery battery = new Battery(myFinch);
		Thread batteryThread = new Thread(battery);
		
		batteryThread.start();
		//batteryThread.run();
		
		while(battery.getBatteryLevel()>0){
			if(battery.getBatteryLevel() <= 30){
				LightData initialData = new LightData(myFinch.getLightSensors());
				lightRotate(initialData, battery);
				battery.charge();
				batteryThread = new Thread(battery);
				batteryThread.start();
			}
			else{
				LightData initialData = new LightData(myFinch.getLightSensors());
				shadowRotate(initialData, batteryThread);
			}
		}
		
	}
	
	public void findLight() {
		LightData maxLight = new LightData(myFinch.getLightSensors());
		boolean sensor = lightDecision();
		if(sensor) {
			myFinch.setWheelVelocities(70, -70, 500); // right sensors
		}
		else{
			myFinch.setWheelVelocities(-70, 70, 500); // left sensor
		}
		LightData currentLight = new LightData(myFinch.getLightSensors());
		boolean left = true;
		while(maxLight.getOneSensor(sensor) < currentLight.getOneSensor(sensor)) {
			maxLight.setData(currentLight);
			if(sensor) {
		   		myFinch.setWheelVelocities(70, -70, 500); // right rotate
		   		left = true;
		   	}
		   	else {
		   		myFinch.setWheelVelocities(-70, 70, 500); // left rotate
		   		left=false;
	    	}
			currentLight = new LightData(myFinch.getLightSensors());
		}
		
		if(left){
			myFinch.setWheelVelocities(-70, 70, 100); 
		}
		else{
			myFinch.setWheelVelocities(70, -70, 100); 
		}
	}
	
	public void findDark() {
		LightData minLight = new LightData(myFinch.getLightSensors());
		boolean sensor = !lightDecision();
		if(sensor) {
			myFinch.setWheelVelocities(70, -70, 500); // right sensors
		}
		else{
			myFinch.setWheelVelocities(-70, 70, 500); // left sensor
		}
		boolean left = true;
		LightData currentLight = new LightData(myFinch.getLightSensors());
		
		while(minLight.getOneSensor(sensor) > currentLight.getOneSensor(sensor)) {
			minLight.setData(currentLight);
			if(sensor) {
		   		myFinch.setWheelVelocities(70, -70, 500); // right rotate
		   		left = true;
		   	}
		   	else {
		   		myFinch.setWheelVelocities(-70, 70, 500); // left rotate
		   		left=false;
	    	}
			currentLight = new LightData(myFinch.getLightSensors());
		}
		
		if(left){
			myFinch.setWheelVelocities(-70, 70, 100); 
		}
		else{
			myFinch.setWheelVelocities(70, -70, 100); 
		}
	}
	
	public boolean lightRotate(LightData initialData, Battery battery){
		//initialData.printData();
		int speed = 130;
		for(int i = 0; i<15; i++){
			battery.discharge();
			findLight();

	    	//myFinch.sleep(200);
			if(checkForObstacle())
    			initialData.setSum(initialData.getSum() - 5);
	    	LightData newData = new LightData(myFinch.getLightSensors());
	    	//newData.printData();
	    	int count = 0; 
	    	while(true){
	    		if(initialData.getSum()<newData.getSum()) {
	    			//newData.printData();
	    			initialData.setData(newData);
	    			myFinch.setWheelVelocities(speed, speed, 500);
	    			newData = new LightData(myFinch.getLightSensors());
	    			/*if(speed>=90){
	    				speed-= 10;
	    			}*/
	    			//checkForObstacle();
	    			if(checkForObstacle()){
		    			initialData.setSum(initialData.getSum() - 50);
		    			break;
	    			}
		    		i=5;
	    			count++;
	    			//battery.setBatteryLevel(battery.getBatteryLevel()+1);
	    		}
	    		else {
	    			if (checkForObstacle())
	    				initialData.setSum(initialData.getSum() - 50);
	    			
	    			break;
	    		}
	    	}
		}
		return false;
	}
	
	public boolean shadowRotate(LightData initialData,  Thread batteryThread){
		int speed = 130;
		//initialData.printData();
		for(int i = 0; i<15; i++){
			//battery.discharge();
			if(!batteryThread.isAlive()){
				break;
			}
			findDark();
	    	//myFinch.sleep(200);
	    	if(checkForObstacle())
    			initialData.setSum(initialData.getSum() + 5);
	    	LightData newData = new LightData(myFinch.getLightSensors());
	    	//newData.printData();
	    	while(true){
	    		if(batteryThread.isAlive()){
		    		if(initialData.getSum()>newData.getSum()) {
		    			//newData.printData();
		    			initialData.setData(newData);
		    			myFinch.setWheelVelocities(speed, speed, 500);
		    			newData = new LightData(myFinch.getLightSensors());
		    			if(checkForObstacle())
			    			initialData.setSum(initialData.getSum() + 5);		  
		    			i=5;
		    		}
		    		else {
		    			break;
		    		}
		    		//battery.discharge();
	    		}
	    		else{
	    			break;
	    		}
	    	}
		}
		return false;
	}
	
	/*******KENNEL IN A BOX FUNCTIONS END****************
	 ***************************************************************
	 ***************************************************************
	 ***************************************************************
	 */
	
}
