package dw.pagamentosfut.repository;

//Repositório que encapsula todos os métodos pra persistir e recuperar as classes dentro de um banco de dados
import org.springframework.data.jpa.repository.JpaRepository;
import dw.pagamentosfut.model.Pagamento;
import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
    //Pesquisa basica = find
    //Pesquisa basica por um atributo específico = findByAtributo

    //Faz uma busca pelos pagamentos >= do valor passado
    List<Pagamento> findByValorGreaterThanEqual(double valor);

    //Faz uma busca pelos pagamentos do ano passado por parametro
    List<Pagamento> findByAnoIs(short ano);

    //Faz uma busca pelos pagamentos do jogador
    //List<Pagamento> findByCod_JogadorContaining(int cod_jogador);
}