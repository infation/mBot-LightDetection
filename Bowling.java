package Code;
// Needs a package declaration to move to another folder

import java.util.Scanner;
import edu.cmu.ri.createlab.terk.robot.finch.Finch;

/**
 * Created by: Stanislav Minev and Ivaylo Nenovski
 * Date: 01/24/17
 * A bowling program for the Finch
 */

public class Bowling {

	/*
	 * A function to change the color of the finch beak while 
	 * in motion. Still in progress..
	 * */
	public static void rainbow(Finch myFinch, int color){
		
		if(color+100<255){
			
			myFinch.setLED(color, color+50, color + 100);
			
		}
		else{
			myFinch.setLED(color, color+50, color-100);
		}
	}
	
	public static void menu() {
		System.out.println("Please enter one of the choises (1-4)");
		System.out.println("1. No Bumpers Control Law");
		System.out.println("2. Bumpers Control Law");
		System.out.println("3. Second try for no bumpers");
		System.out.println("4. Play music");
		System.out.println("5. Exit");
	}
	
	/*
	 * Dead Reckoning - yeah this wasn't a great idea..
	 * */
	public static void deadReckoning(Finch myFinch) {
		
		while (true) {
			
			myFinch.setWheelVelocities(150, 153);
	
			if(myFinch.isObstacle()) {
				for(int i=0;i<3;i++){
					myFinch.setWheelVelocities(-140, -140, 1500);
					myFinch.setWheelVelocities(240, 50, 1000);
					myFinch.setWheelVelocities(-255, -70, 1000);
					myFinch.setWheelVelocities(230, 233, 2000);
					myFinch.stopWheels();
				}
				break;
			}
		}
	}
	
	/*
	 * Decision Making function to decide whether the finch
	 * needs to go left, right or straight depending on the readings
	 * of the sensors
	 * */
	public static boolean decision(Finch myFinch,boolean[] sensorVals) {
		
		myFinch.stopWheels();
		myFinch.sleep(100);
		boolean isLeft;
		
		/*if(myFinch.isObstacleLeftSide() && myFinch.isObstacleRightSide()) { // go straight
			myFinch.setWheelVelocities(240, 240, 100);
			isLeft = true;
		}*/
		if(sensorVals[0]) {
			myFinch.setWheelVelocities(-100, 0 , 800);
			isLeft = true;
		}
		else {
			myFinch.setWheelVelocities(0, -100, 800);
			isLeft = false;
		}
		myFinch.setWheelVelocities(255, 255,1000);
		
		return isLeft;
	}
	
	/*
	*	A no bumpers control law to try to get a strike 
	*	from the second try. Instead of detecting obstacles, it
	*   goes straight for 10 seconds and it does it's finishing move 
	*/
	public static void noBumpersSecondTry(Finch myFinch, int time){
		
		int speed=35;
		Long timeA = System.currentTimeMillis();

		while(true){
			myFinch.setWheelVelocities(speed, speed+2);
			if(speed<=251){
				speed+=1;	
			}
			//myFinch.sleep(20);
			if(System.currentTimeMillis() - timeA > 7000) {
				break;
			}
		}
		finishMove(myFinch);
	}
	
	/*
	 * Control law with no bumpers
	 * Gradually accelerate the finch. Whenever an obstacle is detected
	 * depending on which side it is detected (left or right), go back and
	 * turn in that direction, then go back and forths full speed to get as
	 *  many pins as you can knock down, after which rotate to eliminate
	 *  any remaining pins
	 * */
	public static void noBumpersControlLaw(Finch myFinch, int time) {
		
		int speed = 35;
		boolean isLeft;
		long timeB = System.currentTimeMillis();
		
		while (System.currentTimeMillis()-timeB < time*1000){
			boolean[] sensors = myFinch.getObstacleSensors();
			//rainbow(myFinch,speed);
			if(sensors[0] || sensors[1]) {	
					isLeft = decision(myFinch,sensors);
					
					for(int i = 0; i < 2; i++){
						myFinch.stopWheels();
						
						myFinch.setWheelVelocities(-255, -255, 900);
						if(isLeft) {
							myFinch.setWheelVelocities(0, -100, 1000);
							
						}
						else {
							myFinch.setWheelVelocities(-100, 0 , 1000);
						}
						isLeft = !isLeft;
						myFinch.setWheelVelocities(255, 255, 1000);
						
					}
					
					rotate(myFinch);
					break;
				}
			myFinch.setWheelVelocities(speed, speed+2);
			if(speed<=251){
				speed+=1;	
			}
						
		}
		myFinch.stopWheels();
	}
	
