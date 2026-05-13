package com.locatehub.demo.dto;

public record RegisterRequest(String email, String senha, String documento , String nome , boolean locador) {
}
