package com.example.IGORPROYECTO.Command;

import java.util.ArrayList;
import java.util.List;

public class CargaMasivaUsuariosInvoker {

    private final List<Command> commandQueue = new ArrayList<>();

    public void addCommand(Command command) {
        commandQueue.add(command);
    }

    public void ejecutarComandos() {
        for (Command command : commandQueue) {
            command.execute();
        }
        commandQueue.clear();
    }
}
