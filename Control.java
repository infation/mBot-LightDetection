package Code;

import java.util.Random;

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
	      double temperature;
	      while(true){
	    	  checkForObstacle();
	    	 if(!lightRotate(myFinch, initialData)){
	    		 int oneLight = getLight();
	    		 headToNextLight();
	    		 int secondLight = getLight();
	    		 int avg = (oneLight + secondLight) / 2;
	    		 //goToMiddle(avg);
	    		 break;
	    	 }
	      }
	      myFinch.quit();
	      System.exit(0);
	}
	
	public int getLight() {
		myFinch.stopWheels();
		LightData newData = new LightData(myFinch.getLightSensors());
		return newData.getSum();
	}
	
	public void headToNextLight() {
		myFinch.stopWheels();
		myFinch.sleep(100);
		
		boolean[] sensorVals = new boolean[2];
		
		sensorVals = myFinch.getObstacleSensors();
		myFinch.setWheelVelocities(-150, -150, 500);
		
		if(sensorVals[0]) {
			myFinch.setWheelVelocities(-100, 0 , 1500);
		}
		else {
			myFinch.setWheelVelocities(0, -100, 1500);
		}
		
		myFinch.setWheelVelocities(200, 200,2500);
		LightData currentLight = new LightData(getLight());

		while(true){
			//checkForObstacle();
			if(!lightRotate(myFinch, currentLight)){
				break;
			}
		}
	}
	
	public boolean lightRotate(Finch myFinch, LightData initialData){
		for(int i = 0; i<5; i++){
	    	
			checkForObstacle();
	    	if(lightDecision()) {
	    		myFinch.setWheelVelocities(100,-100, 500); // right rotate
	    	}
	    	else {
	    		myFinch.setWheelVelocities(-100,100, 500); // left rotate
	    	}
			
			LightData newData = new LightData(myFinch.getLightSensors());
			newData.printData();
			if(initialData.getSum()<newData.getSum()){
				initialData.setData(newData);
				myFinch.setWheelVelocities(200, 200);
				//myFinch.sleep(500);
				//checkForObstacle();
				return true;
			}
		}
		return false;
	}
	
	public void checkForObstacle(){
		boolean[] obstSensors = new boolean[2];
		obstSensors = myFinch.getObstacleSensors();
		if(obstSensors[0]||obstSensors[1]||(myFinch.isTapped() && myFinch.isTapped())){
	    		myFinch.stopWheels();
	    		myFinch.sleep(100);
	    		myFinch.setWheelVelocities(-255, -255, 500);
	    	
		}
	}
	
	
	public boolean lightDecision() {
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
	
	
}
