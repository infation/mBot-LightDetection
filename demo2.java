package Code;
// Needs a package declaration to move to another folder

import java.util.Random;
import java.util.Vector;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

/**
 * Created by:
 * Date:
 * A starter file to use the Finch
 */

public class demo2
{

	
   @SuppressWarnings("unused")
public static void main(final String[] args) throws InterruptedException
      {
      // Instantiating the Finch object
      Finch myFinch = new Finch();
      Control control = new Control(myFinch);
      int[] sensors = new int[2];
      control.twoLightsInBox();
      /*
      for(int i = 0; i < 10; ++i) {
    	sensors = myFinch.getLightSensors();
	  	//temperature = myFinch.getTemperature();
	  	System.out.println(sensors[0] + "   " + sensors[1] + "   ");
	  	Thread.sleep(1000);
      }*/
      // Write some code here!

    			 //}
    	//else if(obstSensors[0]||obstSensors[1]||(myFinch.isTapped() && myFinch.isTapped())){
    	/*	myFinch.stopWheels();
    		myFinch.sleep(100);
    		myFinch.setWheelVelocities(-255, -255, 500);
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

      }
   }

