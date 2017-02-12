package Code;

public class LightData {
	private int[] sensorData;
	private int dataSum;
	
	public LightData(){
		sensorData = new int[2];
	}
	
	public LightData(int[] data){
		sensorData = new int[2];
		sensorData[0] = data[0];
		sensorData[1] = data[1];
		dataSum = data[0]+data[1];
	}
	
	public int getSum(){
		return dataSum;
	}
	
	public void setLeft(int left){
		sensorData[0]=left;
	}
	
	public void setRight(int right){
		sensorData[1]=right;
	}
	
	public int getLeft(){
		return sensorData[0];
	}
	
	public int getRight(){
		return sensorData[1];
	}
	
	public void setData(LightData data){
		setLeft(data.getLeft());
		setRight(data.getRight());
		dataSum = data.getSum();
	}
	
	public void printData(){
		System.out.println(sensorData[0]+" "+sensorData[1]+ " "+dataSum);
	}
}
