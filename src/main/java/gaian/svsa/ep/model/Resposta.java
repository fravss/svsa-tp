package gaian.svsa.ep.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity
@Table(name="resposta")
@NamedQueries({
    @NamedQuery(
        name = "Resposta.findByOcorrencia",
        query = "SELECT r FROM Resposta r WHERE r.ocorrencia.codigo = :codigoOcorrencia order by r.dataCriacao"
    )
})
public class Resposta implements Serializable {

	private static final long serialVersionUID = 82375949344894033L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="codigo_usuario")
	private UsuarioEP usuario;
	
	private String resposta;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="codigo_ocorrencia")
	private Ocorrencia ocorrencia;

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