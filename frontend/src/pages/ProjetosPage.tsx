import { EmptyState } from '../components/feedback/EmptyState'
import { ErrorState } from '../components/feedback/ErrorState'
import { LoadingState } from '../components/feedback/LoadingState'
import { PageHeader } from '../components/layout/PageHeader'
import { Badge } from '../components/ui/Badge'
import { Card } from '../components/ui/Card'
import { useApiCollection } from '../hooks/useApiCollection'
import type { ProjetoResponse } from '../types/api'

export function ProjetosPage() {
  const { data: projetos, loading, error, apiConnected, reload } =
    useApiCollection<ProjetoResponse>('/projetos')

  return (
    <div className="page">
      <PageHeader
        eyebrow="Portfólio"
        title="Projetos"
        description="Consulte repositórios, branches base e convenções do time."
        apiConnected={apiConnected}
      />

      {loading && (
        <Card>
          <LoadingState message="Carregando projetos..." />
        </Card>
      )}

      {!loading && error && (
        <Card>
          <ErrorState message={error} onRetry={() => void reload()} />
        </Card>
      )}

      {!loading && !error && projetos.length === 0 && (
        <Card>
          <EmptyState
            title="Nenhum projeto para exibir"
            description="Os projetos cadastrados na API aparecerão nesta página."
          />
        </Card>
      )}

      {!loading && !error && projetos.length > 0 && (
        <section className="resource-grid" aria-label="Projetos cadastrados">
          {projetos.map((projeto) => (
            <Card className="resource-card project-card" key={projeto.id}>
              <div className="resource-card__topline">
                <Badge variant="accent">Projeto #{projeto.id}</Badge>
                <span>branch base</span>
              </div>
              <div className="resource-card__title">
                <h2>{projeto.nome}</h2>
                <p>{projeto.repositorio}</p>
              </div>
              <code className="branch-label">{projeto.branchBase}</code>
            </Card>
          ))}
        </section>
      )}
    </div>
  )
}
