import { useCallback, useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router'
import { EmptyState } from '../components/feedback/EmptyState'
import { ErrorState } from '../components/feedback/ErrorState'
import { LoadingState } from '../components/feedback/LoadingState'
import { PageHeader } from '../components/layout/PageHeader'
import { Badge } from '../components/ui/Badge'
import { Card } from '../components/ui/Card'
import { api } from '../services/api'
import type { ProjetoResponse, TarefaResponse } from '../types/api'

function isChecklistComplete(tarefa: TarefaResponse) {
  return tarefa.checklist.length > 0 && tarefa.checklist.every((item) => item.concluido)
}

function errorMessage(error: unknown) {
  return error instanceof Error ? error.message : 'Ocorreu um erro inesperado ao consultar a API.'
}

export function DashboardPage() {
  const [projetos, setProjetos] = useState<ProjetoResponse[]>([])
  const [tarefas, setTarefas] = useState<TarefaResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [apiConnected, setApiConnected] = useState(false)

  const carregarDashboard = useCallback(async () => {
    setLoading(true)
    setError(null)

    try {
      const [projetosResponse, tarefasResponse] = await Promise.all([
        api.get<ProjetoResponse[]>('/projetos'),
        api.get<TarefaResponse[]>('/tarefas'),
      ])

      setProjetos(projetosResponse)
      setTarefas(tarefasResponse)
      setApiConnected(true)
    } catch (requestError) {
      setApiConnected(false)
      setError(errorMessage(requestError))
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    void carregarDashboard()
  }, [carregarDashboard])

  const tarefasConcluidas = useMemo(() => tarefas.filter(isChecklistComplete).length, [tarefas])
  const tarefasEmAndamento = tarefas.length - tarefasConcluidas
  const projetosRecentes = [...projetos].sort((a, b) => b.id - a.id).slice(0, 3)
  const tarefasRecentes = [...tarefas].sort((a, b) => b.id - a.id).slice(0, 3)

  return (
    <div className="page">
      <PageHeader
        eyebrow="Visão geral"
        title="Dashboard"
        description="Acompanhe os projetos, tarefas e o ritmo do fluxo de desenvolvimento."
        apiConnected={apiConnected}
      />

      {loading && (
        <Card>
          <LoadingState message="Consultando projetos e tarefas..." />
        </Card>
      )}

      {!loading && error && (
        <Card>
          <ErrorState message={error} onRetry={() => void carregarDashboard()} />
        </Card>
      )}

      {!loading && !error && (
        <>
          <section className="metrics-grid" aria-label="Indicadores do fluxo">
            <Card className="metric-card">
              <span>Projetos</span>
              <strong>{projetos.length}</strong>
              <small>repositórios acompanhados</small>
            </Card>
            <Card className="metric-card">
              <span>Tarefas</span>
              <strong>{tarefas.length}</strong>
              <small>tarefas cadastradas</small>
            </Card>
            <Card className="metric-card">
              <span>Concluídas</span>
              <strong>{tarefasConcluidas}</strong>
              <small>checklists finalizados</small>
            </Card>
            <Card className="metric-card">
              <span>Em andamento</span>
              <strong>{tarefasEmAndamento}</strong>
              <small>fluxos ainda abertos</small>
            </Card>
          </section>

          {projetos.length === 0 && tarefas.length === 0 ? (
            <Card className="dashboard-empty">
              <EmptyState
                title="Nenhum dado cadastrado"
                description="Cadastre projetos e tarefas pela API para acompanhar o fluxo neste dashboard."
              />
            </Card>
          ) : (
            <section className="dashboard-grid" aria-label="Atividade recente">
              <Card className="dashboard-panel">
                <div className="section-heading">
                  <div>
                    <span>Projetos</span>
                    <h2>Adicionados recentemente</h2>
                  </div>
                  <Link to="/projetos">Ver todos</Link>
                </div>
                {projetosRecentes.length === 0 ? (
                  <p className="inline-empty">Nenhum projeto disponível.</p>
                ) : (
                  <ul className="recent-list">
                    {projetosRecentes.map((projeto) => (
                      <li key={projeto.id}>
                        <div>
                          <strong>{projeto.nome}</strong>
                          <span>{projeto.repositorio}</span>
                        </div>
                        <Badge variant="accent">{projeto.branchBase}</Badge>
                      </li>
                    ))}
                  </ul>
                )}
              </Card>

              <Card className="dashboard-panel">
                <div className="section-heading">
                  <div>
                    <span>Tarefas</span>
                    <h2>Movimentadas recentemente</h2>
                  </div>
                  <Link to="/tarefas">Ver todas</Link>
                </div>
                {tarefasRecentes.length === 0 ? (
                  <p className="inline-empty">Nenhuma tarefa disponível.</p>
                ) : (
                  <ul className="recent-list">
                    {tarefasRecentes.map((tarefa) => (
                      <li key={tarefa.id}>
                        <div>
                          <strong>{tarefa.codigo} · {tarefa.titulo}</strong>
                          <span>{tarefa.projetoNome}</span>
                        </div>
                        <Badge variant={isChecklistComplete(tarefa) ? 'success' : 'warning'}>
                          {isChecklistComplete(tarefa) ? 'Concluída' : 'Em andamento'}
                        </Badge>
                      </li>
                    ))}
                  </ul>
                )}
              </Card>
            </section>
          )}

          <section className="shortcut-grid" aria-label="Atalhos">
            <Link className="shortcut-card" to="/projetos">
              <span>01</span>
              <div>
                <strong>Explorar projetos</strong>
                <small>Consulte repositórios e branches base.</small>
              </div>
            </Link>
            <Link className="shortcut-card" to="/tarefas">
              <span>02</span>
              <div>
                <strong>Acompanhar tarefas</strong>
                <small>Veja branches e progresso dos checklists.</small>
              </div>
            </Link>
            <Link className="shortcut-card" to="/commits">
              <span>03</span>
              <div>
                <strong>Validar um commit</strong>
                <small>Confira mensagens no padrão do projeto.</small>
              </div>
            </Link>
          </section>
        </>
      )}
    </div>
  )
}
