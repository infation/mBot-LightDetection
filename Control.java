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
	      boolean[] obstSensors = new boolean[2];
	      double temperature;
	      //Vector<LightData> allData = new Vector<LightData>();
	      while(true){
	    	 for(int i=0;i<10;i++){
	    	   lightRotate(myFinch, initialData);
	    	 }
	    	   myFinch.quit();
	    	   System.exit(0);
	      }
	}
	
	public static void lightRotate(Finch myFinch, LightData initialData){
		for(int i = 0; i<7; i++){
			myFinch.setWheelVelocities(100,-100, 500);
			LightData newData = new LightData(myFinch.getLightSensors());
			if(initialData.getSum()<newData.getSum()){
				initialData.setData(newData);
				myFinch.setWheelVelocities(200, 200, 1000);
				break;
			}
		}
	}
	/*******TWO LIGHTS IN A BOX FUNCTIONS END****************
	 ***************************************************************
	 ***************************************************************
	 ***************************************************************
	 */
	
	
}
