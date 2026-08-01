import { Link } from 'react-router'
import { EmptyState } from '../components/feedback/EmptyState'
import { PageHeader } from '../components/layout/PageHeader'
import { Card } from '../components/ui/Card'

export function NotFoundPage() {
  return (
    <div className="page">
      <PageHeader
        eyebrow="Erro 404"
        title="Página não encontrada"
        description="O endereço acessado não existe ou foi movido."
      />
      <Card>
        <EmptyState
          title="Este caminho não leva a lugar algum"
          description="Use a navegação ou retorne ao dashboard."
          action={
            <Link className="button button--primary" to="/">
              Voltar ao dashboard
            </Link>
          }
        />
      </Card>
    </div>
  )
}
