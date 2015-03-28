

import java.util.ArrayList;

import org.apache.commons.math3.distribution.ExponentialDistribution;



public class digits {
	
	public static void main(String[] args) {
		int mean=50;
		ArrayList <Double> arr=new ArrayList<Double>();
		ExponentialDistribution ed=new ExponentialDistribution(mean);
		for(int i=0;i<1000;i++)
		{
			arr.add((ed.sample()));
			System.out.println((int)ed.sample());
		}
		double sum=0;
		for (Double temp : arr) {
			
			sum=sum+temp;
		}
		System.out.println("Average is:"+(sum/arr.size()));
		
		
	}
		
}


