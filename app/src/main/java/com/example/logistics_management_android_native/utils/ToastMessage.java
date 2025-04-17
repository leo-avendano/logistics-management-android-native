package com.example.logistics_management_android_native.utils;

public enum ToastMessage {
    EMPTY_FIELDS("Complete todos los campos."),
    INVALID_EMAIL("El email ingresado no es válido."),
    PASSWORD_MISMATCH("Las contraseñas no coinciden."),
    PASSWORD_TOO_WEAK("La contraseña es muy debil."),
    REGISTER_SUCCESS("Registro exitoso. Verifique su correo antes de iniciar sesión."),
    REGISTER_FAIL("Hubo un error al registrarse: "),
    REGISTER_UNKNOWN_FAIL("Hubo un error desconocido al registrarse.");


    private final String message;

    ToastMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}