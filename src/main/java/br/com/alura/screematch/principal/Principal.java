package br.com.alura.screematch.principal;

import br.com.alura.screematch.model.*;
import br.com.alura.screematch.service.ConsumoAPI;
import br.com.alura.screematch.service.ConverteDados;

import javax.crypto.spec.PSource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();


    public void exibirMenu(){
        System.out.println("Digite o nome da série para a busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        //System.out.println(json);

           DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
           System.out.println(dados);


           List<DadosTemporada> temporadas = new ArrayList<>();

           for(int i = 1; i<=dados.totalTemporadas(); i++) {
               json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") +"&season=" + i + API_KEY);
               DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
               temporadas.add(dadosTemporada);
           }
        System.out.println("Imprimindo todos os dados de todas temporadas e episodio");
        System.out.println(temporadas);
        temporadas.forEach(System.out::println);


   /*     System.out.println("Imprimindo todos os episodios de todas as temporadas com codigo abrassal");

        for(int i =0; i < dados.totalTemporadas(); i++) {
            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodio();
            for(int j = 0; j<episodiosTemporada.size(); j++){
                System.out.println(episodiosTemporada.get(j).titulo());
            }
        }*/

        System.out.println("Imprimindo todos os episodios de todas as temporadas com forEach");
        temporadas.forEach(t -> t.episodio().forEach(e -> System.out.println(e.titulo())));




     /*   List<String> nomes = Arrays.asList("Jacque", "Iasmin", "Paulo", "Rodrigo", "Nico");

        nomes.stream()
                .sorted()
                .limit(3)
                .filter(n -> n.startsWith("N"))
                .map(n -> n.toUpperCase())
                .forEach(System.out::println);*/

       List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodio().stream())
                .collect(Collectors.toList()); //uma série possui uma lista de temporadas, onde cada temporada tem uma lista de episódios. Então, teríamos que percorrer uma lista dentro da outra.

        System.out.println("\n top 10 episódios");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao))
                .limit(10)
                .map(e -> e.titulo().toUpperCase() + " + " + e.avaliacao())
                .forEach(System.out::println);


        System.out.println("\n criando uma lista de episodios");


        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodio().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());


        System.out.println("Imprimindo todos os episodios de todas as temporadas com forEach");

        episodios.forEach(System.out::println);

        System.out.println("Digite um trecho do titulo do episodio: ");
        var trechoTitulo = leitura.nextLine();
        Optional<Episodio> ep = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();

        if(ep.isPresent()){
            System.out.println("Episódio encontrado!");
            System.out.println("Temporada: " + ep.get().getTemporada());
        } else {
            System.out.println("Episódio não encontrado!");
        }

       System.out.println("A partir de que ano você deseja ver os episódios");

        var ano = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1 , 1);

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                " Episódio: " + e.getTitulo() +
                                " Data lançamento: " + e.getDataLancamento().format(formatador)
                ));

      /*  Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter( e -> e.getAvaliacao() >0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("Média: " + est.getAverage());
        System.out.println("Melhor episódio: " + est.getMax());
        System.out.println("Pior episódio: " + est.getMin());
        System.out.println("Quantidade: " + est.getCount());
*/



    }
}

//https://www.omdbapi.com/?t=friends&season=1&apikey=6585022c