package br.cefetrj.ppcic.mt;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.cefetrj.ppcic.mt.engine.TextMining;
import br.cefetrj.ppcic.mt.util.MathUtil;
import br.cefetrj.ppcic.mt.util.TextUtil;

/**
 * 
 * @author rtavares
 *
 */
public class Main {
	
	public static void main(String[] args) {
		
		int printMode = 2; // 0 = console | 1 = PDF | 2 = Ambos
		
		StringBuilder header = new StringBuilder("");
		
		header.append("CEFET-RJ - PPCIC - Mineração de Texto \n\n");
		header.append("Professor: Gustavo Guedes\n");
		header.append("Aluno: Rodrigo Tavares de Souza\n\n");
		header.append("Trabalho 2\n\n");
		
		curto(header, printMode);
		
		longo(header, printMode);
		
	}
	
	public static void curto(StringBuilder header, int printMode){
		
		String folder = "W:\\CEFET\\2017.2\\MT\\RTTextMining\\src\\main\\resources\\curto";
		
		StringBuilder text = new StringBuilder("");
		text.append("Parte 1 - Curto\n\n");
		
		Date start = new Date();
		
		TextMining tm = new TextMining(folder);
		
		Date end = new Date();
		long time = end.getTime() - start.getTime();
		double timeS = new Double(time) / new Double(1000);
		
		text.append("Tempo de processamento: " + time + " (ms) / " + new DecimalFormat("00.000").format(timeS) + " (s).\n\n");
		
		start = new Date();
		
		text.append("Matriz DF:\n");
		text.append(tm.getDFMatrix());
		text.append("\n\n");
		
		text.append("Matriz TF:\n");
		text.append(tm.getMatrixInTFFormat(tm.getTf()));
		text.append("\n\n");
		
		text.append("Matriz TF x DF:\n");
		text.append(tm.getMatrixInTFFormat(tm.getTfxdf()));
		text.append("\n\n");
		
		text.append("Matriz TF x IDF:\n");
		text.append(tm.getMatrixInTFFormat(tm.getTfxidf()));
		text.append("\n\n");
		
		text.append("Matriz TF c/ peso W:\n");
		text.append(tm.getMatrixInTFFormat(tm.getTfw()));
		text.append("\n\n");
		
		text.append("Matriz TF c/ peso W x IDF:\n");
		text.append(tm.getMatrixInTFFormat(tm.getTfwxidf()));
		
		end = new Date();
		time = end.getTime() - start.getTime();
		timeS = new Double(time) / new Double(1000);
		
		text.append("\n\nTempo de escrita (console / PDF): " + time + " (ms) / " + new DecimalFormat("00.000").format(timeS) + " (s).\n\n");
		
		String hText = header.toString();
		String sText = text.toString();
		
		if(printMode == 0 || printMode == 2)
			System.out.println(hText + sText);
		
		String pdfFile = "W:\\CEFET\\2017.2\\MT\\RTTextMining\\src\\main\\resources\\pdf\\curto_resultado.pdf";
		
		if(printMode == 1 || printMode == 2)
			TextUtil.writePdf(pdfFile, hText, sText);
		
	}
	
	public static void longo(StringBuilder header, int printMode){
		
		String folder = "W:\\CEFET\\2017.2\\MT\\RTTextMining\\src\\main\\resources\\longo";
		
		StringBuilder text = new StringBuilder("");
		text.append("Parte 2 - Long\n\n");
		
		String query = "kids dog";
		
		Date start = new Date();
		
		TextMining tm = new TextMining(folder);
		
		//writeDocFreqByTerm(tm);
		
		Date end = new Date();
		long time = end.getTime() - start.getTime();
		double timeS = new Double(time) / new Double(1000);
		
		text.append("Tempo de processamento: " + time + " (ms) / " + new DecimalFormat("00.000").format(timeS) + " (s).\n\n");
		
		start = new Date();
		
		text.append("Total de documentos processados: " + tm.getN() + "\n");
		text.append("10 documentos mais relevantes para a query: " + query + "\n\n");
		text.append(tm.searchTop10DocsBySimilarity(query));
		
		end = new Date();
		time = end.getTime() - start.getTime();
		timeS = new Double(time) / new Double(1000);
		
		text.append("\n\nTempo de escrita (console / PDF): " + time + " (ms) / " + new DecimalFormat("00.000").format(timeS) + " (s).\n\n");
		
		text.append("Existem 10 documentos relevantes que possuem as palavras kids e dog, com as seguintes frequências, respectivamente: \n");
		text.append("cv327_20292.txt (4 e 10)\n");
		text.append("cv304_28706.txt (7 e 4)\n");
		text.append("cv119_9867.txt (2 e 2)\n");
		text.append("cv298_23111.txt (1 e 2)\n");
		text.append("cv284_19119.txt (1 e 1)\n");
		text.append("cv153_10779.txt (0 e 7)\n");
		text.append("cv136_11505.txt (0 e 6)\n");
		text.append("cv112_11193.txt (0 e 6)\n");
		text.append("cv256_14740.txt (7 e 0)\n");
		text.append("cv380_7574.txt (5 e 0)\n\n");
		
		text.append("O sistema retornou 8 desses documentos. Kids aparece 84 vezes em 54 arquivos e dog 69 vezes em 26 arquivos. "
				    + "Dessa forma, dog tem peso maior do que kids. "
				    + "O sistema TF e DF não considera um peso maior na busca para documentos que apresentem simultaneamente os dois termos, por isso o "
				    + "documento cv284_19119.txt não foi retornado. O documento apresenta apenas 1 ocorrência de cada termo, o que resulta em um peso final baixo. "
				    + "O outro documento não retornado - cv256_14740.txt, possui baixa frequência dos demais termos além de kids. A distância por coseno utiliza a "
				    + "raiz do somatório dos quadrados das frequências desses termos excedentes, o que fez com que o mesmo tivesse baixo peso e portanto não fosse retornado. "
				    + "Para efeito de cálculo, são considerados então: 10 documentos retornados, 8 relevantes de um total de 10 relevantes esperados. "
				    + "Nessa configuração temos a seguinte precisão, abrangência e F-Measure:\n");
		
		
		double p = MathUtil.calculatePrecision(8, 10);
		text.append("Precisão: " + new DecimalFormat("00.000").format(p) + "\n");
		
		double r = MathUtil.calculateRecall(8, 10);
		text.append("Abrangência: " + new DecimalFormat("00.000").format(r) + "\n");
		
		double f = MathUtil.calculateFMeasure(p, r);
		text.append("F-Measure: " + new DecimalFormat("00.000").format(f) + "\n");
		
		String hText = header.toString();
		String sText = text.toString();
		
		if(printMode == 0 || printMode == 2)
			System.out.println(hText + "\n" + sText);
		
		String pdfFile = "W:\\CEFET\\2017.2\\MT\\RTTextMining\\src\\main\\resources\\pdf\\longo_resultado.pdf";
		
		if(printMode == 1 || printMode == 2)
			TextUtil.writePdf(pdfFile, hText, sText);
		
	}
	
	private static void writeDocFreqByTerm(TextMining tm){
		
		System.out.println("###");
		List<String> terms = new ArrayList<String>();
		terms.add("kids");
		terms.add("dog");
		tm.writeDocTf("cv256_14740.txt", terms);
		
		System.out.println("###");
		terms = new ArrayList<String>();
		terms.add("kids");
		terms.add("dog");
		tm.writeDocTf("cv380_7574.txt", terms);
		
	}

}
