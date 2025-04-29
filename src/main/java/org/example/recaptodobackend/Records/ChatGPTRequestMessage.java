package org.example.recaptodobackend.Records;

public record ChatGPTRequestMessage(
        String role,
        String content
) {
}