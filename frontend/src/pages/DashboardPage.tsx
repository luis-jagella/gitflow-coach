import { PageHeader } from '../components/layout/PageHeader'
import { EmptyState } from '../components/feedback/EmptyState'
import { Card } from '../components/ui/Card'

export function DashboardPage() {
  return (
    <div className="page">
      <PageHeader
        eyebrow="Visão geral"
        title="Dashboard"
        description="Acompanhe os projetos, tarefas e o ritmo do fluxo de desenvolvimento."
      />
      <Card>
        <EmptyState
          title="Seu fluxo começa aqui"
          description="Os indicadores aparecerão assim que a API retornar projetos e tarefas."
        />
      </Card>
    </div>
  )
}
