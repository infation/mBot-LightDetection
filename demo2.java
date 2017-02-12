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
	//Rotate finch for 1.5 sec 
	public static void randomRotate(Finch myFinch) {
		Random rand = new Random();
		int randSpeed = (rand.nextInt() % 210) + 40;
		myFinch.setWheelVelocities(randSpeed, -randSpeed, 1500);
	}
	
	public static void rotate(Finch myFinch, LightData initialData){
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
	
   @SuppressWarnings("unused")
public static void main(final String[] args) throws InterruptedException
      {
      // Instantiating the Finch object
      Finch myFinch = new Finch();

      // Write some code here!
      
      LightData initialData = new LightData(myFinch.getLightSensors());
      boolean[] obstSensors = new boolean[2];
      double temperature;
      //Vector<LightData> allData = new Vector<LightData>();
      while(true){
    	 for(int i=0;i<10;i++){
    	   rotate(myFinch, initialData);
    	 }
    	   myFinch.quit();
    	   System.exit(0);
      }
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

