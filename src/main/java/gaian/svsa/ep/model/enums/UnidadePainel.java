package gaian.svsa.ep.model.enums;

import lombok.Getter;

public enum UnidadePainel{
	
	CENTROPOP("Centro Pop - PSE"),
	CREAS("CREAS - PSE"),
	CRASSANTCRUZ("CRAS Santa Cruz - PSB"),
	CRASSALTENSE("CRAS Saltnese - PSB"),
	CRASINDEPENDENCIA("CRAS Independência - PSB"),
	ESPACOSALTOSAOJOSE("Espaço Salto São José - PSB"),
	CRASNACOES("CRAS Nações - PSB"),
	CCI("CCI - PSB"),
	CASADEPASSAGEM("Casa de Passagem - PSE");
	
	@Getter
	private String nome;

	UnidadePainel(String nome) {
        this.nome = nome;
    }
    
    @Override
    public String toString() {
        return nome;
    }
	
}
