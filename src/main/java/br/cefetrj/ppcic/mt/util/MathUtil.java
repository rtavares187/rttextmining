package br.cefetrj.ppcic.mt.util;

/**
 * 
 * @author rtavares
 *
 */

public class MathUtil {
	
	public static double calculateCosineDistance(double[] a, double[] b){
		
		double n = 0;
		
		double asums = 0;
		
		double bsums = 0;
		
		for(int i = 0; i < a.length; i++){
			
			n += a[i] * b[i];
			asums += Math.pow(a[i], 2);
			bsums += Math.pow(b[i], 2);
			
		}
		
		double d = Math.sqrt(asums) * Math.sqrt(bsums);
		
		return n / d;
		
	}
	
	public static double calculateDistance(double[] a, double[] b){
		
		double ret = 0;
		
		for(int i = 0; i < a.length; i++)
			ret += a[i] * b[i];
		
		return ret;
		
	}
	
	public static double calculatePrecision(double relevantReturned, double totalReturned){
		
		return relevantReturned / totalReturned;
		
	}
	
	public static double calculateRecall(double relevantReturned, double totalRelevant){
		
		return relevantReturned / totalRelevant;
	
	}
	
	public static double calculateFMeasure(double p, double r){
		
		return (2 * p * r) / (p + r);
		
	}
	
}
