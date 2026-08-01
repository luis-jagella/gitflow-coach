import { type FormEvent, useState } from 'react'
import { PageHeader } from '../components/layout/PageHeader'
import { Badge } from '../components/ui/Badge'
import { Card } from '../components/ui/Card'
import { api } from '../services/api'
import type { CommitValidationRequest, CommitValidationResponse } from '../types/api'

const exampleMessage = 'feat: adiciona interface de acompanhamento'

export function CommitValidatorPage() {
  const [message, setMessage] = useState('')
  const [result, setResult] = useState<CommitValidationResponse | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [apiConnected, setApiConnected] = useState(false)
  const [copyFeedback, setCopyFeedback] = useState<string | null>(null)

  async function validateCommit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setLoading(true)
    setError(null)
    setCopyFeedback(null)

    try {
      const response = await api.post<CommitValidationRequest, CommitValidationResponse>(
        '/commits/validar',
        { mensagem: message },
      )
      setResult(response)
      setApiConnected(true)
    } catch (requestError) {
      setResult(null)
      setApiConnected(false)
      setError(
        requestError instanceof Error
          ? requestError.message
          : 'Ocorreu um erro inesperado ao validar a mensagem.',
      )
    } finally {
      setLoading(false)
    }
  }

  async function copySuggestion() {
    if (!result?.sugestao) {
      return
    }

    try {
      await navigator.clipboard.writeText(result.sugestao)
      setCopyFeedback('Sugestão copiada')
    } catch {
      setCopyFeedback('Não foi possível copiar a sugestão')
    }
  }

  function applySuggestion() {
    if (!result?.sugestao) {
      return
    }

    setMessage(result.sugestao)
    setResult(null)
    setCopyFeedback(null)
  }

  return (
    <div className="page commit-page">
      <PageHeader
        eyebrow="Qualidade"
        title="Validar commit"
        description="Confira mensagens antes de registrar mudanças no repositório."
        apiConnected={apiConnected}
      />

      <div className="validator-grid">
        <Card className="validator-form-card">
          <div className="section-heading validator-heading">
            <div>
              <span>Conventional Commits</span>
              <h2>Analise sua mensagem</h2>
            </div>
          </div>

          <form className="validator-form" onSubmit={validateCommit}>
            <div className="field">
              <label htmlFor="commit-message">Mensagem do commit</label>
              <input
                className="input"
                id="commit-message"
                name="mensagem"
                value={message}
                onChange={(event) => setMessage(event.target.value)}
                placeholder="tipo: descrição objetiva"
                autoComplete="off"
              />
              <span className="field__hint">
                Exemplo: <code>{exampleMessage}</code>
              </span>
            </div>

            {error && (
              <div className="inline-alert inline-alert--error" role="alert">
                <strong>Não foi possível validar</strong>
                <span>{error}</span>
              </div>
            )}

            <button className="button button--primary" type="submit" disabled={loading}>
              {loading ? 'Validando...' : 'Validar mensagem'}
            </button>
          </form>
        </Card>

        <Card className="validator-result-card">
          {!result && (
            <div className="validator-waiting">
              <span aria-hidden="true">{'{ }'}</span>
              <h2>Aguardando validação</h2>
              <p>O resultado semântico retornado pela API será exibido aqui.</p>
            </div>
          )}

          {result && (
            <div className="validation-result" aria-live="polite">
              <div className="validation-result__heading">
                <Badge variant={result.valida ? 'success' : 'error'}>
                  {result.valida ? 'Mensagem válida' : 'Mensagem inválida'}
                </Badge>
                {result.tipo && <code>{result.tipo}</code>}
              </div>

              <h2>{result.valida ? 'Tudo certo para o commit' : 'A mensagem precisa de ajuste'}</h2>

              {result.descricao && (
                <div className="validation-detail">
                  <span>Descrição identificada</span>
                  <p>{result.descricao}</p>
                </div>
              )}

              {result.motivo && (
                <div className="validation-detail">
                  <span>Motivo</span>
                  <p>{result.motivo}</p>
                </div>
              )}

              {result.sugestao && (
                <div className="suggestion-block">
                  <span>Sugestão da API</span>
                  <pre className="code-block">{result.sugestao}</pre>
                  <div className="suggestion-actions">
                    <button className="button button--secondary" type="button" onClick={applySuggestion}>
                      Aplicar sugestão
                    </button>
                    <button className="button button--ghost" type="button" onClick={() => void copySuggestion()}>
                      Copiar sugestão
                    </button>
                  </div>
                  <small className="copy-result" aria-live="polite">{copyFeedback}</small>
                </div>
              )}
            </div>
          )}
        </Card>
      </div>
    </div>
  )
}
