package com.backend.trackinvest.usuarios.domain.models.valueobjects;

import com.backend.trackinvest.usuarios.domain.rules.NombreValidoRule;

public record Nombre(
        String primerNombre,
        String segundoNombre,
        String primerApellido,
        String segundoApellido
) {

    public Nombre {
        // Usamos tus Rules para validar cada parte
        primerNombre = NombreValidoRule.validar(primerNombre);
        primerApellido = NombreValidoRule.validar(primerApellido);

        // Opcionales: Si vienen con texto, deben tener >= 3 letras.
        // Si vienen nulos o vacíos, se guardan como "" sin error.
        segundoNombre = NombreValidoRule.validarOpcional(segundoNombre);
        segundoApellido = NombreValidoRule.validarOpcional(segundoApellido);
    }

    public String nombreCompleto() {
        return String.format("%s %s %s %s", primerNombre, segundoNombre, primerApellido, segundoApellido)
                .replace("  ", " ").trim();
    }
}
