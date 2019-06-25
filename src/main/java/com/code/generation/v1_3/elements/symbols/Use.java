package com.code.generation.v1_3.elements.symbols;

public class Use {
    private Position position;
    private boolean write;

    public Use(Position position) {
        this.position = position;
    }

    public void setWriteIndication() {
        write = true;
    }

    public boolean isWrite() {
        return write;
    }

    public Position getPosition() {
        return position;
    }
}
