package com.luisjagella.gitflowcoach.service;

import com.luisjagella.gitflowcoach.dto.tarefa.TarefaRequest;
import com.luisjagella.gitflowcoach.dto.tarefa.TarefaResponse;
import com.luisjagella.gitflowcoach.entity.Projeto;
import com.luisjagella.gitflowcoach.entity.Tarefa;
import com.luisjagella.gitflowcoach.exception.ProjetoNaoEncontradoException;
import com.luisjagella.gitflowcoach.exception.TarefaNaoEncontradaException;
import com.luisjagella.gitflowcoach.repository.ProjetoRepository;
import com.luisjagella.gitflowcoach.repository.TarefaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    private static final Long TAREFA_ID = 10L;
    private static final Long PROJETO_ID = 1L;

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private ProjetoRepository projetoRepository;

    @InjectMocks
    private TarefaService tarefaService;

    @Test
    void deveCadastrarTarefaQuandoProjetoExistir() {
        Projeto projeto = criarProjeto(PROJETO_ID, "Gitflow Coach");
        TarefaRequest request = criarRequest(PROJETO_ID);
        when(projetoRepository.findById(PROJETO_ID)).thenReturn(Optional.of(projeto));
        when(tarefaRepository.save(any(Tarefa.class))).thenAnswer(invocation -> {
            Tarefa tarefa = invocation.getArgument(0);
            tarefa.setId(TAREFA_ID);
            return tarefa;
        });

        TarefaResponse response = tarefaService.cadastrar(request);

        ArgumentCaptor<Tarefa> tarefaCaptor = ArgumentCaptor.forClass(Tarefa.class);
        verify(tarefaRepository).save(tarefaCaptor.capture());
        assertAll(
                () -> assertEquals(TAREFA_ID, response.id()),
                () -> assertEquals(request.codigo(), response.codigo()),
                () -> assertEquals(request.titulo(), response.titulo()),
                () -> assertEquals(request.descricao(), response.descricao()),
                () -> assertEquals(request.branchSugerida(), response.branchSugerida()),
                () -> assertEquals(PROJETO_ID, response.projetoId()),
                () -> assertEquals(projeto.getNome(), response.projetoNome()),
                () -> assertSame(projeto, tarefaCaptor.getValue().getProjeto())
        );
    }

    @Test
    void deveLancarExcecaoAoCadastrarComProjetoInexistente() {
        TarefaRequest request = criarRequest(PROJETO_ID);
        when(projetoRepository.findById(PROJETO_ID)).thenReturn(Optional.empty());

        assertThrows(
                ProjetoNaoEncontradoException.class,
                () -> tarefaService.cadastrar(request)
        );

        verifyNoInteractions(tarefaRepository);
    }

    @Test
    void deveBuscarTarefaExistentePorId() {
        Projeto projeto = criarProjeto(PROJETO_ID, "Gitflow Coach");
        Tarefa tarefa = criarTarefa(TAREFA_ID, projeto);
        when(tarefaRepository.findById(TAREFA_ID)).thenReturn(Optional.of(tarefa));

        TarefaResponse response = tarefaService.buscarPorId(TAREFA_ID);

        assertAll(
                () -> assertEquals(TAREFA_ID, response.id()),
                () -> assertEquals(tarefa.getCodigo(), response.codigo()),
                () -> assertEquals(PROJETO_ID, response.projetoId()),
                () -> assertEquals(projeto.getNome(), response.projetoNome())
        );
    }

    @Test
    void deveLancarExcecaoAoBuscarTarefaInexistente() {
        when(tarefaRepository.findById(TAREFA_ID)).thenReturn(Optional.empty());

        assertThrows(
                TarefaNaoEncontradaException.class,
                () -> tarefaService.buscarPorId(TAREFA_ID)
        );
    }

    @Test
    void deveAtualizarTarefaEProjetoAssociado() {
        Projeto projetoAtual = criarProjeto(2L, "Projeto atual");
        Projeto novoProjeto = criarProjeto(PROJETO_ID, "Gitflow Coach");
        Tarefa tarefa = criarTarefa(TAREFA_ID, projetoAtual);
        TarefaRequest request = criarRequest(PROJETO_ID);
        when(tarefaRepository.findById(TAREFA_ID)).thenReturn(Optional.of(tarefa));
        when(projetoRepository.findById(PROJETO_ID)).thenReturn(Optional.of(novoProjeto));
        when(tarefaRepository.save(any(Tarefa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TarefaResponse response = tarefaService.atualizar(TAREFA_ID, request);

        assertAll(
                () -> assertEquals(request.codigo(), response.codigo()),
                () -> assertEquals(request.titulo(), response.titulo()),
                () -> assertEquals(request.descricao(), response.descricao()),
                () -> assertEquals(request.branchSugerida(), response.branchSugerida()),
                () -> assertEquals(PROJETO_ID, response.projetoId()),
                () -> assertEquals(novoProjeto.getNome(), response.projetoNome()),
                () -> assertSame(novoProjeto, tarefa.getProjeto())
        );
        verify(tarefaRepository).save(tarefa);
    }

    @Test
    void deveExcluirTarefaExistente() {
        Projeto projeto = criarProjeto(PROJETO_ID, "Gitflow Coach");
        Tarefa tarefa = criarTarefa(TAREFA_ID, projeto);
        when(tarefaRepository.findById(TAREFA_ID)).thenReturn(Optional.of(tarefa));

        tarefaService.excluir(TAREFA_ID);

        verify(tarefaRepository).delete(tarefa);
    }

    private TarefaRequest criarRequest(Long projetoId) {
        return new TarefaRequest(
                "ISSUE-4",
                "Implementar CRUD de tarefas",
                "Criar endpoints e camada de serviço para tarefas",
                "feat/4-crud-tarefas",
                projetoId
        );
    }

    private Projeto criarProjeto(Long id, String nome) {
        Projeto projeto = new Projeto();
        projeto.setId(id);
        projeto.setNome(nome);
        projeto.setRepositorio("luis-jagella/gitflow-coach");
        projeto.setBranchBase("main");
        return projeto;
    }

    private Tarefa criarTarefa(Long id, Projeto projeto) {
        Tarefa tarefa = new Tarefa();
        tarefa.setId(id);
        tarefa.setCodigo("ISSUE-3");
        tarefa.setTitulo("Título anterior");
        tarefa.setDescricao("Descrição anterior");
        tarefa.setBranchSugerida("feat/3-tarefa-anterior");
        tarefa.setProjeto(projeto);
        return tarefa;
    }
}
