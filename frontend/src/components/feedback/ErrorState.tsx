interface ErrorStateProps {
  message: string
  onRetry?: () => void
}

export function ErrorState({ message, onRetry }: ErrorStateProps) {
  return (
    <div className="feedback-state feedback-state--error" role="alert">
      <span className="feedback-state__icon" aria-hidden="true">
        !
      </span>
      <h2>Não foi possível carregar</h2>
      <p>{message}</p>
      {onRetry && (
        <button className="button button--secondary" type="button" onClick={onRetry}>
          Tentar novamente
        </button>
      )}
    </div>
  )
}
