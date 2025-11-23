package com.Farmacia.BoticaFarma.service;

import com.Farmacia.BoticaFarma.dto.ClienteDTO;
import com.Farmacia.BoticaFarma.model.Cliente;
import com.Farmacia.BoticaFarma.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<ClienteDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                // .filter(c -> c.getIdCliente() != 1) // ← COMENTA o ELIMINA esta línea
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO crear(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setApellidoPat(dto.getApellidoPat());
        cliente.setApellidoMat(dto.getApellidoMat());
        cliente.setDNI(dto.getDni());
        cliente.setRUC(dto.getRuc());

        Cliente guardado = clienteRepository.save(cliente);
        return convertirADTO(guardado);
    }

    public ClienteDTO actualizar(Integer id, ClienteDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        cliente.setNombre(dto.getNombre());
        cliente.setApellidoPat(dto.getApellidoPat());
        cliente.setApellidoMat(dto.getApellidoMat());
        cliente.setDNI(dto.getDni());
        cliente.setRUC(dto.getRuc());
        
        Cliente actualizado = clienteRepository.save(cliente);
        return convertirADTO(actualizado);
    }

    public void eliminar(Integer id) {
        if (id == 1) {
            throw new RuntimeException("No se puede eliminar el cliente genérico");
        }
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado");
        }
        clienteRepository.deleteById(id);
    }

    private ClienteDTO convertirADTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setIdCliente(cliente.getIdCliente());
        dto.setNombre(cliente.getNombre());
        dto.setApellidoPat(cliente.getApellidoPat());
        dto.setApellidoMat(cliente.getApellidoMat());
        dto.setDni(cliente.getDNI());
        dto.setRuc(cliente.getRUC());  // Ahora es Long
        dto.setNombreCompleto(cliente.getNombre() + " " + cliente.getApellidoPat() + " " + cliente.getApellidoMat());
        return dto;
    }
}
