package com.example.logistics_management_android_native.data.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Route {
    private String uuid;
    private Destino destino;
    private Fechas fechas;
    private String cliente;
    private String repartidorUserID;
    private String estado;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Destino {
        private double lon;
        private double lat;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Fechas {
        private String inicioRepartir;
        private String finRepartir;
    }
}