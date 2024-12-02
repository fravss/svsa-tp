       package gaian.svsa.ep.model.enums;

       import lombok.Getter;

       public enum CargosDepartamentos {
       	
       	ASSESSOR("ASSESSOR"),
       	GERENTE("Gerente"),
       	ADMINISTRATIVOS("Administrativo"),
       	MOTORISTA("Motorista"),
       	SERVICOSGERAIS("Auxiliar de Serviços Gerais");
       	
       	@Getter
       	private String nome;

       	CargosDepartamentos(String nome) {
               this.nome = nome;
           }
           
           @Override
           public String toString() {
               return nome;
           }
       }
