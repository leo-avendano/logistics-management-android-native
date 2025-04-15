package com.example.logistics_management_android_native.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Rute {
    private String uuid;
    private String cliente;
    private String repartidorUserID;
    private String estado;
    private long inicioRepartir;
    private long finRepartir;
    private double lat;
    private double lon;
}