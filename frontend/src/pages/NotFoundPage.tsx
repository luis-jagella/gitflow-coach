import { Link } from 'react-router'
import { PageHeader } from '../components/layout/PageHeader'

export function NotFoundPage() {
  return (
    <div className="page">
      <PageHeader
        eyebrow="Erro 404"
        title="Página não encontrada"
        description="O endereço acessado não existe ou foi movido."
      />
      <section className="page-placeholder">
        <Link to="/">Voltar ao dashboard</Link>
      </section>
    </div>
  )
}
