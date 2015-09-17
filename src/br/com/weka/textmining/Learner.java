package br.com.weka.textmining;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.MultiFilter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.StringToWordVector;


public class Learner {
	/**
     * arquivo de treinamento.
     */
    Instances arqTreino;
    /**
     * filtro
     */
    StringToWordVector stringToWordVectorFiltro;
    /**
	 * classificador.
	 */
    FilteredClassifier classificador;
	/**
     * tokenizer.
     */
	WordTokenizer tokenizer;
	
	
	/**
	 * Método para carregar o arquivo ARFF.
	 */
	
	public void carregarArqTreino(String nomeArq) {
		try {
			BufferedReader leitor = new BufferedReader(new FileReader(nomeArq));
			ArffReader arffReader = new ArffReader(leitor);
			arqTreino = arffReader.getData();
			System.out.println("===== Arquivo carregado: " + nomeArq + " =====");
			leitor.close();
		}
		catch (IOException e) {
			System.out.println("Ocorreu problema durante a leitura:" + nomeArq);
		}
	}
	
	public void avaliar() {
		try {
			arqTreino.setClassIndex(0);
			
			tokenizer = new WordTokenizer();
			tokenizer.setDelimiters(" \r\n\t.,;:\"\'()?!-¿¡+*&#$%\\/=<>[]_`@");

			
			//coletar tokens de palavra sobre as classes como um todo.
			//True porque estamos interessados nas palavras independentemente se Upper case ou Lower case
			//quebrar os texto em tokes de acordo com uma série de regras.
			//keep para incluir o focabulário completo
			stringToWordVectorFiltro = new StringToWordVector();
			stringToWordVectorFiltro.setDoNotOperateOnPerClassBasis(true);
			stringToWordVectorFiltro.setLowerCaseTokens(true);
			stringToWordVectorFiltro.setTokenizer(tokenizer);
			stringToWordVectorFiltro.setWordsToKeep(10000000);
			
			AttributeSelection attributeSelectionFiltro = new AttributeSelection();
			attributeSelectionFiltro.setEvaluator(new InfoGainAttributeEval());
			Ranker rankerSearch = new Ranker();
			rankerSearch.setThreshold(0.0);
			attributeSelectionFiltro.setSearch(rankerSearch);
			
			Filter[] filtros = new Filter[2];
			filtros[0] = stringToWordVectorFiltro;
			filtros[1] = attributeSelectionFiltro;
			
			MultiFilter multiFiltro = new MultiFilter();
			
			classificador = new FilteredClassifier();
			multiFiltro.setFilters(filtros);
			classificador.setFilter(multiFiltro);
			classificador.setClassifier(new SMO());	
						
			Evaluation avaliador = new Evaluation(arqTreino);
			avaliador.crossValidateModel(classificador, arqTreino, 4, new Random(1));
			System.out.println(avaliador.toSummaryString());
			System.out.println(avaliador.toClassDetailsString());
			System.out.println(avaliador.toMatrixString());
			System.out.println("===== Avaliação foi realizada. =====");
		}
		catch (Exception e) {
			System.out.println("Ocorreu problema durante a avaliação.");
			System.out.println(e.toString());
		}
	}
	
	public void treinar() {
		try {
			arqTreino.setClassIndex(0);
			
			tokenizer = new WordTokenizer();
			tokenizer.setDelimiters(" \r\n\t.,;:\"\'()?!-¿¡+*&#$%\\/=<>[]_`@");

			stringToWordVectorFiltro = new StringToWordVector();
			stringToWordVectorFiltro.setDoNotOperateOnPerClassBasis(true);
			stringToWordVectorFiltro.setLowerCaseTokens(true);
			stringToWordVectorFiltro.setTokenizer(tokenizer);
			stringToWordVectorFiltro.setWordsToKeep(10000000);
			
			AttributeSelection attributeSelectionFiltro = new AttributeSelection();
			attributeSelectionFiltro.setEvaluator(new InfoGainAttributeEval());
			Ranker rankerSearch = new Ranker();
			rankerSearch.setThreshold(0.0);
			attributeSelectionFiltro.setSearch(rankerSearch);
			
			Filter[] filtros = new Filter[2];
			filtros[0] = stringToWordVectorFiltro;
			filtros[1] = attributeSelectionFiltro;
			
			MultiFilter multiFiltro = new MultiFilter();
			
			classificador = new FilteredClassifier();
			multiFiltro.setFilters(filtros);
			classificador.setFilter(multiFiltro);
			classificador.setClassifier(new SMO());	
			
			classificador.buildClassifier(arqTreino);
			
			System.out.println("===== Treinamento nos dados filtrados (treinamento) foi feito. =====");
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Ocorreu problema durante o treinamento");
		}
	}
	
    public void salvarModeloClassificador(String nomeClassificador) {
        try {
		    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nomeClassificador));
		    out.writeObject(classificador);
		    out.close();
            System.out.println("===== Modelo Classificador salvo: " + nomeClassificador + " =====");
        } 
        catch (IOException e) {
        	System.out.println("Ocorreu problema para salvar o modelo: " + nomeClassificador);
        	System.out.println(e.toString());
        }
    }


}