import { PageHeader } from '../components/layout/PageHeader'

export function ProjetosPage() {
  return (
    <div className="page">
      <PageHeader
        eyebrow="Portfólio"
        title="Projetos"
        description="Consulte repositórios, branches base e convenções do time."
      />
      <section className="page-placeholder">
        <p>Os projetos cadastrados aparecerão aqui.</p>
      </section>
    </div>
  )
}
