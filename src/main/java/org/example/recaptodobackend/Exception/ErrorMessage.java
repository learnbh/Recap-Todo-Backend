package org.example.recaptodobackend.Exception;

import java.time.Instant;

public record ErrorMessage(String message, Instant timestamp, int status) {
}
