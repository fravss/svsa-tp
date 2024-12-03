package gaian.svsa.ep.util;

import java.io.Serializable;
import java.util.Date;

public class DatasIniFim implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Date ini;
	private Date fim;
	
	public Date getIni() {
		return ini;
	}
	public void setIni(Date ini) {
		this.ini = ini;
	}
	public Date getFim() {
		return fim;
	}
	public void setFim(Date fim) {
		this.fim = fim;
	}
	
}
