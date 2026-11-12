package br.edu.unicesumar.descartepoint.model;
public final class ResiduoEletronico implements Residuo {
    private final int id;
    private final String nome, descricao, tipoEquipamento;
    public ResiduoEletronico(int id,String nome,String descricao,String detalhe){this.id=id;this.nome=nome;this.descricao=descricao;this.tipoEquipamento=detalhe;}
    @Override public int getId(){return id;}
    @Override public String getNome(){return nome;}
    @Override public String getDescricao(){return descricao;}
    @Override public String getCategoria(){return "ELETRONICO";}
    @Override public String getDetalhe(){return "Equipamento: "+tipoEquipamento;}
}