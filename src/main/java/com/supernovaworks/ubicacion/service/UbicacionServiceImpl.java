package com.supernovaworks.ubicacion.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernovaworks.ubicacion.dto.UbicacionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UbicacionServiceImpl implements UbicacionService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void procesarUbicacion(UbicacionDTO ubicacion) {
 
    	
    }
}
