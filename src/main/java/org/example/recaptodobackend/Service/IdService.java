package org.example.recaptodobackend.Service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdService {
    public String createId() {
        return UUID.randomUUID().toString();
    }
}
