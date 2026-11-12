package br.edu.unicesumar.descartepoint;
import br.edu.unicesumar.descartepoint.dao.*;
import br.edu.unicesumar.descartepoint.model.*;
import br.edu.unicesumar.descartepoint.util.Conexao;
import java.sql.SQLException;
import java.util.*;
public final class Main {
    private static final Scanner IN=new Scanner(System.in);
    private static final PontoDescarteDAO PONTOS=new PontoDescarteDAO();
    private static final ResiduoDAO RESIDUOS=new ResiduoDAO();
    private static final Usuario OPERADOR=new Administrador(0,"Operador local","operador@local");
    public static void main(String[] args){
        System.out.println("=== DESCARTE POINT | ODS 12 ===");
        try(var ignored=Conexao.abrir()){System.out.println("MySQL conectado com sucesso.");}
        catch(SQLException e){System.err.println("Falha ao conectar ao MySQL: "+e.getMessage());System.err.println("Configure DB_URL, DB_USER e DB_PASSWORD e execute database/schema.sql.");return;}
        while(true){
            System.out.println("\n1 Cadastrar ponto | 2 Listar pontos | 3 Atualizar ponto | 4 Excluir ponto");
            System.out.println("5 Cadastrar resíduo | 6 Listar resíduos | 7 Associar resíduo | 8 Remover associação");
            System.out.println("9 Consultar pontos por resíduo | 10 Detalhes do ponto | 0 Sair");
            int op=numero("Opção: ");
            if(op==0){System.out.println("Até logo!");return;}
            try{switch(op){
                case 1 -> criar();case 2 -> listar();case 3 -> atualizar();case 4 -> excluir();
                case 5 -> criarResiduo();case 6 -> listarResiduos();case 7 -> associar(true);case 8 -> associar(false);
                case 9 -> consultar();case 10 -> detalhes();default -> System.out.println("Opção inválida.");
            }}catch(SQLException e){System.out.println("Erro de banco de dados: "+e.getMessage();}
        }
    }
    private static String texto(String p){String s;do{System.out.print(p);s=IN.nextLine().trim();if(s.isEmpty())System.out.println("Campo obrigatório.");}while(s.isEmpty());return s;}
    private static int numero(String p){while(true){try{return Integer.parseInt(texto(p));}catch(NumberFormatException e){System.out.println("Digite um número inteiro válido.");}}}
    private static Localizacao local(){return new Localizacao(0,texto("Logradouro: "),texto("Número: "),texto("Bairro: "),texto("Cidade: "),texto("CEP: "));}
    private static void exigirGestao(){if(!"GERENCIAR_PONTOS".equals(OPERADOR.getPermissao()))throw new SecurityException("Sem permissão");}
    private static void criar()throws SQLException{exigirGestao();String nome=texto("Nome do ponto: "),horario=texto("Horário de funcionamento: ");int id=PONTOS.criar(nome,horario,local());System.out.println("Ponto cadastrado com ID "+id);}
    private static void imprimir(PontoDescarte p){System.out.printf("[%d] %s | %s | %s%n",p.id(),p.nome(),p.horarioFuncionamento(),p.localizacao());}
    private static void listar()throws SQLException{var todos=PONTOS.listar();if(todos.isEmpty())System.out.println("Nenhum ponto cadastrado.");todos.forEach(Main::imprimir);}
    private static void atualizar()throws SQLException{exigirGestao();int id=numero("ID do ponto: ");if(PONTOS.buscar(id).isEmpty()){System.out.println("Ponto não encontrado.");return;}String nome=texto("Novo nome: "),horario=texto("Novo horário: ");System.out.println(PONTOS.atualizar(id,nome,horario,local())?"Ponto atualizado.":"Ponto não encontrado.");}
    private static void excluir()throws SQLException{exigirGestao();int id=numero("ID para excluir: ");System.out.println(PONTOS.excluir(id)?"Ponto e suas associações excluídos.":"Ponto não encontrado.");}
    private static void criarResiduo()throws SQLException{
        exigirGestao();String nome=texto("Nome do resíduo: "),desc=texto("Descrição: ");
        System.out.println("1 Eletrônico | 2 Reciclável | 3 Perigoso");int categoria=numero("Categoria: ");
        String cat=switch(categoria){case 1->"ELETRONICO";case 2->"RECICLAVEL";case 3->"PERIGOSO";default->null;};
        if(cat==null){System.out.println("Categoria inválida.");return;}
        String detalhe=texto(switch(categoria){case 1->"Tipo de equipamento: ";case 2->"Material: ";default->"Risco: ";});
        System.out.println("Resíduo cadastrado com ID "+RESIDUOS.cadastrar(nome,desc,cat,detalhe));
    }
    private static void listarResiduos()throws SQLException{var residuos=RESIDUOS.listar();if(residuos.isEmpty())System.out.println("Nenhum resíduo cadastrado.");residuos.forEach(Main::imprimirResiduo);}
    private static void imprimirResiduo(Residuo r){System.out.printf("[%d] %s | %s | %s | %s%n",r.getId(),r.getNome(),r.getCategoria(),r.getDescricao(),r.getDetalhe());}
    private static void associar(boolean adicionar)throws SQLException{exigirGestao();int ponto=numero("ID do ponto: "),residuo=numero("ID do resíduo: ");boolean ok=adicionar?PONTOS.associar(ponto,residuo):PONTOS.desassociar(ponto,residuo);System.out.println(ok?(adicionar?"Associação cadastrada.":"Associação removida."):"Nenhuma alteração (associação já existente ou não encontrada).");}
    private static void consultar()throws SQLException{int id=numero("ID do tipo de resíduo: ");var pts=PONTOS.porResiduo(id);if(pts.isEmpty())System.out.println("Nenhum ponto encontrado para o resíduo informado.");pts.forEach(Main::imprimir);}
    private static void detalhes()throws SQLException{int id=numero("ID do ponto: ");var ponto=PONTOS.buscar(id);if(ponto.isEmpty()){System.out.println("Ponto não encontrado.");return;}imprimir(ponto.get());var residuos=RESIDUOS.listarPorPonto(id);if(residuos.isEmpty())System.out.println("Nenhum resíduo associado.");residuos.forEach(Main::imprimirResiduo);}
}