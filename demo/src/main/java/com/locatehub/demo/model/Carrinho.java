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
}
