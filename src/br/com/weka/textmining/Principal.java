package br.com.weka.textmining;

public class Principal {

	public static void main(String[] args) {
		
		// Treinamento
		Learner treino = new Learner();
		treino.carregarArqTreino(" arquivo de treinamento em formanto arff");
		treino.avaliar();
		treino.treinar();
		treino.salvarModeloClassificador("nome do arquivo de modelo classificador");
		
		//classificando texto
		Classifier classificador;
		classificador = new Classifier();
		classificador.carregarArqTexto("arquivo de texto");
		classificador.carregarModeloClassificador("arquivo do modelo classificador");
		classificador.criarInstanciaArff(); 
		classificador.classificar();
	}

}
