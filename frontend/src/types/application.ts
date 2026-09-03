export type ApplicationStatus = 'APPLIED' | 'INTERVIEW' | 'OFFER' | 'REJECTED'

export type JobApplication = {
  id: number
  company: string
  position: string
  status: ApplicationStatus
  applicationDate: string
}

export type PageMetadata = {
  size: number
  number: number
  totalElements: number
  totalPages: number
}

export type PaginatedResponse<T> = {
  content: T[]
  page: PageMetadata
}