package com.javanauta.usuario.infraesctructure.exceptions;

public class ResourceNotFountException extends RuntimeException {

    public ResourceNotFountException(String mensagem){
        super(mensagem);
    }

    public ResourceNotFountException(String mensagem, Throwable throwable){
        super(mensagem,throwable);
    }
}
