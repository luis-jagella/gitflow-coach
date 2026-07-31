import { PageHeader } from '../components/layout/PageHeader'

export function DashboardPage() {
  return (
    <div className="page">
      <PageHeader
        eyebrow="Visão geral"
        title="Dashboard"
        description="Acompanhe os projetos, tarefas e o ritmo do fluxo de desenvolvimento."
      />
      <section className="page-placeholder">
        <p>Os indicadores do seu fluxo aparecerão aqui.</p>
      </section>
    </div>
  )
}
