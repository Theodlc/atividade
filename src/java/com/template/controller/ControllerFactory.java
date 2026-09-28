package com.template.controller;

import com.template.service.IHonkaiService;
import com.template.validator.IHonkaiValidador;
import javafx.util.Callback;

/** Cria Controllers e injeta as dependências configuradas pelo Main. */
public class ControllerFactory implements Callback<Class<?>, Object> {
    private final IHonkaiService service;
    private final IHonkaiValidador validador;

    public ControllerFactory(IHonkaiService service, IHonkaiValidador validador) {
        this.service = service;
        this.validador = validador;
    }

    @Override
    public Object call(Class<?> controllerClass) {
        if (controllerClass == MainController.class) {
            return new MainController(service, validador);
        }

        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Não foi possível criar o controller " + controllerClass.getName(), e);
        }
    }
}
