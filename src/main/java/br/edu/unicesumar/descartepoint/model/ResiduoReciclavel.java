package br.edu.unicesumar.descartepoint.model;
public final class ResiduoReciclavel implements Residuo {
    private final int id;
    private final String nome, descricao, material;
    public ResiduoReciclavel(int id,String nome,String descricao,String detalhe){this.id=id;this.nome=nome;this.descricao=descricao;this.material=detalhe;}
    @Override public int getId(){return id;}
    @Override public String getNome(){return nome;}
    @Override public String getDescricao(){return descricao;}
    @Override public String getCategoria(){return "RECICLAVEL";}
    @Override public String getDetalhe(){return "Material: "+material;}
}