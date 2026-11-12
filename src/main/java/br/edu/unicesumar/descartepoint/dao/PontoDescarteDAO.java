package br.edu.unicesumar.descartepoint.dao;
import br.edu.unicesumar.descartepoint.model.*;
import br.edu.unicesumar.descartepoint.util.Conexao;
import java.sql.*;
import java.util.*;
public class PontoDescarteDAO {
    private static final String SELECT="SELECT p.id,p.nome,p.horario_funcionamento,l.id AS local_id,l.logradouro,l.numero,l.bairro,l.cidade,l.cep FROM pontos_descarte p JOIN localizacoes l ON l.id=p.localizacao_id";
    private static PontoDescarte map(ResultSet r) throws SQLException {
        return new PontoDescarte(r.getInt("id"),r.getString("nome"),r.getString("horario_funcionamento"),new Localizacao(r.getInt("local_id"),r.getString("logradouro"),r.getString("numero"),r.getString("bairro"),r.getString("cidade"),r.getString("cep")));
    }
    public int criar(String nome,String horario,Localizacao l) throws SQLException {
        try(Connection c=Conexao.abrir()){c.setAutoCommit(false);
            try {
                int idLocal,idPonto;
                try(PreparedStatement p=c.prepareStatement("INSERT INTO localizacoes(logradouro,numero,bairro,cidade,cep) VALUES (?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS)) {
                    preencherLocal(p,l,1);p.executeUpdate();try(ResultSet rs=p.getGeneratedKeys()){if(!rs.next())throw new SQLException("ID de localização ausente");idLocal=rs.getInt(1);}
                }
                try(PreparedStatement p=c.prepareStatement("INSERT INTO pontos_descarte(nome,horario_funcionamento,localizacao_id) VALUES (?,?,?)",Statement.RETURN_GENERATED_KEYS)){
                    p.setString(1,nome);p.setString(2,horario);p.setInt(3,idLocal);p.executeUpdate();try(ResultSet rs=p.getGeneratedKeys()){if(!rs.next())throw new SQLException("ID de ponto ausente");idPonto=rs.getInt(1);}
                }
                c.commit();return idPonto;
            }catch(SQLException|RuntimeException e){c.rollback();throw e;}finally{c.setAutoCommit(true);}
        }
    }
    private static void preencherLocal(PreparedStatement p,Localizacao l,int inicio) throws SQLException {
        p.setString(inicio,l.logradouro());p.setString(inicio+1,l.numero());p.setString(inicio+2,l.bairro());p.setString(inicio+3,l.cidade());p.setString(inicio+4,l.cep());
    }
    public List<PontoDescarte> listar() throws SQLException {
        List<PontoDescarte> out=new ArrayList<>();try(Connection c=Conexao.abrir();PreparedStatement p=c.prepareStatement(SELECT+" ORDER BY p.nome");ResultSet r=p.executeQuery()){while(r.next())out.add(map(r));}return out;
    }
    public Optional<PontoDescarte> buscar(int id) throws SQLException {
        try(Connection c=Conexao.abrir();PreparedStatement p=c.prepareStatement(SELECT+" WHERE p.id=?")){p.setInt(1,id);try(ResultSet r=p.executeQuery()){return r.next()?Optional.of(map(r)):Optional.empty();}}
    }
    public List<PontoDescarte> porResiduo(int residuoId) throws SQLException {
        List<PontoDescarte> out=new ArrayList<>();
        try(Connection c=Conexao.abrir();PreparedStatement p=c.prepareStatement(SELECT+" JOIN ponto_residuo pr ON pr.ponto_id=p.id WHERE pr.residuo_id=? ORDER BY p.nome")){
            p.setInt(1,residuoId);try(ResultSet r=p.executeQuery()){while(r.next())out.add(map(r));}
        }return out;
    }
    public boolean atualizar(int id,String nome,String horario,Localizacao l) throws SQLException {
        try(Connection c=Conexao.abrir()){c.setAutoCommit(false);
            try{
                Integer locId=null;
                try(PreparedStatement p=c.prepareStatement("SELECT localizacao_id FROM pontos_descarte WHERE id=? FOR UPDATE")){p.setInt(1,id);try(ResultSet r=p.executeQuery()){if(r.next())locId=r.getInt(1);}}
                if(locId==null){c.rollback();return false;}
                try(PreparedStatement p=c.prepareStatement("UPDATE localizacoes SET logradouro=?,numero=?,bairro=?,cidade=?,cep=? WHERE id=?")){preencherLocal(p,l,1);p.setInt(6,locId);p.executeUpdate();}
                try(PreparedStatement p=c.prepareStatement("UPDATE pontos_descarte SET nome=?,horario_funcionamento=? WHERE id=?")){p.setString(1,nome);p.setString(2,horario);p.setInt(3,id);p.executeUpdate();}
                c.commit();return true;
            }catch(SQLException|RuntimeException e){c.rollback();throw e;}finally{c.setAutoCommit(true);}
        }
    }
    public boolean excluir(int id) throws SQLException {
        try(Connection c=Conexao.abrir()){c.setAutoCommit(false);
            try{
                Integer locId=null;
                try(PreparedStatement p=c.prepareStatement("SELECT localizacao_id FROM pontos_descarte WHERE id=? FOR UPDATE")){p.setInt(1,id);try(ResultSet r=p.executeQuery()){if(r.next())locId=r.getInt(1);}}
                if(locId==null){c.rollback();return false;}
                try(PreparedStatement p=c.prepareStatement("DELETE FROM pontos_descarte WHERE id=?")){p.setInt(1,id);p.executeUpdate();}
                try(PreparedStatement p=c.prepareStatement("DELETE FROM localizacoes WHERE id=?")){p.setInt(1,locId);p.executeUpdate();}
                c.commit();return true;
            }catch(SQLException|RuntimeException e){c.rollback();throw e;}finally{c.setAutoCommit(true);}
        }
    }
    public boolean associar(int pontoId,int residuoId) throws SQLException {
        try(Connection c=Conexao.abrir();PreparedStatement p=c.prepareStatement("INSERT IGNORE INTO ponto_residuo(ponto_id,residuo_id) VALUES (?,?)")){
            p.setInt(1,pontoId);p.setInt(2,residuoId);return p.executeUpdate()>0;
        }
    }
    public boolean desassociar(int pontoId,int residuoId) throws SQLException {
        try(Connection c=Conexao.abrir();PreparedStatement p=c.prepareStatement("DELETE FROM ponto_residuo WHERE ponto_id=? AND residuo_id=?")){
            p.setInt(1,pontoId);p.setInt(2,residuoId);return p.executeUpdate()>0;
        }
    }
}