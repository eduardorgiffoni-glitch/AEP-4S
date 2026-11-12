package br.edu.unicesumar.descartepoint.model;
public final class ResiduoPerigoso implements Residuo {
    private final int id;
    private final String nome, descricao, risco;
    public ResiduoPerigoso(int id,String nome,String descricao,String detalhe){this.id=id;this.nome=nome;this.descricao=descricao;this.risco=detalhe;}
    @Override public int getId(){return id;}
    @Override public String getNome(){return nome;}
    @Override public String getDescricao(){return descricao;}
    @Override public String getCategoria(){return "PERIGOSO";}
    @Override public String getDetalhe(){return "Risco: "+risco;}
}