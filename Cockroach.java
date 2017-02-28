package Code;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;
	
public class Cockroach extends Control{
	private Finch myFinch;
	public Cockroach(Finch myFinch) {
		super(myFinch);
		this.myFinch = myFinch;
		// TODO Auto-generated constructor stub
	}
	
	public void cockroach(){
		
		
		//currentData.printData();
		
		Calibration values = new Calibration(myFinch);
		values.calibrateMin();
		values.calibrateAverage();
		values.calibrateMax();
		int shade = values.getMinValue();
		int lightOff = values.getAverageValue();
		int lightOn = values.getMaxValue();
		
		while(true){
			LightData currentData = new LightData(myFinch.getLightSensors());
			//LightData currentData = new LightData(myFinch.getLightSensors());
			
			//If the light has been turned off roam slowly around the room
			if(lightOn > currentData.getSum() + 30 || lightOff < currentData.getSum()-10){
				lookForLight(lightOn);
			}
			
			currentData = new LightData(myFinch.getLightSensors());			

			//Quickly find shade
			if(shade < currentData.getSum() - 10 || shade > currentData.getSum()-10 ){
				lookForShade(shade, lightOff);
				myFinch.stopWheels();
				myFinch.sleep(1000);
				//myFinch.setLED(0, 255, 0);
			}
			
			currentData = new LightData(myFinch.getLightSensors());
			
			//If the shade has been found stay there until the external light has been turned off or there is
			//change in the environment and the shade is not the safe spot anymore
			while(shade > currentData.getSum()-20){
				currentData = new LightData(myFinch.getLightSensors());
				myFinch.setLED(0, 255, 0);
			}
			
	
	
				//If the external light is turned off and the finch is safe to roam
					 //If the shade is not the safe spot anymore since there is light coming in
		}
		
	}

	public void lookForLight(int value){

		LightData currentData = new LightData(myFinch.getLightSensors());

		while(value>currentData.getSum()+30){
			avoidObstacle(true);
			lightDecisionTest(true, 70);
			currentData = new LightData(myFinch.getLightSensors());
			myFinch.setLED(0, 255, 0);
		}
		
	}

	//A function to run away from the light and find the darkest place
	public void lookForShade(int shade, int lightOff){
	
		LightData currentData = new LightData(myFinch.getLightSensors());
		
		while((shade < currentData.getSum() - 20) || (lightOff < currentData.getSum()-20)){
			
			/*if(currentData.getSum() < maxValue - 20 ) {
				return;
			}*/
			if((currentData.getLeft()>currentData.getRight()+15)&&(currentData.getLeft()*2-10<shade)
				||(currentData.getLeft()+15<currentData.getRight())&&(currentData.getRight()*2-10<shade)){
				findLightOrShade(false);
				return;
			}
			else{
				avoidObstacle(false);
				lightDecisionTest(false, 170);

			}
			
			currentData = new LightData(myFinch.getLightSensors());
			myFinch.setLED(255, 0, 0);
		}
	}

	
}
