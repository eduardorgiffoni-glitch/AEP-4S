package br.edu.unicesumar.descartepoint.dao;
import br.edu.unicesumar.descartepoint.model.*;
import br.edu.unicesumar.descartepoint.util.Conexao;
import java.sql.*;
import java.util.*;
public class ResiduoDAO {
    private static Residuo mapear(ResultSet rs) throws SQLException {
        int id=rs.getInt("id"); String nome=rs.getString("nome"),descricao=rs.getString("descricao"),cat=rs.getString("categoria"),detalhe=rs.getString("detalhe");
        return switch(cat){
            case "ELETRONICO" -> new ResiduoEletronico(id,nome,descricao,detalhe);
            case "RECICLAVEL" -> new ResiduoReciclavel(id,nome,descricao,detalhe);
            case "PERIGOSO" -> new ResiduoPerigoso(id,nome,descricao,detalhe);
            default -> throw new SQLException("Categoria desconhecida: "+cat);
        };
    }
    public int cadastrar(String nome,String descricao,String categoria,String detalhe) throws SQLException {
        try(Connection c=Conexao.abrir()) {
            c.setAutoCommit(false);
            try {
                int id;
                try(PreparedStatement p=c.prepareStatement("INSERT INTO residuos(nome,descricao,categoria) VALUES (?,?,?)",Statement.RETURN_GENERATED_KEYS)) {
                    p.setString(1,nome);p.setString(2,descricao);p.setString(3,categoria);p.executeUpdate();
                    try(ResultSet keys=p.getGeneratedKeys()){ if(!keys.next())throw new SQLException("ID de resíduo ausente");id=keys.getInt(1); }
                }
                String tabela=switch(categoria){case "ELETRONICO"->"residuos_eletronicos";case "RECICLAVEL"->"residuos_reciclaveis";case "PERIGOSO"->"residuos_perigosos";default->throw new SQLException("Categoria inválida");};
                String coluna=switch(categoria){case "ELETRONICO"->"tipo_equipamento";case "RECICLAVEL"->"material";default->"risco";};
                try(PreparedStatement p=c.prepareStatement("INSERT INTO "+tabela+"(id,"+coluna+") VALUES (?,?)")) {p.setInt(1,id);p.setString(2,detalhe);p.executeUpdate();}
                c.commit();return id;
            } catch(SQLException|RuntimeException e){c.rollback();throw e;} finally{c.setAutoCommit(true);}
        }
    }
    private static final String SELECT="SELECT r.id,r.nome,r.descricao,r.categoria, COALESCE(e.tipo_equipamento,rc.material,p.risco) AS detalhe FROM residuos r LEFT JOIN residuos_eletronicos e ON e.id=r.id LEFT JOIN residuos_reciclaveis rc ON rc.id=r.id LEFT JOIN residuos_perigosos p ON p.id=r.id";
    public List<Residuo> listar() throws SQLException {
        List<Residuo> lista=new ArrayList<>();
        try(Connection c=Conexao.abrir();PreparedStatement p=c.prepareStatement(SELECT+" ORDER BY r.nome");ResultSet rs=p.executeQuery()) {while(rs.next())lista.add(mapear(rs));}
        return lista;
    }
    public List<Residuo> listarPorPonto(int pontoId) throws SQLException {
        List<Residuo> lista=new ArrayList<>();
        try(Connection c=Conexao.abrir();PreparedStatement p=c.prepareStatement(SELECT+" INNER JOIN ponto_residuo pr ON pr.residuo_id=r.id WHERE pr.ponto_id=? ORDER BY r.nome")) {
            p.setInt(1,pontoId);try(ResultSet rs=p.executeQuery()){while(rs.next())lista.add(mapear(rs));}
        } return lista;
    }
}