package com.supernovaworks.ubicacion.dto;

import lombok.Data;
@Data
public class UbicacionDTO {
    private String type; // "sender"
    private String userId;
    private Double lat;
    private Double lng;
    private Long timestamp;
}
