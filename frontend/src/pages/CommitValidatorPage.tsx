import { PageHeader } from '../components/layout/PageHeader'
import { Card } from '../components/ui/Card'

export function CommitValidatorPage() {
  return (
    <div className="page">
      <PageHeader
        eyebrow="Qualidade"
        title="Validar commit"
        description="Confira mensagens antes de registrar mudanças no repositório."
      />
      <Card className="page-placeholder">
        <p>O validador de mensagens aparecerá aqui.</p>
      </Card>
    </div>
  )
}
