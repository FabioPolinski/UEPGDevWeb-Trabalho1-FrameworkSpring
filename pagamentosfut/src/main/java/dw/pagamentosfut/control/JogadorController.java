package dw.pagamentosfut.control;

import dw.pagamentosfut.repository.JogadorRepository;
import dw.pagamentosfut.model.Jogador;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localost:8080")

//Notação de controlador e todos que seguirem o modelo /api serão destinado para cá
@RestController
@RequestMapping("/api")
public class JogadorController {
    //Precisamos colocar dentro do controlado o repository
    //Notação que faz com que consigamos fazer o framework criar um objeto e injetar no codigo
    @Autowired
    JogadorRepository rep;

    //Criar metodos que respondam a api

    //Lista todos os jogadores ao seguir o modelo /jogadores (get)
    //ResponseEntity -> encapsula um objeto que será retornado a camada de visualização
    //Os métodos devem retornar uma lista de jogadores
    //Parâmetro nome será para buscar por nome(requestparam false para ser opcional)
    @GetMapping("/jogadores")
    public ResponseEntity<List<Jogador>> getAllJogadores(@RequestParam(required=false) String nome, 
                @RequestParam(required=false) String email) {
        try{
                List<Jogador> lj = new ArrayList<Jogador>();
                
                //Se nao esta passando nome ou email, adiciona a lista e retorna todos os jogadores encontrados no banco de dados
                if(nome == null && email == null){
                    rep.findAll().forEach(lj::add);
                }
                else if(nome != null){
                    //Se estiver passando, ache por nome e adicione a lista
                    rep.findByNomeContaining(nome).forEach(lj::add);
                }
                else if(email != null){
                    //Se estiver passando, ache por email e adicione a lista
                    rep.findByEmailContaining(email).forEach(lj::add);
                }

                //Se a busca nao retornou nada, erro de sem conteudo
                if(lj.isEmpty()){
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                else{
                    //Se retornou algo, retornamos a lista e um ok
                    return new ResponseEntity<>(lj, HttpStatus.OK);
                }
        } catch (Exception e){
            //Se nao der boa a busca, não retornamos nenhum objeto e retornamos um erro (httpstatuses.com)
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Criar jogador (post)
    @PostMapping("/jogadores")
    public ResponseEntity<Jogador> createJogador(@RequestBody Jogador jog) {
        try{
            //Cria um objeto do tipo jogador, que é criado pelo save
            //No save criamos um Jogador, para termos um controle de identificação dentro do hibernate
            //Pegando seus dados
            //Com o save persistimos na base de dados, quando persistir retornamos um objeto ja com o ID criado
            Jogador _j = rep.save(new Jogador(jog.getNome(), jog.getEmail(), jog.getDatanasc()));

            //Retorna o jogador criado e um status de criado
            return new ResponseEntity<>(_j, HttpStatus.CREATED);

        } catch (Exception e){
            //Se nao der boa a criaçao, não retornamos nenhum objeto e retornamos um erro (httpstatuses.com)
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    //Listar Jogador por cod (get)
    //Usuario passa o cod no link
    @GetMapping("/jogadores/{cod_jogador}")
    public ResponseEntity<Jogador> getJogadorById(@PathVariable("cod_jogador") int cod_jogador) {
        //Optional outra opçao para List
        //Faz a busca e passa o cod caso ele ache
        Optional<Jogador> data = rep.findById(cod_jogador);

        //Se ele achou o cod
        if(data.isPresent()){
            //Retornamos o cod achado e retorna ok
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        }
        else{
            //Se nao achou retorna um not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Atualizar por cod (put)
    @PutMapping("/jogadores/{cod_jogador}")
    public ResponseEntity<Jogador> updateJogador(@PathVariable("cod_jogador") int cod_jogador, @RequestBody Jogador j) {
        //Obter o cod do banco
        Optional<Jogador> data = rep.findById(cod_jogador);
    
        //Se existir altera o atributos dele e os do jogador que passamos por parametro
    
        if(data.isPresent()){
        
            Jogador jog = data.get();
            jog.setNome(j.getNome());
            jog.setEmail(j.getEmail());
            jog.setDatanasc(j.getDatanasc());
    
            //Salva o objeto e retorna OK
            return new ResponseEntity<>(rep.save(jog), HttpStatus.OK);
        }
        else{
            //Caso nao exista retorna NOTFOUND
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Deletar jogador por um cod
    @DeleteMapping("/jogadores/{cod_jogador}")
    public ResponseEntity<HttpStatus> deleteJogador(@PathVariable("cod_jogador") int cod_jogador) {
        try{
            //Chama o metodo e deleta o cod
            rep.deleteById(cod_jogador);
            //Retorna q esta sem conteudo
            return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
        } catch(Exception e){
            //Se n existir o objeto retorna erro
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Deletar todos os jogadores
    @DeleteMapping("/jogadores")
    public ResponseEntity<HttpStatus> deleteAllJogadores() {
        try{
            //Chama o metodo e deleta todos
            rep.deleteAll();
            //Retorna q esta sem conteudo
            return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
        } catch(Exception e){
            //Se n existir o objeto retorna erro
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}