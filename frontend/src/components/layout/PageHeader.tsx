interface PageHeaderProps {
  eyebrow?: string
  title: string
  description: string
  apiConnected?: boolean
}

export function PageHeader({
  eyebrow = 'GitFlow Coach',
  title,
  description,
  apiConnected,
}: PageHeaderProps) {
  return (
    <header className="page-header">
      <div>
        <span className="page-header__eyebrow">{eyebrow}</span>
        <h1>{title}</h1>
        <p>{description}</p>
      </div>

      {apiConnected === true && (
        <span className="api-status" role="status">
          <span className="api-status__dot" aria-hidden="true" />
          API conectada
        </span>
      )}
    </header>
  )
}
