import { afterEach, describe, expect, it, vi } from 'vitest'
import { ApiRequestError, api } from './api'
import { jsonResponse } from '../test/response'

describe('cliente da API', () => {
  afterEach(() => {
    vi.unstubAllGlobals()
  })

  it('transforma uma resposta de erro padronizada em erro tipado', async () => {
    const errorBody = {
      timestamp: '2026-07-30T10:00:00Z',
      status: 404,
      erro: 'Not Found',
      mensagem: 'Tarefa não encontrada',
      caminho: '/tarefas/99',
      campos: null,
    }
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue(jsonResponse(errorBody, 404)))

    const request = api.get('/tarefas/99')

    await expect(request).rejects.toEqual(
      expect.objectContaining<ApiRequestError>({
        name: 'ApiRequestError',
        status: 404,
        message: 'Tarefa não encontrada',
        details: errorBody,
      }),
    )
  })
})
