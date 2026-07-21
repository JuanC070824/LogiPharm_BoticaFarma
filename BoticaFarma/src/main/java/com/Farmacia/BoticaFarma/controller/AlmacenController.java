package com.Farmacia.BoticaFarma.controller;

import com.Farmacia.BoticaFarma.model.Almacen;
import com.Farmacia.BoticaFarma.model.Botica;
import com.Farmacia.BoticaFarma.repository.AlmacenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/almacenes")
@CrossOrigin(origins = "*")
public class AlmacenController {

    @Autowired
    private AlmacenRepository almacenRepository;

    // GET /api/almacenes/botica/{idBotica}
    @GetMapping("/botica/{idBotica}")
    public ResponseEntity<List<Almacen>> obtenerSucursalesPorBotica(@PathVariable Integer idBotica) {
        List<Almacen> almacenes = almacenRepository.findByBoticaIdBotica(idBotica);
        return ResponseEntity.ok(almacenes);
    }

    // 👇 TE FALTABA ESTE ENDPOINT: POST /api/almacenes
    @PostMapping
    public ResponseEntity<?> crearSucursal(@RequestBody Map<String, Object> payload) {
        try {
            String nombreSucursal = (String) payload.get("nombreSucursal");
            String direccion = (String) payload.get("direccion");
            Integer idBotica = ((Number) payload.get("idBotica")).intValue();

            Almacen nuevoAlmacen = new Almacen();
            nuevoAlmacen.setNombreSucursal(nombreSucursal);
            nuevoAlmacen.setDireccion(direccion);

            // Relacionar con la Botica
            Botica botica = new Botica();
            botica.setIdBotica(idBotica);
            nuevoAlmacen.setBotica(botica);

            Almacen guardado = almacenRepository.save(nuevoAlmacen);
            return ResponseEntity.ok(guardado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear la sucursal: " + e.getMessage());
        }
    }
    // ============ AÑADIR: actualizar sucursal ============
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarSucursal(@PathVariable Integer id, @RequestBody Map<String, Object> payload) {
        try {
            Almacen almacen = almacenRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

            if (payload.get("nombreSucursal") != null) {
                almacen.setNombreSucursal((String) payload.get("nombreSucursal"));
            }
            if (payload.get("direccion") != null) {
                almacen.setDireccion((String) payload.get("direccion"));
            }

            Almacen actualizado = almacenRepository.save(almacen);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar la sucursal: " + e.getMessage());
        }
    }
    // ============ FIN ============

    // ============ AÑADIR: eliminar sucursal ============
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarSucursal(@PathVariable Integer id) {
        try {
            if (!almacenRepository.existsById(id)) {
                throw new RuntimeException("Sucursal no encontrada");
            }
            almacenRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Sucursal eliminada correctamente"));
        } catch (Exception e) {
            // Puede fallar por FK si tiene productos, lotes o usuarios asociados
            return ResponseEntity.badRequest().body(Map.of("message", "No se pudo eliminar: " + e.getMessage()));
        }
    }
    // ============ FIN ============







}