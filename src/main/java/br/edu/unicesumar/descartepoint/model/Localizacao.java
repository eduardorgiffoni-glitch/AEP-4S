package br.edu.unicesumar.descartepoint.model;
public record Localizacao(int id,String logradouro,String numero,String bairro,String cidade,String cep) {
    @Override public String toString(){return logradouro+", "+numero+" - "+bairro+", "+cidade+" - CEP "+cep;}
}