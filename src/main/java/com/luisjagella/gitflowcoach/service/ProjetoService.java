package com.luisjagella.gitflowcoach.service;

import com.luisjagella.gitflowcoach.dto.projeto.ProjetoRequest;
import com.luisjagella.gitflowcoach.dto.projeto.ProjetoResponse;
import com.luisjagella.gitflowcoach.entity.Projeto;
import com.luisjagella.gitflowcoach.exception.ProjetoNaoEncontradoException;
import com.luisjagella.gitflowcoach.repository.ProjetoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;

    public ProjetoService(ProjetoRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }

    @Transactional
    public ProjetoResponse cadastrar(ProjetoRequest request) {
        Projeto projeto = new Projeto();
        projeto.setNome(request.nome());
        projeto.setRepositorio(request.repositorio());
        projeto.setBranchBase(request.branchBase());

        return toResponse(projetoRepository.save(projeto));
    }

    @Transactional(readOnly = true)
    public List<ProjetoResponse> listar() {
        return projetoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjetoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidadePorId(id));
    }

    private Projeto buscarEntidadePorId(Long id) {
        return projetoRepository.findById(id)
                .orElseThrow(() -> new ProjetoNaoEncontradoException(id));
    }

    private ProjetoResponse toResponse(Projeto projeto) {
        return new ProjetoResponse(
                projeto.getId(),
                projeto.getNome(),
                projeto.getRepositorio(),
                projeto.getBranchBase()
        );
    }
}
