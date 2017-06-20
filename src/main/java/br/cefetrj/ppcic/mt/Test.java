package br.cefetrj.ppcic.mt;

import br.cefetrj.ppcic.mt.engine.TextMining;
import br.cefetrj.ppcic.mt.util.MathUtil;


/**
 * 
 * @author rtavares
 *
 */
public class Test {

	public static void main(String[] args) {
		
		String query = "carro bonito";
		String folder = "W:\\CEFET\\2017.2\\MT\\java\\src\\main\\resources\\teste";
		TextMining tm = new TextMining(folder);
		
		System.out.println(tm.searchTop10DocsBySimilarity(query));
		/*
		double p = MathUtil.calculatePrecision(returned, relevant);
		
		double r = MathUtil.calculateRecall(relevantReturned, totalRelevant);
		
		double f = MathUtil.calculateFMeasure(p, r)
		*/
	}

}