	//Rotate finch for 1.5 sec 
	public static void rotate(Finch myFinch) {
		myFinch.setWheelVelocities(200, -200, 1500);
	}
	
	
	//A function for the finishing move, without using obstacle sensors
	public static void finishMove(Finch myFinch) {
		for(int i = 0; i < 2; i++) {
			myFinch.stopWheels();
				
			myFinch.setWheelVelocities(-255, -255, 900);
			if(i % 2 == 0) {
				myFinch.setWheelVelocities(0, -100, 1000);
			}
			else {
				myFinch.setWheelVelocities(-100, 0 , 1000);
			}
			myFinch.setWheelVelocities(255, 255, 1000);
				
		}
			
		rotate(myFinch);
	}
	
	/*
	 * Decision function for the zigzag control law to decide whether the finch
	 * detects an obstacle from left or right. For 2 seconds the robot goes slowly to
	 * bump into the obstacle. While doing that it checks if it was tapped. The isTapped() 
	 * function is checked twice and only returns true in both cases if the obstacle is indeed
	 * a bumper. If it's pins it will just go for 2 seconds and will return false. In that case
	 * the finishing move is activated to finish off any remaining pins.
	 * */
	public static Boolean decisionBumper(Finch myFinch) {
		boolean[] sensors = myFinch.getObstacleSensors();
		
		if( sensors[0] || sensors[1]) {		
			myFinch.stopWheels();
			myFinch.sleep(300);
			myFinch.setWheelVelocities(135, 135);
			
			Long timeA = System.currentTimeMillis();
			
			while(true) {
				
				if(myFinch.isTapped() && myFinch.isTapped()) {
					
					myFinch.saySomething("Bumper!");
		
					myFinch.setWheelVelocities(-255, -255, 900);
					
					if(sensors[0]) {
						myFinch.setWheelVelocities(0, -100, 1200);
					}
					else {
						myFinch.setWheelVelocities(-100, 0 , 1400);
					}
					return true;
				}	

				else if( System.currentTimeMillis() - timeA > 2000) {
					
					myFinch.saySomething("Initializing destructive sequence!");

					boolean isLeft = decision(myFinch,sensors);

					for(int i = 0; i < 2; i++){
						myFinch.stopWheels();
							
						myFinch.setWheelVelocities(-255, -255, 900);
						if(isLeft) {
							myFinch.setWheelVelocities(-100, 0, 1000);
							
						}
						else {
							myFinch.setWheelVelocities(0, -100 , 1000);
						}
						myFinch.setWheelVelocities(255, 255, 1000);
							
					}
							
						
						rotate(myFinch);
						return false;
				}
				
			}
					
		}
		else {
			return null;
		}
	}	
	
	
	/*
	 * Control law for when there are bumpers around. The
	 * idea of this control law is to go either left or right
	 * and whenever a bumper is detected go back and turn the
	 * other directions
	 * */
	public static void zigZag(Finch myFinch, int time) {
		int speed = 35;
		long timeB = System.currentTimeMillis();
		while(System.currentTimeMillis()-timeB<time*1000) {
			Boolean answer = decisionBumper(myFinch);
			
			if(answer!=null){
				if (answer) {
					myFinch.stopWheels();
					myFinch.sleep(100);
					speed = 35;
				}
				else {
					//finishMove(myFinch);
					break;
				}
			}
			
			myFinch.setWheelVelocities(speed, speed + 4);
			speed += 5;
		}
		myFinch.stopWheels();
	}
	
	/*
	 * if the time given is more than 20 seconds, then put the
	 * whole thread to sleep. If the time is less than 20 seconds,
	 * then the finch will makes a move
	 * */
	public static void sleep(int time){
		if(time>20){
			int sleep_time = time - 20;
			sleep_time *= 1000;
			try {
				Thread.sleep(sleep_time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//Play a buzz sound on the finch's internal buzzer
	public static void playMusic(Finch myFinch) {
		for(int i = 50; i<1000;i++){
			myFinch.buzz(i, 100);
		}
	}
	
	public static void main(final String[] args) {
	   
      // Instantiating the Finch object
      Finch finchie = new Finch();
      
      finchie.setLED(0, 255, 0);
      //finchie.saySomething("Professor Fine, buckle up, show's about to start!");

      // Write some code here!
      
      Scanner s = new Scanner(System.in);
      int option = 0; // default value
      
      
      //The menu 
      while(option != 5) {
    	  System.out.println("Enter time: ");
          int time = s.nextInt();
    	  menu();
    	  option = s.nextInt();
    	  
    	  //sleep(time);
    	  
    	  switch(option) {
    	  case 1:
    		  noBumpersControlLaw(finchie,time);
    		  break;
    		  
    	  case 2:
    		  zigZag(finchie,time);
    		  break;
    		  
    	  case 3:
    		  noBumpersSecondTry(finchie,time);
    		  //deadReckoning(finchie);
    		  break;
    		  
    	  case 4:
    		  playMusic(finchie);
    		  break;
    	  }
      }

      // Always end your program with finch.quit()
      finchie.setLED(255, 0, 0, 1000);
      finchie.quit();
      s.close();
      
      
      System.exit(0);
      
      }
   
   }

