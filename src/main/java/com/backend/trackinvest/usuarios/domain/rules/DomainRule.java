package com.backend.trackinvest.usuarios.domain.rules;

public interface DomainRule<T>{
    T check(T data);
}
