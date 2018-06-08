package com.lzg.calc;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static com.lzg.calc.Constant.MAX_TOKEN_SIZE;

public class lexicalanalyzer {

    public static byte[] st_line;
    public static int st_line_pos = 0;

    enum LexerStatus {
        INITIAL_STATUS,
        IN_INT_PART_STATUS,
        DOT_STATUS,
        IN_FRAC_PART_STATUS,
    }

    void set_line(byte[] line) {
        st_line = line;
        st_line_pos = 0;
    }

    void get_token(Token token) {
        int out_pos = 0;
        LexerStatus status = LexerStatus.INITIAL_STATUS;
        byte current_byte;

        token.kind = TokenKind.BAD_TOKEN;
        while (st_line[st_line_pos] != 0) {
            current_byte = st_line[st_line_pos];
            if ((status == LexerStatus.IN_INT_PART_STATUS
                    || status == LexerStatus.IN_FRAC_PART_STATUS) &&
                    !Character.isDigit(current_byte) && current_byte != '.') {
                token.kind = TokenKind.NUMBER_TOKEN;
                try {
                    token.value = Double.valueOf(new String(token.str));
                } catch (Exception e) {
                }
                return;
            }
            if (current_byte == '\n') {
                token.kind = TokenKind.END_OF_LINE_TOKEN;
                return;
            }
            if (Character.isSpaceChar(current_byte)) {
                st_line_pos++;
                continue;
            }
            if (out_pos >= MAX_TOKEN_SIZE-1) {
                System.err.println("token too long.");
                return;
            }
            token.str[out_pos] = st_line[st_line_pos];
            st_line_pos++;
            out_pos++;
            token.str[out_pos] = 0;

            if (current_byte == '+') {
                token.kind = TokenKind.ADD_OPERATOR_TOKEN;
                return;
            } else if (current_byte == '-') {
                token.kind = TokenKind.SUB_OPERATOR_TOKEN;
                return;
            } else if (current_byte == '*') {
                token.kind = TokenKind.MUL_OPERATOR_TOKEN;
                return;
            } else if (current_byte == '/') {
                token.kind = TokenKind.DIV_OPERATOR_TOKEN;
                return;
            } else if (Character.isDigit(current_byte)) {
                if (status == LexerStatus.INITIAL_STATUS) {
                    status = LexerStatus.IN_INT_PART_STATUS;
                } else if (status == LexerStatus.DOT_STATUS) {
                    status = LexerStatus.IN_FRAC_PART_STATUS;
                }
            } else if (current_byte == '.') {
                if (status == LexerStatus.IN_INT_PART_STATUS) {
                    status = LexerStatus.DOT_STATUS;
                } else {
                    System.err.printf("syntax error.\n");
                    return;
                }
            } else {
                System.err.printf("bad character(%d)\n", current_byte);
                return;
//                throw new Exception(String.format("bad character(%d)", current_byte));
            }
        }
    }

    void parse_line(byte[] buf) {
        Token token;
        set_line(buf);
        for (;;) {
            token = new Token();
            get_token(token);
            if (token.kind == TokenKind.END_OF_LINE_TOKEN) {
                break;
            } else if (token.kind == TokenKind.BAD_TOKEN) {
                System.out.printf("kind..(%s), str..%s\n", token.kind, new String(token.str));
                break;
            } else {
                System.out.printf("kind..(%s), str..%s\n", token.kind, new String(token.str));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        lexicalanalyzer lexicalanalyzer = new lexicalanalyzer();

        char[] buf = new char[1024];
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (br.read(buf) > 0) {
            lexicalanalyzer.parse_line(new String(buf).getBytes());
        }
    }

}
