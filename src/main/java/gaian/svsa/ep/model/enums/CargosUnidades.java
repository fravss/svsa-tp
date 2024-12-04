package gaian.svsa.ep.model.enums;

import lombok.Getter;

public enum CargosUnidades {
	
	TECNICO("Tecnicos"),
	COORDENADOR("Coordenador"),
	GESTOr("Gerente"),
	DIRETOR("Diretor"),
	ADMINISTRATIVOS("Administrativo"),
	CADASTRADOS("Cadastrador"),
	ASSISTENTESSOCIAIS("Assistente Social"),
	PSICOLOGOS("Psicólogo"),
	SERVICOSGERAIS("Auxiliar de Serviços Gerais"),
	ORIENTADORESSOCIAIS("Orientador Social");
	
    
	@Getter
	private String nome;

	CargosUnidades(String nome) {
        this.nome = nome;
    }
    
    @Override
    public String toString() {
        return nome;
    }
}
