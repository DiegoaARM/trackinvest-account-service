package com.backend.trackinvest.common.domain.rules;

public interface DomainRule<T>{
    T check(T data);
}
