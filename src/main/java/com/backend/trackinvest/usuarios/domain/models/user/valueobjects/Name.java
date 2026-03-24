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
        lastName = NameValidRule.validateOptional(lastName);

        // Optional: If they come with text, they must have >= 3 letters.
        middleName = NameValidRule.validateOptional(middleName);
        secondLastName = NameValidRule.validateOptional(secondLastName);
    }

    public static Name fromFullString(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            return new Name("Usuario", "", "InverTrack", "");
        }

        String[] parts = fullName.trim().split("\\s+");

        String first = parts[0];
        String middle = "";
        String last = ""; // Inicializamos vacío
        String secondLast = "";

        if (parts.length == 2) {
            // "Diego Arboleda"
            last = parts[1];
        } else if (parts.length == 3) {
            // "Diego Alejandro Arboleda"
            middle = parts[1];
            last = parts[2];
        } else if (parts.length >= 4) {
            // "Diego Alejandro Arboleda Restrepo"
            middle = parts[1];
            last = parts[2];
            secondLast = parts[3];
        }

        return new Name(first, middle, last, secondLast);
    }

    public String fullName() {
        return String.format("%s %s %s %s", firstName, middleName, lastName, secondLastName)
                .replace("  ", " ").trim();
    }
}
