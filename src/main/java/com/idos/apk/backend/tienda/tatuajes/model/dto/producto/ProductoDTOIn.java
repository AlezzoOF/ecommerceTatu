package com.idos.apk.backend.tienda.tatuajes.model.dto.producto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record ProductoDTOIn(
        @NotNull(message = "No puede ser nulo")
        @NotEmpty(message = "No puede ser vacio")
        @Size(max = 100, message = "Exedio el tamano max")
        String nombre,
        @NotNull(message = "No puede ser nulo")
        @NotEmpty(message = "No puede ser vacio")
        @Size(max = 200, message = "Exedio el tamano max")
        String descripcion,

        @Min(0)
        double precio,

        @Min(0)
        int cantidad,
        @NotNull(message = "No puede ser nulo")
        @NotEmpty(message = "No puede ser vacio")
        @Size(max = 50, message = "Exedio el tamano max")
        String tipo) {
}
