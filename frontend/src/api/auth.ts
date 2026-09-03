import { apiPost } from './apiClient'
import type {
    LoginRequest,
    RegisterRequest,
    TokenResponse,
    UserResponse,
} from '../types/auth'

export function login(request: LoginRequest) {
    return apiPost<TokenResponse, LoginRequest>('/api/auth/login', request)
}

export function register(request: RegisterRequest) {
    return apiPost<UserResponse, RegisterRequest>('/api/auth/register', request)
}