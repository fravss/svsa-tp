package gaian.svsa.ep.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@Table(name="tenant")
public class TenantEP implements Serializable {

	private static final long serialVersionUID = -5526059262907035239L;
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@NotBlank(message="O nome do tenant/municipio é obrigatório.")
	private String tenant;	
	
	private String secretaria = null;
	
	private String s3Key = null;
	

}
