import { PageHeader } from '../components/layout/PageHeader'

export function TarefasPage() {
  return (
    <div className="page">
      <PageHeader
        eyebrow="Execução"
        title="Tarefas"
        description="Visualize branches sugeridas e o progresso dos checklists."
      />
      <section className="page-placeholder">
        <p>As tarefas do time aparecerão aqui.</p>
      </section>
    </div>
  )
}
