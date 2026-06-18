package com.example.clinica.clinica_api.service;

import com.example.clinica.clinica_api.entity.Especialidade;
import com.example.clinica.clinica_api.entity.Medico;
import com.example.clinica.clinica_api.entity.Usuario;
import com.example.clinica.clinica_api.repository.EspecialidadeRepository;
import com.example.clinica.clinica_api.repository.MedicoRepository;
import com.example.clinica.clinica_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministradorService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    // Gerenciar usuários
    public void gerenciarUsuarios(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
        usuarioRepository.save(usuario);
    }

    // Gerenciar médicos
    public void gerenciarMedicos(Medico medico) {
        if (medico.getCrm() == null || medico.getCrm().isBlank()) {
            throw new RuntimeException("CRM é obrigatório");
        }
        medicoRepository.save(medico);
    }

    // Gerenciar consultores (usuários com cargo de consultor)
    public void gerenciarConsultores(Usuario consultor) {
        consultor.setCargo("CONSULTOR");
        if (usuarioRepository.existsByEmail(consultor.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
        usuarioRepository.save(consultor);
    }

    // Gerenciamento de especialidades
    public void gerenciamentoEspecialidades(Especialidade especialidade) {
        if (especialidade.getNome() == null || especialidade.getNome().isBlank()) {
            throw new RuntimeException("Nome da especialidade é obrigatório");
        }
        especialidadeRepository.save(especialidade);
    }

    // Listar todos os usuários
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Listar todos os médicos ativos
    public List<Medico> listarMedicosAtivos() {
        return medicoRepository.findByAtivoTrue();
    }

    // Listar todas as especialidades
    public List<Especialidade> listarEspecialidades() {
        return especialidadeRepository.findAll();
    }
}
