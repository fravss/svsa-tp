package gaian.svsa.ep.model;

import java.io.Serializable;
import java.util.Date;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@Table(name="unidade")
public class UnidadeEP implements Serializable {


	private static final long serialVersionUID = -5526059262907035239L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;

	private String nome;
	
	
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
