package Code;

import java.util.Scanner;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

public class Calibration {
	
	private int maxValue;
	private int minValue;
	private int averageValue;
	private Finch myFinch;
	private int numCalibrations;
	
	public Calibration(Finch myFinch){
		maxValue = 0;
		minValue = 0;
		averageValue = 0;
		numCalibrations = 150;
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
	
	public void calibrateMin() {
		
		Scanner s = new Scanner(System.in);
		System.out.println("MIN Threshold : ");
		System.out.print("Press enter to begin! ");
		s.nextLine();
		
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
		System.out.print("Press enter for next step! ");
		s.nextLine();
		
	}
	
	public void calibrateMax() {
		
		Scanner s = new Scanner(System.in);
		System.out.println("MAX Threshold : ");
		System.out.print("Press enter to begin! ");
		s.nextLine();
		
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
		System.out.print("Press enter for next step! ");
		s.nextLine();
		
	}

	public void calibrateAverage() {
		
		Scanner s = new Scanner(System.in);
		System.out.println("AVERAGE Threshold : ");
		System.out.print("Press enter to begin! ");
		s.nextLine();
		
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
		System.out.print("Press enter for next step! ");
		s.nextLine();
	}
}
