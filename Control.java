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
	    	checkForObstacle();
	    	LightData newData = new LightData(myFinch.getLightSensors());
	    	newData.printData();
	    	while(true){
	    		if(initialData.getSum()<newData.getSum()) {
	    			newData.printData();
	    			initialData.setData(newData);
	    			newData = new LightData(myFinch.getLightSensors());
	    			myFinch.setWheelVelocities(250, 250, 200);
	    			checkForObstacle();
	    			i=0;
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
	    	checkForObstacle();
	    	LightData newData = new LightData(myFinch.getLightSensors());
	    	newData.printData();
	    	while(true){
	    		if(initialData.getSum()>newData.getSum()) {
	    			newData.printData();
	    			initialData.setData(newData);
	    			newData = new LightData(myFinch.getLightSensors());
	    			myFinch.setWheelVelocities(250, 250, 200);
	    			checkForObstacle();
	    			i=0;
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
		    	myFinch.setWheelVelocities(-255, -255, 200);
		    	return true;
		}
		else{
			if(myFinch.isTapped() && myFinch.isTapped()){
				myFinch.stopWheels();
	    		myFinch.sleep(100);
	    		myFinch.setWheelVelocities(255, 255, 500);
	    		return true;
			}
		}
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
			if(battery.getBatteryLevel() <= 20){
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
	
	public boolean lightRotate(LightData initialData, Battery battery){
		//initialData.printData();
		for(int i = 0; i<25; i++){
			battery.discharge();
			
			if(lightDecision()) {
	    		myFinch.setWheelVelocities(100, -100, 300); // right rotate
	    	}
	    	else {
	    		myFinch.setWheelVelocities(-100, 100, 300); // left rotate
	    	}

	    	//myFinch.sleep(200);
	    	checkForObstacle();
	    	LightData newData = new LightData(myFinch.getLightSensors());
	    	//newData.printData();
	    	while(true){
	    		if(initialData.getSum()<newData.getSum()) {
	    			//newData.printData();
	    			initialData.setData(newData);
	    			newData = new LightData(myFinch.getLightSensors());
	    			myFinch.setWheelVelocities(250, 250, 200);
	    			checkForObstacle();
	    			i=0;
	    			battery.setBatteryLevel(battery.getBatteryLevel()+1);
	    		}
	    		else {
	    			break;
	    		}
	    	}
		}
		return false;
	}
	
	public boolean shadowRotate(LightData initialData,  Thread batteryThread){
		//initialData.printData();
		for(int i = 0; i<25; i++){
			//battery.discharge();
			if(!batteryThread.isAlive()){
				break;
			}
	    	
	    	if(!lightDecision()) {
	    		myFinch.setWheelVelocities(100, -100, 200); // right rotate
	    	}
	    	else {
	    		myFinch.setWheelVelocities(-100, 100, 200); // left rotate
	    	}

	    	//myFinch.sleep(200);
	    	checkForObstacle();
	    	LightData newData = new LightData(myFinch.getLightSensors());
	    	//newData.printData();
	    	while(true){
	    		if(batteryThread.isAlive()){
		    		if(initialData.getSum()>newData.getSum()) {
		    			//newData.printData();
		    			initialData.setData(newData);
		    			newData = new LightData(myFinch.getLightSensors());
		    			myFinch.setWheelVelocities(250, 250, 200);
		    			checkForObstacle();
		    			i=0;
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
