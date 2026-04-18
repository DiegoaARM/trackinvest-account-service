package com.trackinvest.account.common.domain.rules;

public interface DomainRule<T>{
    T check(T data);
}
