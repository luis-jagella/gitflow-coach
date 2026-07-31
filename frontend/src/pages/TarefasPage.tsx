import { useEffect, useRef, useState } from 'react'
import { EmptyState } from '../components/feedback/EmptyState'
import { ErrorState } from '../components/feedback/ErrorState'
import { LoadingState } from '../components/feedback/LoadingState'
import { PageHeader } from '../components/layout/PageHeader'
import { Badge } from '../components/ui/Badge'
import { Card } from '../components/ui/Card'
import { useApiCollection } from '../hooks/useApiCollection'
import type { TarefaResponse } from '../types/api'

interface ChecklistProgress {
  completed: number
  total: number
  percentage: number
}

function checklistProgress(tarefa: TarefaResponse): ChecklistProgress {
  const completed = tarefa.checklist.filter((item) => item.concluido).length
  const total = tarefa.checklist.length

  return {
    completed,
    total,
    percentage: total === 0 ? 0 : Math.round((completed / total) * 100),
  }
}

export function TarefasPage() {
  const { data: tarefas, loading, error, apiConnected, reload } =
    useApiCollection<TarefaResponse>('/tarefas')
  const [copiedTaskId, setCopiedTaskId] = useState<number | null>(null)
  const [copyError, setCopyError] = useState<string | null>(null)
  const feedbackTimer = useRef<number | null>(null)

  useEffect(
    () => () => {
      if (feedbackTimer.current !== null) {
        window.clearTimeout(feedbackTimer.current)
      }
    },
    [],
  )

  async function copyBranch(tarefa: TarefaResponse) {
    try {
      await navigator.clipboard.writeText(tarefa.branchSugerida)
      setCopiedTaskId(tarefa.id)
      setCopyError(null)

      if (feedbackTimer.current !== null) {
        window.clearTimeout(feedbackTimer.current)
      }
      feedbackTimer.current = window.setTimeout(() => setCopiedTaskId(null), 2200)
    } catch {
      setCopyError('Não foi possível copiar a branch neste navegador.')
    }
  }

  return (
    <div className="page">
      <PageHeader
        eyebrow="Execução"
        title="Tarefas"
        description="Visualize branches sugeridas e o progresso dos checklists."
        apiConnected={apiConnected}
      />

      {loading && (
        <Card>
          <LoadingState message="Carregando tarefas..." />
        </Card>
      )}

      {!loading && error && (
        <Card>
          <ErrorState message={error} onRetry={() => void reload()} />
        </Card>
      )}

      {!loading && !error && tarefas.length === 0 && (
        <Card>
          <EmptyState
            title="Nenhuma tarefa para exibir"
            description="As tarefas cadastradas na API aparecerão nesta página."
          />
        </Card>
      )}

      {!loading && !error && tarefas.length > 0 && (
        <>
          <div className="copy-feedback" aria-live="polite">
            {copiedTaskId !== null && 'Branch copiada'}
            {copyError}
          </div>
          <section className="task-list" aria-label="Tarefas cadastradas">
            {tarefas.map((tarefa) => {
              const progress = checklistProgress(tarefa)
              const complete = progress.total > 0 && progress.completed === progress.total

              return (
                <Card className="resource-card task-card" key={tarefa.id}>
                  <div className="task-card__main">
                    <div className="resource-card__topline">
                      <Badge variant="accent">{tarefa.codigo}</Badge>
                      <Badge variant={complete ? 'success' : 'warning'}>
                        {complete ? 'Concluída' : 'Em andamento'}
                      </Badge>
                    </div>
                    <div className="resource-card__title">
                      <h2>{tarefa.titulo}</h2>
                      <p>{tarefa.projetoNome}</p>
                    </div>
                    <div className="branch-copy">
                      <code className="branch-label">{tarefa.branchSugerida}</code>
                      <button
                        className="button button--ghost"
                        type="button"
                        onClick={() => void copyBranch(tarefa)}
                      >
                        {copiedTaskId === tarefa.id ? 'Branch copiada' : 'Copiar branch'}
                      </button>
                    </div>
                  </div>

                  <div className="task-progress">
                    <div className="task-progress__label">
                      <span>Progresso do checklist</span>
                      <strong>{progress.completed}/{progress.total}</strong>
                    </div>
                    <div
                      className="progress-track"
                      role="progressbar"
                      aria-label={`Progresso de ${tarefa.titulo}`}
                      aria-valuemin={0}
                      aria-valuemax={100}
                      aria-valuenow={progress.percentage}
                    >
                      <span style={{ width: `${progress.percentage}%` }} />
                    </div>
                    <small>{progress.percentage}% concluído</small>
                  </div>
                </Card>
              )
            })}
          </section>
        </>
      )}
    </div>
  )
}
