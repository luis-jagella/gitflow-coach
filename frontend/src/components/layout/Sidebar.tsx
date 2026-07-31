import { NavLink } from 'react-router'

const navigation = [
  { to: '/', label: 'Dashboard', symbol: 'D' },
  { to: '/projetos', label: 'Projetos', symbol: 'P' },
  { to: '/tarefas', label: 'Tarefas', symbol: 'T' },
  { to: '/commits', label: 'Validar commit', symbol: 'C' },
]

export function Sidebar() {
  return (
    <aside className="sidebar">
      <NavLink className="brand" to="/" aria-label="GitFlow Coach - início">
        <span className="brand__mark">GF</span>
        <span>
          <strong>GitFlow</strong>
          <small>Coach</small>
        </span>
      </NavLink>

      <nav className="sidebar__nav" aria-label="Navegação principal">
        {navigation.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            end={item.to === '/'}
            className={({ isActive }) =>
              `sidebar__link${isActive ? ' sidebar__link--active' : ''}`
            }
          >
            <span className="sidebar__symbol" aria-hidden="true">
              {item.symbol}
            </span>
            <span>{item.label}</span>
          </NavLink>
        ))}
      </nav>

      <div className="sidebar__footer">
        <span className="sidebar__footer-dot" aria-hidden="true" />
        <span>Ambiente local</span>
      </div>
    </aside>
  )
}
