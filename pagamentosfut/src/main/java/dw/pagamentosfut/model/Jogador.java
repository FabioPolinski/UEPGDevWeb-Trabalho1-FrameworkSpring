package dw.pagamentosfut.model;

import java.util.Date;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

//Notações
//Entidade será persistida no banco
//Indica ao banco de dados para criar um tabela com o nome jogador
@Entity
@Table(name="jogador")
public class Jogador {

    //Dizer que será a chave primária com auto incremento
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cod_jogador;

    //Colunas da tabela
    @Column(length = 60)
    private String nome;

    @Column(length = 60)
    private String email;

    @Column
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date datanasc;

    @OneToMany(mappedBy = "cod_jogador")
    private List<Pagamento> pagamentos;

    //Contrutores
    public Jogador() { }

    public Jogador(String nome, String email, Date datanasc){
        this.nome=nome;
        this.email=email;
        this.datanasc=datanasc;
    }

    //Get e seters
    public int getCod_jogador(){
        return cod_jogador;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome=nome;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public Date getDatanasc(){
        return datanasc;
    }

    public void setDatanasc(Date datanasc){
        this.datanasc=datanasc;
    }

    public List<Pagamento> getPagamentos(){
        return pagamentos;
    }

    @Override
    public String toString(){
        return "Jogador [codigo = "+ cod_jogador +", nome = "+ nome + ", email = "+ email + 
            ", data de nasc. = " + datanasc;
    }
}
