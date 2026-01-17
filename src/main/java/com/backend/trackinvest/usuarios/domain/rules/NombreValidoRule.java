package com.backend.trackinvest.usuarios.domain.rules;

import com.backend.trackinvest.usuarios.domain.exception.format.NombreInvalidoException;

public final class NombreValidoRule implements DomainRule<String> {

    private NombreValidoRule() {}

    public static String validar(String data) {
        return new NombreValidoRule().validate(data, true);
    }

    public static String validarOpcional(String data) {
        return new NombreValidoRule().validate(data, false);
    }

    public String validate(String nombre, boolean esObligatorio) {
        String nombreLimpio = (nombre != null) ? nombre.trim() : "";

        // Si es opcional y viene vacío, no validamos longitud, solo devolvemos vacío
        if (!esObligatorio && nombreLimpio.isEmpty()) {
            return "";
        }

        // Si es obligatorio (o si es opcional pero el usuario escribió algo), validamos longitud
        if (nombreLimpio.length() < 3 || nombreLimpio.length() > 25) {
            throw new NombreInvalidoException(nombre);
        }

        return nombreLimpio;
    }

    @Override
    public String validate(String data) {
        return validate(data, true); // Por defecto es obligatorio para cumplir la interfaz
    }
}
