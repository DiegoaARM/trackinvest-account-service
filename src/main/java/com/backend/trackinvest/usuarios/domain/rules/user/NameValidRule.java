package com.backend.trackinvest.usuarios.domain.rules.user;

import com.backend.trackinvest.usuarios.domain.exception.format.UserNameInvalidException;
import com.backend.trackinvest.usuarios.domain.rules.DomainRule;

public final class NameValidRule implements DomainRule<String> {

    private NameValidRule() {}

    public static String validate(String data) {
        return new NameValidRule().check(data, true);
    }

    public static String validateOptional(String data) {
        return new NameValidRule().check(data, false);
    }

    public String check(String name, boolean isMandatory) {
        String nameClean = (name != null) ? name.trim() : "";

        if (!isMandatory && nameClean.isEmpty()) {
            return "";
        }

        if (nameClean.length() < 3 || nameClean.length() > 25) {
            throw new UserNameInvalidException(name);
        }

        return nameClean;
    }

    @Override
    public String check(String data) {
        return check(data, true);
    }
}
