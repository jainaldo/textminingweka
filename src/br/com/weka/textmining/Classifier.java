package br.com.weka.textmining;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;

import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;


public class Classifier {
	/**
	 * Armazena o texto e o modelo
	 */
	String texto;
	String modelo;
	/**
	 * Armazena as instancias.
	 */
	Instances instances;
	/**
	 * Classificador.
	 */
	FilteredClassifier classificador;
		
	/**
	 * Metodo que carrega o texto que será classificado
	 * 
	 */
	
	public void carregarArqTexto(String nomeArq) {
		try {
		BufferedReader leitor = new BufferedReader(new FileReader(nomeArq));
			String linha;
			texto = "";
			while ((linha = leitor.readLine()) != null) {
                texto = texto + " " + linha;
            }
			System.out.println("===== Arquivo foi carregado: " + nomeArq + " =====");
			leitor.close();
			System.out.println(texto);
		}
		catch (IOException e) {
			System.out.println("Ocorreu problema durante a leitura:" + nomeArq);
		}
	}
	
	
	/**
	 * Metodo que carrega o modelo que será usado para classificar.
	 * 
	 */
	public void carregarModeloClassificador(String nomeModeloClassificador) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(nomeModeloClassificador));
            Object tmp = in.readObject();
			classificador = (FilteredClassifier) tmp;
            in.close();
 			System.out.println("===== Modelo foi carregado: " + nomeModeloClassificador + " =====");
       } 
		catch (Exception e) {
			System.out.println("Ocorreu problema durante a leitura:" + nomeModeloClassificador);
		}
	}
	
	/**
	 * This method creates the instance to be classified, from the text that has been read.
	 */
	public void criarInstanciaArff() {
	
		// Criando header
		FastVector listaAtributos = new FastVector(2);
		
		// Criando primeiro atributo, a classe
		FastVector valores = new FastVector(3);
		valores.addElement("EN"); 
		valores.addElement("FR"); 
		valores.addElement("SP"); 
		Attribute attributo1 = new Attribute("language_class", valores);
		listaAtributos.addElement(attributo1);
		
		// Criando segundo atributo, o texto
		Attribute attributo2 = new Attribute("text",(FastVector) null);
		listaAtributos.addElement(attributo2);
		
		// Contruindo a instancia e definido com uma instancia.
		instances = new Instances("Test relation",listaAtributos, 1);           
		// Set indice de classe
		instances.setClassIndex(0);

		// Criando e adicionado a instancia
		Instance instance = new Instance(2);	
		instance.setDataset(instances);
		instance.setValue(attributo2, texto);
		instances.add(instance);
		
 		System.out.println("===== Instância criada em formato arff =====");
		System.out.println(instances);
	}

	public void classificar() {
		try {
			double previsto = classificador.classifyInstance(instances.instance(0));

			System.out.println("===== Classificando instância =====");
			System.out.println("Class prevista foi: " + instances.classAttribute().value((int) previsto));
		}
		catch (Exception e) {
			System.out.println("Ocorreu problema ao classificar o texto");
			System.out.println(e.toString());
		}		
	}
}
