package com.lzg.calc;

import static com.lzg.calc.Constant.MAX_TOKEN_SIZE;

public class Token {
    private TokenKind kind;
    private double value;
    private byte[] str = new byte[MAX_TOKEN_SIZE];

    public TokenKind getKind() {
        return kind;
    }

    public void setKind(TokenKind kind) {
        this.kind = kind;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public byte[] getStr() {
        return str;
    }

    public void setStr(byte[] str) {
        this.str = str;
    }
}