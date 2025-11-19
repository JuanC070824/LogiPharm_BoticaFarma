package com.Farmacia.BoticaFarma.service;

import com.Farmacia.BoticaFarma.dto.ClienteDTO;
import com.Farmacia.BoticaFarma.model.Cliente;
import com.Farmacia.BoticaFarma.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<ClienteDTO> listarClientes() {
        return clienteRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteDTO obtenerClientePorId(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return convertirADTO(cliente);
    }

    @Transactional
    public ClienteDTO crearCliente(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setApellido_pat(dto.getApellido_pat());
        cliente.setApellido_mat(dto.getApellido_mat());
        cliente.setDNI(dto.getDNI());
        cliente.setRUC(dto.getRUC());

        Cliente clienteGuardado = clienteRepository.save(cliente);
        return convertirADTO(clienteGuardado);
    }

    @Transactional
    public ClienteDTO actualizarCliente(Integer id, ClienteDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        cliente.setNombre(dto.getNombre());
        cliente.setApellido_pat(dto.getApellido_pat());
        cliente.setApellido_mat(dto.getApellido_mat());
        cliente.setDNI(dto.getDNI());
        cliente.setRUC(dto.getRUC());

        Cliente clienteActualizado = clienteRepository.save(cliente);
        return convertirADTO(clienteActualizado);
    }

    @Transactional
    public void eliminarCliente(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado");
        }
        clienteRepository.deleteById(id);
    }

    private ClienteDTO convertirADTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getIdCliente(),
                cliente.getNombre(),
                cliente.getApellido_pat(),
                cliente.getApellido_mat(),
                cliente.getDNI(),
                cliente.getRUC()
        );
    }
}
