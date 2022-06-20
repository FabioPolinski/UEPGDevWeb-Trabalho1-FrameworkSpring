package dw.pagamentosfut.control;

import dw.pagamentosfut.repository.*;
import dw.pagamentosfut.model.*;

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
public class PagamentoController {
    //Precisamos colocar dentro do controlado o repository
    //Notação que faz com que consigamos fazer o framework criar um objeto e injetar no codigo
    @Autowired
    JogadorRepository repJ;

    @Autowired
    PagamentoRepository repP;

    //Criar metodos que respondam a api

    //Lista todos os pagamentos ao seguir o modelo /pagamentos (get)
    //ResponseEntity -> encapsula um objeto que será retornado a camada de visualização
    //Os métodos devem retornar uma lista de pagamentos
    //Parâmetro ano será para buscar por ano(requestparam false para ser opcional)
    @GetMapping("/pagamentos")
    public ResponseEntity<List<Pagamento>> getAllPagamentos(@RequestParam(required=false, defaultValue = "0") short ano, 
                @RequestParam(required=false, defaultValue = "0") double valor) { 
                    //@RequestParam(required=false, defaultValue = "0") int cod_jogador) {
                    //Adicionar no primeiro if cod_jogador == 0 se conseguir
        try{
                List<Pagamento> lp = new ArrayList<Pagamento>();
                
                //Se nao esta passando nome ou email, adiciona a lista e retorna todos os jogadores encontrados no banco de dados
                if(ano == 0 && valor == 0){
                    repP.findAll().forEach(lp::add);
                }
                else if (ano != 0){
                    //Se estiver passando, ache por ano e adicione a lista
                    repP.findByAnoIs(ano).forEach(lp::add);
                }
                else if (valor != 0){
                    //Se estiver passando, ache por valor >= e adicione a lista
                    repP.findByValorGreaterThanEqual(valor).forEach(lp::add);
                }
                /*else if (cod_jogador != 0){
                    //Se estiver passando, ache por jogador e adicione a lista
                    repP.findByCod_JogadorContaining(cod_jogador).forEach(lp::add);
                }*/

                //Se a busca nao retornou nada, erro de sem conteudo
                if(lp.isEmpty()){
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                else{
                    //Se retornou algo, retornamos a lista e um ok
                    return new ResponseEntity<>(lp, HttpStatus.OK);
                }
        } catch (Exception e){
            //Se nao der boa a busca, não retornamos nenhum objeto e retornamos um erro (httpstatuses.com)
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Criar pagamento (post) com o codigo do jogador
    @PostMapping("/pagamentos/add/{cod_jogador}")
    public ResponseEntity<Pagamento> createPagamento(@RequestBody Pagamento pag, @PathVariable("cod_jogador") int cod_jogador) {
        try{
            //Encontra o cod do jogador passado por parametro
            Optional<Jogador> jogador = repJ.findById(cod_jogador);

            //Se o cod existir fazemos a inserçao
            if (jogador.isPresent()){
                //Cria um objeto do tipo pagamento, que é criado pelo save
                //No save criamos um Pagamento, para termos um controle de identificação dentro do hibernate
                //Pegando seus dados
                //Com o save persistimos na base de dados, quando persistir retornamos um objeto ja com o ID criado
                Pagamento _p = repP.save(new Pagamento(pag.getAno(), pag.getMes(), pag.getValor(), jogador.get()));

                //Retorna o pagamento criado e um status de criado
                return new ResponseEntity<>(_p, HttpStatus.CREATED);
            }
            else{
                //Caso nao existe not found
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            //Se nao der boa a criaçao, não retornamos nenhum objeto e retornamos um erro (httpstatuses.com)
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    //Listar Pagamento por cod (get)
    //Usuario passa o cod no link
    @GetMapping("/pagamentos/{cod_pagamento}")
    public ResponseEntity<Pagamento> getPagamentoById(@PathVariable("cod_pagamento") int cod_pagamento) {
        //Optional outra opçao para List
        //Faz a busca e passa o cod caso ele ache
        Optional<Pagamento> data = repP.findById(cod_pagamento);

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
    @PutMapping("/pagamentos/{cod_pagamento}")
    public ResponseEntity<Pagamento> updatePagamento(@PathVariable("cod_pagamento") int cod_pagamento, @RequestBody Pagamento p) {
        //Obter o cod do banco
        Optional<Pagamento> data = repP.findById(cod_pagamento);
    
        //Se existir altera o atributos dele e os do pagamento que passamos por parametro
    
        if(data.isPresent()){
        
            Pagamento pag = data.get();
            pag.setAno(p.getAno());
            pag.setMes(p.getMes());
            pag.setValor(p.getValor());
            //pag.setCod_jogador(p.getCod_jogador());
    
            //Salva o objeto e retorna OK
            return new ResponseEntity<>(repP.save(pag), HttpStatus.OK);
        }
        else{
            //Caso nao exista retorna NOTFOUND
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Deletar pagamento por um cod
    @DeleteMapping("/pagamentos/{cod_pagamento}")
    public ResponseEntity<HttpStatus> deletePagamento(@PathVariable("cod_pagamento") int cod_pagamento) {
        try{
            //Chama o metodo e deleta o cod
            repP.deleteById(cod_pagamento);
            //Retorna q esta sem conteudo
            return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
        } catch(Exception e){
            //Se n existir o objeto retorna erro
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Deletar todos os pagamentos
    @DeleteMapping("/pagamentos")
    public ResponseEntity<HttpStatus> deleteAllPagamentos() {
        try{
            //Chama o metodo e deleta todos
            repP.deleteAll();
            //Retorna q esta sem conteudo
            return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
        } catch(Exception e){
            //Se n existir o objeto retorna erro
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}