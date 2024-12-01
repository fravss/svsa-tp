package com.teste.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.teste.model.enums.StatusOcorrencia;
import com.teste.model.enums.TipoOcorrencia;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity
@Table(name="ocorrencia")
@NamedQueries({
	@NamedQuery(name="Ocorrencia.buscarTodos", query="select o from Ocorrencia o"),
    @NamedQuery(
        name = "Ocorrencia.buscarPendencias",
        query = "select o from Ocorrencia o "
                + "where o.destinatario = :usuario " // se a ocorrencia se destinar a ele, ** se ele escreveu a ocorrencia ele vai acompanhar ela na tabela do crud
                + "or (o.unidade = :unidade and :grupo = ('COORDENADORES'))"  // se ele for o cordenador da ocorrenica, ele deve ver todas as fases
                + "or (o.status in ('GESTOR', 'FECHADO') and  :grupo = ('GESTORES'))"), // o gestor só seria depois de passar pelo coordenador
    @NamedQuery(name="Ocorrencia.buscarOcorrenciasGestor", query="select o from Ocorrencia o "
	         + "where o.dataCriacao between :ini and :fim "
	         + "order by o.dataCriacao"),
    @NamedQuery(name="Ocorrencia.buscarOcorrenciasGestorStatus", query="select o from Ocorrencia o "
    		 + "where o.tipo = :tipo "	
	         + "and o.dataCriacao between :ini and :fim "
	         + "order by o.dataCriacao"),
    @NamedQuery(name="Ocorrencia.buscarOcorrenciaStatusPeriodo", query="select o from Ocorrencia o "
			+ "where o.status = :status "
			+ "and o.unidade = :unidade "
			+ "and o.tenant = :tenant_id "
			+ "and o.dataCriacao between :ini and :fim "
			+ "order by o.status"),
	@NamedQuery(name="Ocorrencia.buscarOcorrenciaStatus", query="select o from Ocorrencia o "
			+ "where o.status = :status "
			+ "and o.unidade = :unidade "
			+ "and o.tenant = :tenant_id "
			+ "order by o.status")
                
    
})


public class Ocorrencia implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@NotNull(message = "O TIPO DE OCORRÊNCIA É OBRIGATÓRIO")
	@Enumerated(EnumType.STRING)
	private TipoOcorrencia tipo;
	
	@NotNull(message = "O STATUS É OBRIGATÓRIO")
	@Enumerated(EnumType.STRING)
	private StatusOcorrencia status;
	
	@NotNull(message = "POR FAVOR, PREENCHA A DESCRIÇÃO") ///TAVA NOT BLANK E TAVA DANDO ERRO 
	private String descricao;
	
	@ManyToOne
	private UsuarioEP remetente;
	
	@ManyToOne
	private UsuarioEP destinatario;
	
	//@ManyToOne
	private Long unidade;
	
	//@ManyToOne
	private Long tenant;
	
	//@OneToMany
	//private ArrayList<Resposta> respostas;
	
	
	
	
	
	/*
	 * Datas de Criação e Modificação
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao;	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataModificacao;

	@PrePersist
	@PreUpdate
	public void configuraDatasCriacaoAlteracao() {
		this.setDataModificacao( new Date() );
				
		if (this.getDataCriacao() == null) {
			this.setDataCriacao( new Date() );
		}		
	}
	
}