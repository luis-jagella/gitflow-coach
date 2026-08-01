import type { ReactNode } from 'react'

interface EmptyStateProps {
  title: string
  description: string
  action?: ReactNode
}

export function EmptyState({ title, description, action }: EmptyStateProps) {
  return (
    <div className="feedback-state feedback-state--empty">
      <span className="feedback-state__icon" aria-hidden="true">
        ∅
      </span>
      <h2>{title}</h2>
      <p>{description}</p>
      {action && <div className="feedback-state__action">{action}</div>}
    </div>
  )
}
