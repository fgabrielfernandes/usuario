package com.javanauta.usuario.infraesctructure.repository;


import com.javanauta.usuario.infraesctructure.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco,Long> {
}
