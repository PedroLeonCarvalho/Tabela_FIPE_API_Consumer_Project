package br.com.alura.TabelaFipe.principal;

import br.com.alura.TabelaFipe.model.Dados;
import br.com.alura.TabelaFipe.model.Modelos;
import br.com.alura.TabelaFipe.model.Veiculo;
import br.com.alura.TabelaFipe.service.ConsumoApi;
import br.com.alura.TabelaFipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {


    private Scanner leitura = new Scanner(System.in);
    private ConverteDados conversor = new ConverteDados();
    String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    private ConsumoApi consumo= new ConsumoApi();
    public void exibeMenu () {

        var menu = " ***OPÇÕEs***" +
                "Carro" +
                "Moto" +
                "Caminhão" +
                "Digite uma das opções para consultar:";
        System.out.println(menu);

        var opcao = leitura.nextLine();

        String endereco;

        if (opcao.toLowerCase().contains("car")) {
            endereco = URL_BASE + "carros/marcas";

        } else if (opcao.toLowerCase().contains("mot")) {
            endereco = URL_BASE + "motos/marcas";

        }else endereco = URL_BASE + "caminhoes/marcas";

var json = consumo.obterDados(endereco);
        System.out.println(json);
        var marcas = conversor.obterLista(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Informe a marca para consulta:");

        var codigoMarca = leitura.nextLine();
        endereco = endereco + "/" + codigoMarca + "/modelos";
        json = consumo.obterDados(endereco);
        var modeloLista = conversor.obterDados(json, Modelos.class);
        System.out.println("\nModelos desta marca:");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("\n Informe um trecho do nome do modelo desejado:");
        var nomeVeiculo = leitura.next();
        List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                .filter(m->m.nome().toLowerCase()
                        .contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("Modelos filtrados:");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("Digite o código do modelo desejado");
        var codigoModelo = leitura.next();
        endereco = endereco + "/" +codigoModelo + "/anos/";

        //agora quero que mostre anos com valores.

        json = consumo.obterDados(endereco);
        List <Dados> anos = conversor.obterLista(json, Dados.class);
        List <Veiculo> veiculos = new ArrayList<>();
        for (int i =0; i<anos.size(); i++) {
            var enderecoAnos = endereco+ anos.get(i).codigo();
            json = consumo.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
            System.out.println("Veiculos encontrados:");
            veiculos.forEach(System.out::println);
        }

       }}
