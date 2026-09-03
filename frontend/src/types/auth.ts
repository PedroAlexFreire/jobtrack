export type LoginRequest = {
  email: string
  password: string
}

export type RegisterRequest = {
  name: string
  email: string
  password: string
}

export type TokenResponse = {
  accessToken: string
  tokenType: string
  expiresIn: number
}

export type UserResponse = {
  id: number
  name: string
  email: string
}