import { apiGet } from './apiClient'
import type {
  ApplicationStatus,
  JobApplication,
  PaginatedResponse,
} from '../types/application'

export type GetApplicationsParams = {
  token: string
  page?: number
  size?: number
  status?: ApplicationStatus
  search?: string
}

export function getApplications({
  token,
  page = 0,
  size = 10,
  status,
  search,
}: GetApplicationsParams) {
  return apiGet<PaginatedResponse<JobApplication>>('/api/applications', {
    token,
    params: {
      page,
      size,
      status,
      search,
    },
  })
}