package org.meli.crud.exception;

public class ErrosDeBaseDeDados extends RuntimeException {
    public ErrosDeBaseDeDados(String mensagem) {
        super(mensagem);
    }
}
