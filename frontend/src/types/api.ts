export interface ProjetoResponse {
  id: number
  nome: string
  repositorio: string
  branchBase: string
}

export interface ChecklistItemResponse {
  id: number
  descricao: string
  concluido: boolean
  ordem: number
}

export interface GitCommandResponse {
  ordem: number
  descricao: string
  comando: string
}

export interface TarefaResponse {
  id: number
  codigo: string
  titulo: string
  descricao: string
  branchSugerida: string
  projetoId: number
  projetoNome: string
  checklist: ChecklistItemResponse[]
  comandosGit: GitCommandResponse[]
}

export interface CommitValidationRequest {
  mensagem: string
}

export interface CommitValidationResponse {
  valida: boolean
  tipo: string | null
  descricao: string | null
  motivo: string | null
  sugestao: string | null
}

export interface ApiErrorResponse {
  timestamp: string
  status: number
  erro: string
  mensagem: string
  caminho: string
  campos: Record<string, string> | null
}
