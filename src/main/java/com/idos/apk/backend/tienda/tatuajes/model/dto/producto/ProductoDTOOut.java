package com.idos.apk.backend.tienda.tatuajes.model.dto.producto;

public record ProductoDTOOut(String id,
                             String nombre,
                             String descripcion,
                             double precio,

                             int cantidad,
                             String img
) {
}
