package Code;
// Needs a package declaration to move to another folder

import java.util.Random;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

/**
 * Created by:
 * Date:
 * A starter file to use the Finch
 */

public class demo2
   {
	//Rotate finch for 1.5 sec 
	public static void rotate(Finch myFinch) {
		Random rand = new Random();
		int randSpeed = (rand.nextInt() % 210) + 40;
		myFinch.setWheelVelocities(randSpeed, -randSpeed, 1500);
	}
	
	
   public static void main(final String[] args) throws InterruptedException
      {
      // Instantiating the Finch object
      Finch myFinch = new Finch();

      // Write some code here!
      
      int[] lightSensors = new int[2];
      boolean[] obstSensors = new boolean[2];
      double temperature;
      while(true){
    	  myFinch.setWheelVelocities(200, 200);
    	  obstSensors = myFinch.getObstacleSensors();
    	if(false){
    		break;
    	}
    	else if(obstSensors[0]||obstSensors[1]||(myFinch.isTapped() && myFinch.isTapped())){
    		myFinch.stopWheels();
    		myFinch.sleep(100);
    		myFinch.setWheelVelocities(-255, -255, 900);
    		rotate(myFinch);
    	}
    	
      }
      
      /*for(int i = 0; i < 100; ++i) {
    	  sensors = myFinch.getLightSensors();
    	  temperature = myFinch.getTemperature();
    	  System.out.println(sensors[0] + "   " + sensors[1] + "   " + temperature*1.8 + 32);
    	  Thread.sleep(1000);
      }*/
      
      // this is a github test
      
      // Always end your program with finch.quit()
      myFinch.quit();
      System.exit(0);
      }
   }

