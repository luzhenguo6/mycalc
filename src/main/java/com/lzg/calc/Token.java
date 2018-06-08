package com.lzg.calc;

import static com.lzg.calc.Constant.MAX_TOKEN_SIZE;

public class Token {
    TokenKind kind;
    double value;
    byte[] str = new byte[MAX_TOKEN_SIZE];
}