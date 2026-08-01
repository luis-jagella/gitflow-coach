import { render, screen, within } from '@testing-library/react'
import { MemoryRouter } from 'react-router'
import { afterEach, describe, expect, it, vi } from 'vitest'
import { DashboardPage } from './DashboardPage'
import { jsonResponse } from '../test/response'
import type { ProjetoResponse, TarefaResponse } from '../types/api'

const projetos: ProjetoResponse[] = [
  { id: 1, nome: 'Coach API', repositorio: 'org/coach-api', branchBase: 'main' },
  { id: 2, nome: 'Portal', repositorio: 'org/portal', branchBase: 'develop' },
]

const tarefas: TarefaResponse[] = [
  {
    id: 1,
    codigo: 'TASK-1',
    titulo: 'Criar dashboard',
    descricao: 'Interface inicial',
    branchSugerida: 'feat/1-dashboard',
    projetoId: 2,
    projetoNome: 'Portal',
    checklist: [
      { id: 1, descricao: 'Implementar', concluido: true, ordem: 1 },
      { id: 2, descricao: 'Testar', concluido: true, ordem: 2 },
    ],
    comandosGit: [],
  },
  {
    id: 2,
    codigo: 'TASK-2',
    titulo: 'Revisar interface',
    descricao: 'Ajustes visuais',
    branchSugerida: 'feat/2-revisao',
    projetoId: 2,
    projetoNome: 'Portal',
    checklist: [
      { id: 3, descricao: 'Implementar', concluido: true, ordem: 1 },
      { id: 4, descricao: 'Testar', concluido: false, ordem: 2 },
    ],
    comandosGit: [],
  },
]

describe('DashboardPage', () => {
  afterEach(() => {
    vi.unstubAllGlobals()
  })

  it('calcula os indicadores com os projetos e checklists retornados pela API', async () => {
    const fetchMock = vi
      .fn()
      .mockResolvedValueOnce(jsonResponse(projetos))
      .mockResolvedValueOnce(jsonResponse(tarefas))
    vi.stubGlobal('fetch', fetchMock)

    render(
      <MemoryRouter>
        <DashboardPage />
      </MemoryRouter>,
    )

    const metrics = await screen.findByLabelText('Indicadores do fluxo')
    const projetosMetric = within(metrics).getByText('Projetos', { selector: '.metric-card > span' })
      .parentElement
    const tarefasMetric = within(metrics).getByText('Tarefas', { selector: '.metric-card > span' })
      .parentElement
    const concluidasMetric = within(metrics).getByText('Concluídas').parentElement
    const andamentoMetric = within(metrics).getByText('Em andamento').parentElement

    expect(within(projetosMetric as HTMLElement).getByText('2', { selector: 'strong' })).toBeVisible()
    expect(within(tarefasMetric as HTMLElement).getByText('2', { selector: 'strong' })).toBeVisible()
    expect(within(concluidasMetric as HTMLElement).getByText('1', { selector: 'strong' })).toBeVisible()
    expect(within(andamentoMetric as HTMLElement).getByText('1', { selector: 'strong' })).toBeVisible()
    expect(screen.getByText('API conectada')).toBeVisible()
  })
})
