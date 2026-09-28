package com.template.main;

/**
 * Inicializador sem herdar de Application, necessário para o executável
 * empacotado localizar corretamente o JavaFX no classpath.
 */
public final class Launcher {
    private Launcher() {
    }

    public static void main(String[] args) {
        Main.main(args);
    }
}
