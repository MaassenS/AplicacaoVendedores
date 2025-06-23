/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.exceptions;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author herrmann
 */
public class ValidacaoException extends RuntimeException{

    private Map<String, String> errors = new HashMap<>();

    public ValidacaoException(String msg) {
        super(msg);
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void adicionarErro(String campo, String mensagem) {
        errors.put(campo, mensagem);
    }
    
}
