package br.cefetrj.ppcic.mt.engine;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import br.cefetrj.ppcic.mt.util.MathUtil;
import br.cefetrj.ppcic.mt.util.TextUtil;

/**
 * 
 * @author rtavares
 *
 */
public class TextMining {
	
	private static final DecimalFormat dff = new DecimalFormat("00.00");
	
	private static final DecimalFormat lf = new DecimalFormat("00");
	
	private long n = 0;
	
	private double[][] idf;
	
	private double[][] tf;
	
	private double[][] tfxdf;
	
	private double[][] tfxidf;
	
	private double[][] tfw;
	
	private double[][] tfwxidf;
	
	private LinkedHashMap<Long, String> bagDfId;
	private LinkedHashMap<Long, Long> bagDf;
	private LinkedHashMap<Long, Double> bagDfLog;
	private LinkedHashMap<Long, String> bagTfId;
	private LinkedHashMap<Long, LinkedHashMap<Long, Long>> bagTf;
	
	public TextMining(String sFolder){
		
		init(sFolder);
		
	}
	
	private void init(String sFolder){
		
		try{
		
			File folder = new File(sFolder);
			
			bagDfId = new LinkedHashMap<Long, String>();
			Long bagIdkey = 0L;
			
			Long docIdKey = 0L;
			
			bagDf = new LinkedHashMap<Long, Long>();
			bagDfLog = new LinkedHashMap<Long, Double>();
			
			bagTfId = new LinkedHashMap<Long, String>();
			bagTf = new LinkedHashMap<Long, LinkedHashMap<Long, Long>>();
			
			for (final File file : folder.listFiles()) {
		        
				String fileName = file.getName().trim().toLowerCase();
				
				if (file.isDirectory() || !fileName.contains(".txt"))
		            continue;
				
				Scanner scan = new Scanner(file);
				StringBuilder docContent = new StringBuilder("");
				
				while(scan.hasNextLine())
					docContent.append(scan.nextLine());
			    
				scan.close();
				
				n++;
				
				String cleanContent = TextUtil.clean(docContent.toString());
				
				List<String> lstDocument = TextUtil.getContentList(cleanContent);
				
				List<String> lstCleanDocument = TextUtil.removeTokens(lstDocument, TextUtil.getStopWordList());
 				
				LinkedHashMap<Long, Long> hDocTF = new LinkedHashMap<Long, Long>();
				
				for(String token : lstCleanDocument){
					
					token = token.trim();
					
					Long tId = null;
					
					if(!bagDfId.containsValue(token)){
						
						bagDfId.put(++bagIdkey, token);
						tId = bagIdkey;
						
					}else{
						
						Iterator<Long> itBagId = bagDfId.keySet().iterator();
						while(itBagId.hasNext()){
							
							Long id = itBagId.next();
							
							if(token.equals(bagDfId.get(id))){
								
								tId = id;
								break;
								
							}
							
						}
						
					}
					
					if(!hDocTF.containsKey(tId))
						hDocTF.put(tId, 1L);
					
					else
						hDocTF.put(tId, (hDocTF.get(tId).longValue() + 1));
					
				}
				
				Iterator<Long> itDocTf = hDocTF.keySet().iterator();
				
				while(itDocTf.hasNext()){
					
					Long tId = itDocTf.next();
					
					if(!bagDf.containsKey(tId))
						bagDf.put(tId, 1L);
					
					else
						bagDf.put(tId, (bagDf.get(tId).longValue() + 1));
					
				}
				
				docIdKey++;
				
				bagTfId.put(docIdKey, fileName);
				bagTf.put(docIdKey, hDocTF);
				
		    }
			
			idf = new double[bagDf.size()][4];
			
			int posX = -1;
			
			Iterator<Long> itIdf = bagDf.keySet().iterator();
			while(itIdf.hasNext()){
				
				Long tId = itIdf.next();
				
				posX++;
				
				idf[posX][0] = new Double(tId).doubleValue();
				idf[posX][1] = bagDf.get(tId).doubleValue();
				idf[posX][2] = new Double(n).doubleValue() / idf[posX][1];
				idf[posX][3] = Math.log10(idf[posX][2]);
				
				bagDfLog.put(tId, idf[posX][3]);
				
			}
			
			tf = new double[bagTf.size() + 1][bagDf.size() + 1];
			
			posX = 0;
			int posY = 0;
			
			Iterator<Long> itBagDf = bagDf.keySet().iterator();
			
			while(itBagDf.hasNext())
				tf[posX][++posY] = itBagDf.next();
			
			Iterator<Long> itTf = bagTf.keySet().iterator();
			
			while(itTf.hasNext()){
				
				Long docId = itTf.next();
				
				posX++;
				
				posY = 0;
				
				tf[posX][posY] = docId;
				
				LinkedHashMap<Long, Long> hDtf = bagTf.get(docId);
				
				Iterator<Long> itDtf = bagDf.keySet().iterator();
				
				while(itDtf.hasNext()){
					
					Long tId = itDtf.next();
					
					Long freq = hDtf.get(tId);
					
					if(freq == null)
						freq = 0L;
					
					tf[posX][++posY] = freq;
					
				}
				
			}
			
			tfxdf = new double[tf.length][tf[0].length];
			tfxdf[0] = tf[0];
			
			tfxidf = new double[tf.length][tf[0].length];;
			tfxidf[0] = tf[0];
			
			tfw = new double[tf.length][tf[0].length];;
			tfw[0] = tf[0];
			
			tfwxidf = new double[tf.length][tf[0].length];;
			tfwxidf[0] = tf[0];
			
			for(int i = 1; i < tf.length; i++){
				
				for(int j = 0; j < tf[0].length; j++){
					
					if(j == 0){
						
						tfxdf[i][j] = tf[i][j];
						tfxidf[i][j] = tf[i][j];
						tfw[i][j] = tf[i][j];
						tfwxidf[i][j] = tf[i][j];
					
					}else{
						
						tfxdf[i][j] = tf[i][j] * idf[j - 1][1];
						tfxidf[i][j] = tf[i][j] * idf[j - 1][3];
						
						double v = tf[i][j];
						
						double nv = 0;
						if(v > 0)
							nv = 1.00 + Math.log10(v);
							
						tfw[i][j] = nv;
						
						tfwxidf[i][j] = tfw[i][j] * idf[j - 1][3];
						
					}
				
				}
				
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
			
	}
	
	public String getDFMatrix(){
		
		StringBuilder ret = new StringBuilder("");
		
		ret.append("\n");
		
		ret.append(TextUtil.formatTextToLength("Termo", 22) + " | "
					+ TextUtil.formatTextToLength("Df", 5) + " | " 
					+ TextUtil.formatTextToLength("Idf", 10) + " | "
					+ TextUtil.formatTextToLength("Log", 10) + "\n");
		
		for(int i = 0; i < idf.length; i++){
			
			Long tId = new Double(idf[i][0]).longValue();
			
			Long df = new Double(idf[i][1]).longValue();
			
			Double vIdf = new Double(idf[i][2]);
			
			Double log = new Double(idf[i][3]);
			
			ret.append(TextUtil.formatTextToLength(bagDfId.get(tId), 22) + " | " + TextUtil.formatTextToLength(lf.format(df), 5) + " | " 
												+ TextUtil.formatTextToLength(dff.format(vIdf), 10) + " | " + TextUtil.formatTextToLength(dff.format(log), 10) + "\n");
			
		}
		
		return ret.toString();
		
	}
	
	public String getMatrixInTFFormat(double[][] mtf){
		
		StringBuilder ret = new StringBuilder("");
		
		ret.append("\n");
		
		for(int i = 0; i < mtf.length; i++){
			
			for(int j = 0; j < mtf[0].length; j++){
				
				if(i == 0){
					
					if(j == 0){
						
						ret.append(TextUtil.formatTextToLength("", 22));
					
					}else{
						
						Long tId = new Double(mtf[i][j]).longValue();
						ret.append(TextUtil.formatTextToLength(bagDfId.get(tId), 22));
						
					}
						
				}else{
				
					if(j == 0){
						
						String doc = bagTfId.get(new Double(mtf[i][j]).longValue());
						ret.append(TextUtil.formatTextToLength(doc, 22));
						
					}else{
						
						ret.append(TextUtil.formatTextToLength(dff.format(mtf[i][j]), 6));
						
					}
					
				}
				
				if(j != (mtf[i].length - 1))
					ret.append(" | ");
				
				else
					ret.append("\n");
				
			}
			
		}
		
		return ret.toString();
		
	}
	
	public SearchResult getSearchMap(String query){
		
		SearchResult sr = new SearchResult();
		sr.setQuery(query);
		
		String cleanQuery = TextUtil.clean(query);
		
		List<String> lstQuery = TextUtil.getContentList(cleanQuery);
		
		List<String> lstCleanQuery = TextUtil.removeTokens(lstQuery, TextUtil.getStopWordList());
		
		double[] queryVector = new double[bagDfLog.size()];
		
		Iterator<Long> itBag= bagDfId.keySet().iterator();
		
		int i = -1;
		
		while(itBag.hasNext()){
			
			Long tId = itBag.next();
			String term = bagDfId.get(tId);
			
			if(lstCleanQuery.contains(term))
				queryVector[++i] = bagDfLog.get(tId);
			else
				queryVector[++i] = 0;
			
		}
		
		sr.setQueryVector(queryVector);
		
		LinkedHashMap<Long, Double> hashDocCosineDist = new LinkedHashMap<Long, Double>();
		
		LinkedHashMap<Long, Double> hashDocDist = new LinkedHashMap<Long, Double>();
		
		for(int r = 1; r < tfwxidf.length; r++){
			
			Long docId = new Double(tfwxidf[r][0]).longValue();
			
			double[] docVector = new double[bagDf.size()];
			
			for(int j = 0; j < docVector.length; j++)
				docVector[j] = tfwxidf[r][j + 1];
			
			double cosineDistance = MathUtil.calculateCosineDistance(docVector, queryVector);
			
			hashDocCosineDist.put(docId, cosineDistance);
			
			double distance = MathUtil.calculateDistance(docVector, queryVector);
			
			hashDocDist.put(docId, distance);
			
		}
		
		sr.setHashDocCosineDist(hashDocCosineDist);
		sr.setHashDocDist(hashDocDist);
		
		return sr;
		
	}
	
	public SearchResult searchTopRelevantByCosineDistance(String query, int qtd){
		
		SearchResult sr = getSearchMap(query);
		
		LinkedHashMap<Long, Double> hashDocCosineDist = sr.getHashDocCosineDist();
		
		Iterator<Long> hashIt = hashDocCosineDist.keySet().iterator();
		
		List<Long> ids = new ArrayList<Long>();
		List<Double> distances = new ArrayList<Double>();
		
		while(hashIt.hasNext()){
			
			Long docId = hashIt.next();
			Double cosDist = hashDocCosineDist.get(docId);
			
			if(ids.size() <= qtd){
				
				ids.add(docId);
				distances.add(cosDist);
				
			}else{
				
				double minor = 999;
				int posMinor = -1;
				
				for(int i = 0; i < distances.size(); i++){
					
					Double dist = distances.get(i);
					
					if(dist.doubleValue() < minor){
						
						minor = dist.doubleValue();
						posMinor = i;
						
					}
					
				}
				
				if(cosDist.doubleValue() > minor){
					
					ids.set(posMinor, docId);
					distances.set(posMinor, cosDist);
					
				}
				
			}
			
		}
		
		List<Long> idsO = new ArrayList<Long>();
		
		while(ids.size() != 0){
			
			double max = -999;
			int posMax = -1;
			
			for(int i = 0; i < distances.size(); i++){
				
				double v = distances.get(i).doubleValue();
				
				if(v > max){
					
					max = v;
					posMax = i;
					
				}
				
			}
			
			idsO.add(ids.get(posMax));
			
			distances.remove(posMax);
			ids.remove(posMax);
			
		}
		
		sr.setDocResult(idsO);
		
		return sr;
		
	}
	
	public String searchTop10DocsBySimilarity(String query){
		
		SearchResult sr = searchTopRelevantByCosineDistance(query, 10);
		
		StringBuilder ret = new StringBuilder("");
		
		ret.append(TextUtil.formatTextToLength("Documento", 30) + "| " 
					+ TextUtil.formatTextToLength("Dist. Coseno", 16) + " | " 
					+ TextUtil.formatTextToLength("Similaridade", 16) + "\n");
		
		for(Long docId : sr.getDocResult())
			ret.append(TextUtil.formatTextToLength(bagTfId.get(docId), 30) + "|" 
							+ TextUtil.formatTextToLength(dff.format(sr.getHashDocCosineDist().get(docId)), 18) + "|"
							+ TextUtil.formatTextToLength(dff.format(sr.getHashDocDist().get(docId)), 16) + "\n");
		
		return ret.toString();
		
	}
	
	public void writeDocTf(String doc, List<String> terms){
		
		System.out.println("Write Doc = " + doc);
		
		Long docId = null;
		
		Iterator<Long> itDocId = bagTfId.keySet().iterator();
		while(itDocId.hasNext()){
			
			Long id = itDocId.next();
			if(bagTfId.get(id).trim().equals(doc.trim())){
				
				docId = id;
				break;
				
			}
			
		}
		
		String sTerms = "";
		String sValues = "";
		
		Iterator<Long> itVl = bagDfId.keySet().iterator();
		while(itVl.hasNext()){
			
			Long dfId = itVl.next();
			Long tfv = bagTf.get(docId).get(dfId);
			
			if(tfv == null)
				tfv = 0L;
			
			if(!terms.contains(bagDfId.get(dfId)))
				continue;
			
			sTerms += " | " + TextUtil.formatTextToLength(bagDfId.get(dfId), 15);
			sValues +=  " | " + TextUtil.formatTextToLength(lf.format(tfv), 15);
		
		}
		
		System.out.println(sTerms);
		System.out.println(sValues);
		System.out.println("");
		
	}

	public double[][] getIdf() {
		return idf;
	}

	public double[][] getTf() {
		return tf;
	}

	public double[][] getTfxdf() {
		return tfxdf;
	}

	public double[][] getTfxidf() {
		return tfxidf;
	}

	public double[][] getTfw() {
		return tfw;
	}

	public double[][] getTfwxidf() {
		return tfwxidf;
	}
	
	public long getN(){
		return n;
	}
	
}
