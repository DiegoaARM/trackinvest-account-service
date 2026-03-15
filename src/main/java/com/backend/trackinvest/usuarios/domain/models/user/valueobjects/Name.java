package com.backend.trackinvest.usuarios.domain.models.user.valueobjects;

import com.backend.trackinvest.usuarios.domain.rules.NameValidRule;

public record Name(
        String firstName,
        String middleName,
        String lastName,
        String secondLastName
) {

    public Name {
        // Use your Rules to validate each part
        firstName = NameValidRule.validate(firstName);
        lastName = NameValidRule.validate(lastName);

        // Optional: If they come with text, they must have >= 3 letters.
        middleName = NameValidRule.validateOptional(middleName);
        secondLastName = NameValidRule.validateOptional(secondLastName);
    }

    public String fullName() {
        return String.format("%s %s %s %s", firstName, middleName, lastName, secondLastName)
                .replace("  ", " ").trim();
    }
}
