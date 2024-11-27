package com.teste.dao.lazy;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

import com.teste.model.Usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LazySorter implements Comparator<Usuario> {
	
    private String field;
    private SortOrder order;

    public LazySorter(String field, SortOrder sortOrder) {
        this.field = field;
        this.order = sortOrder;
    }

    @Override
    public int compare(Usuario u1, Usuario u2) {
        // Lógica de comparação baseada em `field` e `order`
        return 0;
    }
}
