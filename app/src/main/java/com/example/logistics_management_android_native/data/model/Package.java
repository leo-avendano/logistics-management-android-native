package com.example.logistics_management_android_native.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Package {
    private String uuid;
    private String nombre;
    private String descripcion;
    private Ubicacion ubicacion;
    private Tamaño tamaño;
    private double peso;
    private String rutaRef;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ubicacion {
        private int deposito;
        private int sector;
        private int estante;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tamaño {
        private int ancho;
        private int largo;
        private int alto;
    }
}