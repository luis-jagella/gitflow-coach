package com.luisjagella.gitflowcoach.service;

import com.luisjagella.gitflowcoach.dto.tarefa.TarefaRequest;
import com.luisjagella.gitflowcoach.dto.tarefa.TarefaResponse;
import com.luisjagella.gitflowcoach.entity.Projeto;
import com.luisjagella.gitflowcoach.entity.Tarefa;
import com.luisjagella.gitflowcoach.exception.ProjetoNaoEncontradoException;
import com.luisjagella.gitflowcoach.exception.TarefaNaoEncontradaException;
import com.luisjagella.gitflowcoach.repository.ProjetoRepository;
import com.luisjagella.gitflowcoach.repository.TarefaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final ProjetoRepository projetoRepository;
    private final BranchNameGenerator branchNameGenerator;

    public TarefaService(
            TarefaRepository tarefaRepository,
            ProjetoRepository projetoRepository,
            BranchNameGenerator branchNameGenerator
    ) {
        this.tarefaRepository = tarefaRepository;
        this.projetoRepository = projetoRepository;
        this.branchNameGenerator = branchNameGenerator;
    }

    @Transactional
    public TarefaResponse cadastrar(TarefaRequest request) {
        Projeto projeto = buscarProjetoPorId(request.projetoId());
        Tarefa tarefa = new Tarefa();
        preencherDados(tarefa, request, projeto);

        return toResponse(tarefaRepository.save(tarefa));
    }

    @Transactional(readOnly = true)
    public List<TarefaResponse> listar() {
        return tarefaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TarefaResponse buscarPorId(Long id) {
        return toResponse(buscarEntidadePorId(id));
    }

    @Transactional
    public TarefaResponse atualizar(Long id, TarefaRequest request) {
        Tarefa tarefa = buscarEntidadePorId(id);
        Projeto projeto = buscarProjetoPorId(request.projetoId());
        preencherDados(tarefa, request, projeto);

        return toResponse(tarefaRepository.save(tarefa));
    }

    @Transactional
    public void excluir(Long id) {
        Tarefa tarefa = buscarEntidadePorId(id);
        tarefaRepository.delete(tarefa);
    }

    private Tarefa buscarEntidadePorId(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException(id));
    }

    private Projeto buscarProjetoPorId(Long id) {
        return projetoRepository.findById(id)
                .orElseThrow(() -> new ProjetoNaoEncontradoException(id));
    }

    private void preencherDados(Tarefa tarefa, TarefaRequest request, Projeto projeto) {
        tarefa.setCodigo(request.codigo());
        tarefa.setTitulo(request.titulo());
        tarefa.setDescricao(request.descricao());
        tarefa.setBranchSugerida(branchNameGenerator.gerar(request.codigo(), request.titulo()));
        tarefa.setProjeto(projeto);
    }

    private TarefaResponse toResponse(Tarefa tarefa) {
        Projeto projeto = tarefa.getProjeto();
        return new TarefaResponse(
                tarefa.getId(),
                tarefa.getCodigo(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getBranchSugerida(),
                projeto.getId(),
                projeto.getNome()
        );
    }
}
