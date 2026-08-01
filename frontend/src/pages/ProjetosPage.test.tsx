import { render, screen } from '@testing-library/react'
import { afterEach, describe, expect, it, vi } from 'vitest'
import { ProjetosPage } from './ProjetosPage'
import { jsonResponse } from '../test/response'

describe('ProjetosPage', () => {
  afterEach(() => {
    vi.unstubAllGlobals()
  })

  it('exibe o estado vazio quando a API não retorna projetos', async () => {
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue(jsonResponse([])))

    render(<ProjetosPage />)

    expect(await screen.findByText('Nenhum projeto para exibir')).toBeVisible()
    expect(screen.getByText('API conectada')).toBeVisible()
  })
})
