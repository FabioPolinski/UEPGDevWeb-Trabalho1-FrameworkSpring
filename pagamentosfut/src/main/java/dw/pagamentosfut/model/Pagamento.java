package dw.pagamentosfut.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

//Notações
//Entidade será persistida no banco
//Indica ao banco de dados para criar um tabela com o nome pagamento
@Entity
@Table(name="pagamento")
public class Pagamento {

    //Dizer que será a chave primária com auto incremento
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cod_pagamento;

    //Colunas da tabela
    @Column
    private short ano;

    @Column
    private short mes;

    @Column 
    private double valor;
    
    @ManyToOne
    @JoinColumn(name = "cod_jogador", nullable = false)
    @JsonIgnore
    private Jogador cod_jogador;

    //Construtores
    public Pagamento() { }

    public Pagamento(short ano, short mes, double valor, Jogador cod_jogador){
        this.ano=ano;
        this.mes=mes;
        this.valor=valor;
        this.cod_jogador=cod_jogador;
    }

    //Getters e Setters
    public int getCod_pagamento(){
        return cod_pagamento;
    }

    public short getAno(){
        return ano;
    }

    public void setAno(short ano){
        this.ano = ano;
    }

    public short getMes(){
        return mes;
    }

    public void setMes(short mes){
        this.mes = mes;
    }

    public double getValor(){
        return valor;
    }

    public void setValor(double valor){
        this.valor = valor;
    }

    public Jogador getCod_jogador(){
        return cod_jogador;
    }

    public void setCod_jogador(Jogador cod_jogador){
        this.cod_jogador = cod_jogador;
    }

    @Override
    public String toString(){
        return "Pagamento [codigo = "+ cod_pagamento +", ano = "+ ano + ", mes = "+ mes + 
            ", valor = " + valor + ", codigo do jogador = " + cod_jogador;
    }
}
