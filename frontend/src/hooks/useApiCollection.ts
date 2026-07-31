import { useCallback, useEffect, useState } from 'react'
import { api } from '../services/api'

export function useApiCollection<T>(path: string) {
  const [data, setData] = useState<T[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [apiConnected, setApiConnected] = useState(false)

  const reload = useCallback(async () => {
    setLoading(true)
    setError(null)

    try {
      setData(await api.get<T[]>(path))
      setApiConnected(true)
    } catch (requestError) {
      setApiConnected(false)
      setError(
        requestError instanceof Error
          ? requestError.message
          : 'Ocorreu um erro inesperado ao consultar a API.',
      )
    } finally {
      setLoading(false)
    }
  }, [path])

  useEffect(() => {
    void reload()
  }, [reload])

  return { data, loading, error, apiConnected, reload }
}
