package com.locatehub.demo.model;

public final class Chatbot {
    private User user;
    private static Chatbot instance;
    private Chatbot(User user){
        this.user = user;
    };
    public Chatbot getInstance(User user){
        if(instance == null){
            instance = new Chatbot(user);
        }
        return instance;
    }
    public void answerChat(String prompt){
        System.out.println("Você está absolutamente certo!" + user.nome);
    }
}
