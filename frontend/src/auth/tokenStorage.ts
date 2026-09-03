const ACCESS_TOKEN_KEY = 'jobtrack.accessToken'

export function getStoredAccessToken() {
    return localStorage.getItem(ACCESS_TOKEN_KEY)
}

export function storeAccessToken(token: string) {
    localStorage.setItem(ACCESS_TOKEN_KEY, token)
}

export function clearStoredAccessToken() {
    localStorage.removeItem(ACCESS_TOKEN_KEY)
}