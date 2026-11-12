package br.edu.unicesumar.descartepoint.model;
public final class Administrador extends Usuario {
    public Administrador(int id,String nome,String email){super(id,nome,email);}
    @Override public String getPermissao(){return "GERENCIAR_PONTOS";}
}