package com.luisjagella.gitflowcoach.service;

import com.luisjagella.gitflowcoach.dto.checklist.ChecklistItemResponse;
import com.luisjagella.gitflowcoach.dto.tarefa.TarefaRequest;
import com.luisjagella.gitflowcoach.dto.tarefa.TarefaResponse;
import com.luisjagella.gitflowcoach.entity.ChecklistItem;
import com.luisjagella.gitflowcoach.entity.Projeto;
import com.luisjagella.gitflowcoach.entity.Tarefa;
import com.luisjagella.gitflowcoach.exception.ProjetoNaoEncontradoException;
import com.luisjagella.gitflowcoach.exception.TarefaNaoEncontradaException;
import com.luisjagella.gitflowcoach.repository.ChecklistItemRepository;
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
    private final ChecklistFactory checklistFactory;
    private final ChecklistItemRepository checklistItemRepository;

    public TarefaService(
            TarefaRepository tarefaRepository,
            ProjetoRepository projetoRepository,
            BranchNameGenerator branchNameGenerator,
            ChecklistFactory checklistFactory,
            ChecklistItemRepository checklistItemRepository
    ) {
        this.tarefaRepository = tarefaRepository;
        this.projetoRepository = projetoRepository;
        this.branchNameGenerator = branchNameGenerator;
        this.checklistFactory = checklistFactory;
        this.checklistItemRepository = checklistItemRepository;
    }

    @Transactional
    public TarefaResponse cadastrar(TarefaRequest request) {
        Projeto projeto = buscarProjetoPorId(request.projetoId());
        Tarefa tarefa = new Tarefa();
        preencherDados(tarefa, request, projeto);

        Tarefa tarefaSalva = tarefaRepository.save(tarefa);
        List<ChecklistItem> checklist = checklistFactory.criarPara(tarefaSalva);
        checklistItemRepository.saveAll(checklist);

        return toResponse(tarefaSalva);
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
        List<ChecklistItemResponse> checklist = tarefa.getChecklist()
                .stream()
                .map(this::toChecklistItemResponse)
                .toList();

        return new TarefaResponse(
                tarefa.getId(),
                tarefa.getCodigo(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getBranchSugerida(),
                projeto.getId(),
                projeto.getNome(),
                checklist
        );
    }

    private ChecklistItemResponse toChecklistItemResponse(ChecklistItem item) {
        return new ChecklistItemResponse(
                item.getId(),
                item.getDescricao(),
                item.isConcluido(),
                item.getOrdem()
        );
    }
}
