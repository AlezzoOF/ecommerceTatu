package com.idos.apk.backend.tienda.tatuajes.controller;

import com.idos.apk.backend.tienda.tatuajes.exceptions.TipoProductoAllReadyExist;
import com.idos.apk.backend.tienda.tatuajes.exceptions.TipoProductoNotFoundException;
import com.idos.apk.backend.tienda.tatuajes.model.TipoProducto;
import com.idos.apk.backend.tienda.tatuajes.service.interfaces.TipoProductoService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tp")
public class TipoProductoController {
    private final TipoProductoService service;

    public TipoProductoController(TipoProductoService service) {
        this.service = service;
    }

    @GetMapping("/show")
    public List<TipoProducto> show(){
        return service.findAll();
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam("string") String string){
        try {
            service.save(string);
            return new ResponseEntity<>("Tipo creado", HttpStatus.CREATED);
        }catch (TipoProductoAllReadyExist ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }catch (DataAccessException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
        try {
            service.delete(id);
            return new ResponseEntity<>("Borrado", HttpStatus.OK);
        }catch (TipoProductoNotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }catch (DataAccessException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}