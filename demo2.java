package Code;
// Needs a package declaration to move to another folder

import java.util.Scanner;
import edu.cmu.ri.createlab.terk.robot.finch.Finch;

/**
 * Created by:
 * Date:
 * A starter file to use the Finch
 */

public class demo2
{
	
	static void menu() {
		System.out.println("Please choose a control law..");
		System.out.println("Press 1 for Kennel in a Box");
		System.out.println("Press 2 for Two lights in a Box");
		System.out.println("Press 3 for Low Obstacle");
		System.out.println("Press 4 for Cockroach");
		System.out.println("Press 5 to quit");
	}

   public static void main(final String[] args) throws InterruptedException
      {
	      // Instantiating the Finch object
	      Finch myFinch = new Finch();
	      //Calibration thresholdVals = new Calibration(myFinch);
	      
	      //thresholdVals.calibrate();
	
	      // Write some code here!
	      Scanner sn = new Scanner(System.in);
	      int option = 0;
	      while(option!=5){
	    	  menu();
	    	  option = sn.nextInt();
		      switch(option) {
		      case 1:
		    	  Kennel kennel = new Kennel(myFinch);
		    	  kennel.kennelInABox();
		    	  break;
		    	  
		      case 2:
		    	  TwoLights twoLights = new TwoLights(myFinch);
		    	  twoLights.twoLightsInBox();
		    	  break;
		
		      case 3:
		    	  LowObstacle lowObstacle = new LowObstacle(myFinch);
		    	  lowObstacle.lowObstacle();
		    	  break; 	  
		      case 4:
		    	  Cockroach cockroach = new Cockroach(myFinch);
		    	  cockroach.cockroach();
		    	  break;
		      }
	      }
	      

      
      // Always end your program with finch.quit()
      
      sn.close();
      myFinch.quit();
      
      }
   }

