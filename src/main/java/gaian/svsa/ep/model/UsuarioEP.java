package gaian.svsa.ep.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import gaian.svsa.ep.model.enums.GrupoEP;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity
@Table(name="usuario")
public class UsuarioEP implements Serializable {

	private static final long serialVersionUID = 82375949344894033L;

	@Id
	private Long codigo;
	
	@NotBlank(message="O nome é obrigatório")
	private String nome;
	
	private String registroProfissional;
	
	@Column(unique=true)
	private String email;


	@Enumerated(EnumType.STRING)
	private GrupoEP grupo;
	
	@ManyToOne
	@JoinColumn(name="codigo_unidade")
	private UnidadeEP unidade;
	
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
