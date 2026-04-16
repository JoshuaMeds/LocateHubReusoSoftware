package com.locatehub.demo.model;

import java.util.ArrayList;
import java.util.List;

public final class Carrinho {
    private List<Produto> items;
    private static Carrinho instance;

    private Carrinho() {
        this.items = new ArrayList<>();
    }

    public static Carrinho getInstance() {
        if (instance == null) {
            instance = new Carrinho();
        }
        return instance;
    }

    public void add(Produto produto) {
        items.add(produto);
    }

    public void remove(Produto produto) {
        items.forEach((item -> {
            if (item.getNome() == produto.getNome()) {
                items.remove(item);
            }
        }));
    }

    public void clear(){
        items.clear();
    }
}
