package com.backend.trackinvest.usuarios.domain.rules;

public interface DomainRule<T>{
    T validate(T data);
}
