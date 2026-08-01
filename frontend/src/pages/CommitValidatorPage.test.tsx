import { fireEvent, render, screen, waitFor } from '@testing-library/react'
import { afterEach, describe, expect, it, vi } from 'vitest'
import { CommitValidatorPage } from './CommitValidatorPage'
import { jsonResponse } from '../test/response'
import type { CommitValidationResponse } from '../types/api'

const validResponse: CommitValidationResponse = {
  valida: true,
  tipo: 'feat',
  descricao: 'adiciona dashboard',
  motivo: null,
  sugestao: null,
}

const invalidResponse: CommitValidationResponse = {
  valida: false,
  tipo: 'feat',
  descricao: 'adiciona dashboard',
  motivo: 'A mensagem deve seguir o formato tipo: descrição',
  sugestao: 'feat: adiciona dashboard',
}

function fillAndSubmit(message: string) {
  fireEvent.change(screen.getByLabelText('Mensagem do commit'), { target: { value: message } })
  fireEvent.click(screen.getByRole('button', { name: 'Validar mensagem' }))
}

describe('CommitValidatorPage', () => {
  afterEach(() => {
    vi.unstubAllGlobals()
  })

  it('envia a mensagem informada ao endpoint de validação', async () => {
    const fetchMock = vi.fn().mockResolvedValue(jsonResponse(validResponse))
    vi.stubGlobal('fetch', fetchMock)
    render(<CommitValidatorPage />)

    fillAndSubmit('feat: adiciona dashboard')

    await waitFor(() => expect(fetchMock).toHaveBeenCalledOnce())
    expect(fetchMock).toHaveBeenCalledWith(
      '/api/commits/validar',
      expect.objectContaining({
        method: 'POST',
        body: JSON.stringify({ mensagem: 'feat: adiciona dashboard' }),
      }),
    )
  })

  it('apresenta o resultado válido devolvido pela API', async () => {
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue(jsonResponse(validResponse)))
    render(<CommitValidatorPage />)

    fillAndSubmit('feat: adiciona dashboard')

    expect(await screen.findByText('Mensagem válida')).toBeVisible()
    expect(screen.getByText('Tudo certo para o commit')).toBeVisible()
    expect(screen.getByText('adiciona dashboard')).toBeVisible()
  })

  it('apresenta e permite aplicar a sugestão de uma mensagem inválida', async () => {
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue(jsonResponse(invalidResponse)))
    render(<CommitValidatorPage />)

    fillAndSubmit('feat adiciona dashboard')

    expect(await screen.findByText('Mensagem inválida')).toBeVisible()
    expect(screen.getByText(invalidResponse.motivo as string)).toBeVisible()
    expect(screen.getByText(invalidResponse.sugestao as string)).toBeVisible()

    fireEvent.click(screen.getByRole('button', { name: 'Aplicar sugestão' }))

    expect(screen.getByLabelText('Mensagem do commit')).toHaveValue(invalidResponse.sugestao)
  })
})
