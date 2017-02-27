package Code;

import java.io.Console;
import java.io.IOException;
import java.util.Scanner;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

public class Calibration {
	
	private int maxValue;
	private int minValue;
	private int averageValue;
	private Finch myFinch;
	
	public Calibration(Finch myFinch){
		maxValue = 0;
		minValue = 0;
		averageValue = 0;
		this.myFinch = myFinch;
	}
	
	public int getMaxValue() {
		return maxValue;
	}
	
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}
	
	public int getMinValue() {
		return minValue;
	}
	
	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}
	
	public int getAverageValue() {
		return averageValue;
	}
	
	public void setAverageValue(int averageValue) {
		this.averageValue = averageValue;
	}
	
	public void calibrate() {
		int numCalibrations = 150;
		Scanner s = new Scanner(System.in);
		
		System.out.println("Place finch in darkest place");
		System.out.print("Press enter to begin! ");
		s.nextLine();
		calibrateMin(numCalibrations);
	
		
		System.out.println("Place the finch in lightest place");
		System.out.print("Press enter to begin! ");
		s.nextLine();
		calibrateMax(numCalibrations);
		
		/*System.out.println("Place the finch in the middle of field");
		System.out.print("Press enter to begin! ");
		s.nextLine();
		calibrateAverage(numCalibrations);*/
		
		s.close();
		
	}
	
	public void calibrateMin(int numCalibrations) {
		LightData data = new LightData(myFinch.getLightSensors());
        
		for(int i = 0; i < numCalibrations; i++) {
			this.minValue += data.getSum();
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			data = new LightData(myFinch.getLightSensors());
		}
		
		this.minValue = this.minValue / numCalibrations;
		
		System.out.println("Calibration done!");
		System.out.println("Min Value " + this.minValue);
	}
	
	public void calibrateMax(int numCalibrations) {
		LightData data = new LightData(myFinch.getLightSensors());
        
		data = new LightData(myFinch.getLightSensors());
		
		for(int i = 0; i < numCalibrations; i++) {
			this.maxValue += data.getSum();
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			data = new LightData(myFinch.getLightSensors());
		}
		
		this.maxValue = this.maxValue / numCalibrations;
		
		
		System.out.println("Calibration done!");
		System.out.println("Max Value " + this.maxValue);
		
	}

	public void calibrateAverage(int numCalibrations) {
		LightData data = new LightData(myFinch.getLightSensors());
		
		data = new LightData(myFinch.getLightSensors());
		
		for(int i = 0; i < numCalibrations; i++) {
			this.averageValue += data.getSum();
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			data = new LightData(myFinch.getLightSensors());
		}
		
		this.averageValue = this.averageValue / numCalibrations;
		
		System.out.println("Calibration done!");
		System.out.println("Average Value " + this.averageValue);
	}
}
