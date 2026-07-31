import { PageHeader } from '../components/layout/PageHeader'

export function CommitValidatorPage() {
  return (
    <div className="page">
      <PageHeader
        eyebrow="Qualidade"
        title="Validar commit"
        description="Confira mensagens antes de registrar mudanças no repositório."
      />
      <section className="page-placeholder">
        <p>O validador de mensagens aparecerá aqui.</p>
      </section>
    </div>
  )
}
