package com.backend.trackinvest.common.rules;

public interface DomainRule<T>{
    T check(T data);
}
