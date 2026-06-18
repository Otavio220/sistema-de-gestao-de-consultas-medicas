package com.example.clinica.clinica_api.repository;

import com.example.clinica.clinica_api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository responsável pelo acesso aos dados da entidade Usuario.
 * Estende JpaRepository, que já fornece os métodos básicos de CRUD.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca um usuário pelo e-mail.
     * Usado principalmente no processo de autenticação (login).
     * Retorna Optional para evitar NullPointerException caso não encontre.
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica se já existe um usuário cadastrado com o e-mail informado.
     * Útil para validar duplicatas antes de cadastrar um novo usuário.
     * Retorna true se existir, false caso contrário.
     */
    boolean existsByEmail(String email);
}