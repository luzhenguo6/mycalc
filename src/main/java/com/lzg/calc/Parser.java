package com.lzg.calc;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Parser {

    public static Token st_look_ahead_token;
    public static boolean st_look_ahead_token_exists;

    static Token my_get_token() {
        Token token = null;
        if (st_look_ahead_token_exists) {
            token = st_look_ahead_token;
            st_look_ahead_token_exists = false;
        } else {
            token = new Token();
            Lexicalanalyzer.get_token(token);
        }
        return token;
    }

    static void unget_token(Token token) {
        st_look_ahead_token = token;
        st_look_ahead_token_exists = true;
    }

    static double parse_primary_expression() {
        Token token;
        double value;
        int minus_flag = 1;
        token = my_get_token();
        if (TokenKind.SUB_OPERATOR_TOKEN.equals(token.getKind())) {
            minus_flag = -1;
        } else {
            unget_token(token);
        }
        token = my_get_token();
        if (TokenKind.NUMBER_TOKEN.equals(token.getKind())) {
            return token.getValue()*minus_flag;
        } else if (TokenKind.LEFT_PAREN_TOKEN.equals(token.getKind())) {
            value = parse_expression();
            token = my_get_token();
            if (!TokenKind.RIGHT_PAREN_TOKEN.equals(token.getKind())) {
                System.err.println("missing ')' error.");
                return 0.0;
            }
            return value*minus_flag;
        }
        System.err.println("syntax error.");
        return 0.0;
    }

    static double parse_term() {
        double v1;
        double v2;
        Token token;

        v1 = parse_primary_expression();
        for (;;) {
            token = my_get_token();
            if (!TokenKind.MUL_OPERATOR_TOKEN.equals(token.getKind()) &&
                    !TokenKind.DIV_OPERATOR_TOKEN.equals(token.getKind())) {
                unget_token(token);
                break;
            }
            v2 = parse_primary_expression();
            if (TokenKind.MUL_OPERATOR_TOKEN.equals(token.getKind())) {
                v1 *= v2;
            } else if (TokenKind.DIV_OPERATOR_TOKEN.equals(token.getKind())) {
                v1 /= v2;
            }
        }
        return v1;
    }

    static double parse_expression() {
        double v1;
        double v2;
        Token token;

        v1 = parse_term();
        for (;;) {
            token = my_get_token();
            if (!TokenKind.ADD_OPERATOR_TOKEN.equals(token.getKind()) &&
                    !TokenKind.SUB_OPERATOR_TOKEN.equals(token.getKind())) {
                unget_token(token);
                break;
            }
            v2 = parse_term();
            if (TokenKind.ADD_OPERATOR_TOKEN.equals(token.getKind())) {
                v1 += v2;
            } else if (TokenKind.SUB_OPERATOR_TOKEN.equals(token.getKind())) {
                v1 -= v2;
            } else {
                unget_token(token);
            }
        }
        return v1;
    }

    static double parse_line() {
        double value;

        st_look_ahead_token_exists = false;
        value = parse_expression();

        return value;
    }

    public static void main(String[] args) throws Exception {
        double value;
        char[] buf = new char[1024];
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (br.read(buf) > 0) {
            Lexicalanalyzer.set_line(new String(buf).getBytes());

            value = parse_line();
            System.out.println(value);
        }
    }

}
