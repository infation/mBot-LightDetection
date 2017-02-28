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
      //Control control = new Control(myFinch);
      Kennel kennel = new Kennel(myFinch);
      TwoLights twoLights = new TwoLights(myFinch);
      twoLights.twoLightsInBox();
      //control.goStraight();
      //control.twoLightsInBox();
      //kennel.kennelInABox();
      //control.findDark();
      myFinch.quit();
      // Always end your program with finch.quit()

      }
   }

