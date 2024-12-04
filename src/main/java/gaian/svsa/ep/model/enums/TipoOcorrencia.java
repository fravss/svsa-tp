package gaian.svsa.ep.model.enums;

import lombok.Getter;

public enum TipoOcorrencia {
	
	REUNIAO("Reunião"),
	FALTA("Falta"),
	ATESTADO("Atestado"),
	ASSEDIO("Assédio");
	
	@Getter
	private String nome;

    TipoOcorrencia(String nome) {
        this.nome = nome;
    }
    
    @Override
    public String toString() {
        return nome;
    }
	
}
