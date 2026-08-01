import type { ApiErrorResponse } from '../types/api'

const configuredBaseUrl = import.meta.env.VITE_API_BASE_URL ?? '/api'
const apiBaseUrl = configuredBaseUrl.replace(/\/$/, '')

export class ApiRequestError extends Error {
  readonly status: number
  readonly details: ApiErrorResponse | null

  constructor(status: number, message: string, details: ApiErrorResponse | null = null) {
    super(message)
    this.name = 'ApiRequestError'
    this.status = status
    this.details = details
  }
}

function isRecord(value: unknown): value is Record<string, unknown> {
  return typeof value === 'object' && value !== null
}

function isStringRecord(value: unknown): value is Record<string, string> {
  return (
    isRecord(value) && Object.values(value).every((fieldMessage) => typeof fieldMessage === 'string')
  )
}

function isApiErrorResponse(value: unknown): value is ApiErrorResponse {
  if (!isRecord(value)) {
    return false
  }

  const camposValidos = value.campos === null || isStringRecord(value.campos)

  return (
    typeof value.timestamp === 'string' &&
    typeof value.status === 'number' &&
    typeof value.erro === 'string' &&
    typeof value.mensagem === 'string' &&
    typeof value.caminho === 'string' &&
    camposValidos
  )
}

async function parseResponseBody(response: Response): Promise<unknown> {
  if (response.status === 204) {
    return undefined
  }

  const text = await response.text()
  if (!text) {
    return undefined
  }

  const contentType = response.headers.get('content-type') ?? ''
  if (contentType.includes('application/json')) {
    return JSON.parse(text) as unknown
  }

  return text
}

export async function apiRequest<T>(path: string, options: RequestInit = {}): Promise<T> {
  const normalizedPath = path.startsWith('/') ? path : `/${path}`
  const headers = new Headers(options.headers)

  if (options.body && !headers.has('Content-Type')) {
    headers.set('Content-Type', 'application/json')
  }

  const response = await fetch(`${apiBaseUrl}${normalizedPath}`, {
    ...options,
    headers,
  })
  const body = await parseResponseBody(response)

  if (!response.ok) {
    if (isApiErrorResponse(body)) {
      throw new ApiRequestError(response.status, body.mensagem, body)
    }

    throw new ApiRequestError(response.status, `A API retornou o status ${response.status}`)
  }

  return body as T
}

export const api = {
  get<T>(path: string): Promise<T> {
    return apiRequest<T>(path)
  },

  post<TRequest, TResponse>(path: string, body: TRequest): Promise<TResponse> {
    return apiRequest<TResponse>(path, {
      method: 'POST',
      body: JSON.stringify(body),
    })
  },
}
