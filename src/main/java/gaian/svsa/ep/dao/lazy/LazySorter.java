package gaian.svsa.ep.dao.lazy;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

import gaian.svsa.ep.model.UsuarioEP;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LazySorter implements Comparator<UsuarioEP> {
	
    private String field;
    private SortOrder order;

    public LazySorter(String field, SortOrder sortOrder) {
        this.field = field;
        this.order = sortOrder;
    }

    @Override
    public int compare(UsuarioEP u1, UsuarioEP u2) {
        // Lógica de comparação baseada em `field` e `order`
        return 0;
    }
}
