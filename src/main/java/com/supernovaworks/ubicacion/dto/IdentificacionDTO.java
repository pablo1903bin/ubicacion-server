package com.supernovaworks.ubicacion.dto;

import lombok.Data;

@Data
public class IdentificacionDTO {
    private String type; // "viewer" o "sender"
    private String userId;
    private String verA; // solo lo usa el viewer
    private String rol;
    private String grupoId;
    private String dispositivo;
    private Long timestamp;
}
