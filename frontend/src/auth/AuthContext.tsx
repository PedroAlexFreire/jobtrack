import {
  createContext,
  useCallback,
  useContext,
  useMemo,
  useState,
  type ReactNode,
} from 'react'
import { login as loginRequest } from '../api/auth'
import {
  clearStoredAccessToken,
  getStoredAccessToken,
  storeAccessToken,
} from './tokenStorage'

type AuthContextValue = {
  accessToken: string | null
  isAuthenticated: boolean
  login: (email: string, password: string) => Promise<void>
  logout: () => void
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [accessToken, setAccessToken] = useState<string | null>(() =>
    getStoredAccessToken(),
  )

  const login = useCallback(async (email: string, password: string) => {
    const response = await loginRequest({ email, password })

    storeAccessToken(response.accessToken)
    setAccessToken(response.accessToken)
  }, [])

  const logout = useCallback(() => {
    clearStoredAccessToken()
    setAccessToken(null)
  }, [])

  const value = useMemo(
    () => ({
      accessToken,
      isAuthenticated: accessToken !== null,
      login,
      logout,
    }),
    [accessToken, login, logout],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)

  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider')
  }

  return context
}