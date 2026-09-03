const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080'

type RequestOptions = {
    token?: string
    params?: Record<string, string | number | undefined>
}

export async function apiGet<T>(
    path: string,
    options: RequestOptions = {},
): Promise<T> {
    const url = new URL(path, API_BASE_URL)

    if (options.params) {
        Object.entries(options.params).forEach(([key, value]) => {
            if (value !== undefined) {
                url.searchParams.set(key, String(value))
            }
        })
    }

    const response = await fetch(url, {
        headers: {
            ...(options.token ? { Authorization: `Bearer ${options.token}` } : {}),
        },
    })

    if (!response.ok) {
        throw new Error(`Request failed with status ${response.status}`)
    }

    return response.json() as Promise<T>
}
export async function apiPost<TResponse, TBody>(
    path: string,
    body: TBody,
): Promise<TResponse> {
    const url = new URL(path, API_BASE_URL)

    const response = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(body),
    })

    if (!response.ok) {
        throw new Error(`Request failed with status ${response.status}`)
    }

    return response.json() as Promise<TResponse>
}