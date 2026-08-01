interface LoadingStateProps {
  message?: string
}

export function LoadingState({ message = 'Carregando dados...' }: LoadingStateProps) {
  return (
    <div className="feedback-state" role="status" aria-live="polite">
      <span className="loading-indicator" aria-hidden="true" />
      <p>{message}</p>
    </div>
  )
}
