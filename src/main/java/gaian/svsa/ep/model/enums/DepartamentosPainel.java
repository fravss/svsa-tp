package gaian.svsa.ep.model.enums;

import lombok.Getter;

public enum DepartamentosPainel {
	
	GABINETE("Gabinete da Secretária"),
	INCLUSAOSOCIAL("Departamento de inclusão social"),
	PROGRAMASPROJETOS("Departamento de programas/projetos"),
	GERENCIACADUNICO("Gerência Cadúnico"),
	PROTECAOSOCAILBASICA("Proteção Social básica"),
	PROTECAOSOCIALESPECIAL("Proteção Social especial"),
	ASSISTENTESSOCIAIS("Assistente Social"),
	DIREITOSHUMANOS("Departamento de Direitos Humanos "),
	CMAS("CMAS"),
	CONSELHOTUTELAR("Conselho Tutelar");
	
	@Getter
	private String nome;

	DepartamentosPainel(String nome) {
        this.nome = nome;
    }
    
    @Override
    public String toString() {
        return nome;
    }
}
