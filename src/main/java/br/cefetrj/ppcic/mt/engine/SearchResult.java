package br.cefetrj.ppcic.mt.engine;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 
 * @author rtavares
 *
 */

public class SearchResult {

	private String query;
	
	private double[] queryVector;
	
	private LinkedHashMap<Long, Double> hashDocCosineDist;
	
	private LinkedHashMap<Long, Double> hashDocDist;
	
	private List<Long> docResult;
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public LinkedHashMap<Long, Double> getHashDocCosineDist() {
		return hashDocCosineDist;
	}

	public void setHashDocCosineDist(LinkedHashMap<Long, Double> hashDocCosineDist) {
		this.hashDocCosineDist = hashDocCosineDist;
	}

	public LinkedHashMap<Long, Double> getHashDocDist() {
		return hashDocDist;
	}

	public void setHashDocDist(LinkedHashMap<Long, Double> hashDocDist) {
		this.hashDocDist = hashDocDist;
	}

	public double[] getQueryVector() {
		return queryVector;
	}

	public void setQueryVector(double[] queryVector) {
		this.queryVector = queryVector;
	}

	public List<Long> getDocResult() {
		return docResult;
	}

	public void setDocResult(List<Long> docResult) {
		this.docResult = docResult;
	}
	
}
