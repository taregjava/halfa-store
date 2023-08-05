package com.halfacode.data;


import java.time.LocalDateTime;

public record Token(String refreshToken, LocalDateTime issueAt, LocalDateTime expiredAt) {
}
