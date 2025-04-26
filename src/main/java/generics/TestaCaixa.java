package generics;

public class TestaCaixa {
    public static void main(String[] args) {
        Caixa<String> caixaDeTexto = new Caixa();
        caixaDeTexto.setConteudo("Guardando texto na minha caixa");

        Caixa<Integer> caixaDeIdade = new Caixa();
        caixaDeIdade.setConteudo(30);
        System.out.println(caixaDeIdade.somaConteudoNaCaixa(20));


        Caixa<Double> caixaDeValor = new Caixa<>();
        caixaDeValor.setConteudo(150.50);
        System.out.println(caixaDeValor.somaConteudoNaCaixa(50.00));
    }
}
