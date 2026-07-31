import { PageHeader } from '../components/layout/PageHeader'
import { EmptyState } from '../components/feedback/EmptyState'
import { Card } from '../components/ui/Card'

export function TarefasPage() {
  return (
    <div className="page">
      <PageHeader
        eyebrow="Execução"
        title="Tarefas"
        description="Visualize branches sugeridas e o progresso dos checklists."
      />
      <Card>
        <EmptyState
          title="Nenhuma tarefa para exibir"
          description="As tarefas cadastradas na API aparecerão nesta página."
        />
      </Card>
    </div>
  )
}
