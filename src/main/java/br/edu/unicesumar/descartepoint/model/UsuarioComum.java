package br.edu.unicesumar.descartepoint.model;
public final class UsuarioComum extends Usuario {
    public UsuarioComum(int id,String nome,String email){super(id,nome,email);}
    @Override public String getPermissao(){return "CONSULTAR_PONTOS";}
}