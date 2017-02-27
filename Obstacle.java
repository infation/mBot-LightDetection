package Code;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

public class Obstacle implements Runnable {
	
	private boolean sensors[];
	private Finch myFinch;
	
	public Obstacle(Finch myFinch){
		sensors = new boolean[2];
		this.myFinch = myFinch;
	}
	
	
	public void checkForObstacle(){
	
		int speed = -170;
		int duration = 400;
		sensors = myFinch.getObstacleSensors();
		
		//If any of the obstacle sensors detect an object go backwards
		if(sensors[0]||sensors[1]) {
		    	myFinch.stopWheels();
		    	myFinch.sleep(100);
		    	myFinch.setWheelVelocities(speed, speed, duration);
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
	}
	
	@Override
	public void run() {
		while(true){
			checkForObstacle();
		}
		
	}
	

}
