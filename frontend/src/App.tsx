import { Route, Routes } from 'react-router'
import { AppLayout } from './components/layout/AppLayout'
import { CommitValidatorPage } from './pages/CommitValidatorPage'
import { DashboardPage } from './pages/DashboardPage'
import { NotFoundPage } from './pages/NotFoundPage'
import { ProjetosPage } from './pages/ProjetosPage'
import { TarefasPage } from './pages/TarefasPage'

function App() {
  return (
    <Routes>
      <Route element={<AppLayout />}>
        <Route index element={<DashboardPage />} />
        <Route path="projetos" element={<ProjetosPage />} />
        <Route path="tarefas" element={<TarefasPage />} />
        <Route path="commits" element={<CommitValidatorPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Route>
    </Routes>
  )
}

export default App
