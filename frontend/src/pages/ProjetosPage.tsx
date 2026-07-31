import { PageHeader } from '../components/layout/PageHeader'
import { EmptyState } from '../components/feedback/EmptyState'
import { Card } from '../components/ui/Card'

export function ProjetosPage() {
  return (
    <div className="page">
      <PageHeader
        eyebrow="Portfólio"
        title="Projetos"
        description="Consulte repositórios, branches base e convenções do time."
      />
      <Card>
        <EmptyState
          title="Nenhum projeto para exibir"
          description="Os projetos cadastrados na API aparecerão nesta página."
        />
      </Card>
    </div>
  )
}
