package dw.pagamentosfut.repository;

//Repositório que encapsula todos os métodos pra persistir e recuperar as classes dentro de um banco de dados
import org.springframework.data.jpa.repository.JpaRepository;
import dw.pagamentosfut.model.Jogador;
import java.util.List;

public interface JogadorRepository extends JpaRepository<Jogador, Integer> {
    //Pesquisa basica = find
    //Pesquisa basica por um atributo específico = findByAtributo

    //Faz uma busca de jogador contendo o nome passado
    List<Jogador> findByNomeContaining(String nome);

    //Faz uma busca de jogador pelo email
    List<Jogador> findByEmailContaining(String email);
}