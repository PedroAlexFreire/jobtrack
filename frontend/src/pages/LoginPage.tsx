import { zodResolver } from '@hookform/resolvers/zod'
import { BriefcaseBusiness, Loader2, LogIn } from 'lucide-react'
import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { z } from 'zod'
import { useAuth } from '../auth/AuthContext'

const loginSchema = z.object({
  email: z.string().email('Enter a valid email address'),
  password: z.string().min(1, 'Password is required'),
})

type LoginFormValues = z.infer<typeof loginSchema>

export function LoginPage() {
  const { login } = useAuth()
  const [loginError, setLoginError] = useState<string | null>(null)

  const {
    formState: { errors, isSubmitting },
    handleSubmit,
    register,
  } = useForm<LoginFormValues>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      email: '',
      password: '',
    },
  })

  async function onSubmit(values: LoginFormValues) {
    setLoginError(null)

    try {
      await login(values.email, values.password)
    } catch {
      setLoginError('Invalid email or password')
    }
  }

  return (
    <main className="flex min-h-screen bg-slate-100 text-slate-950">
      <section className="hidden min-h-screen flex-1 flex-col justify-between bg-slate-950 px-10 py-8 text-white lg:flex">
        <div className="flex items-center gap-3">
          <div className="flex size-10 items-center justify-center rounded-lg bg-white text-slate-950">
            <BriefcaseBusiness size={20} />
          </div>
          <div>
            <p className="text-sm font-semibold">JobTrack</p>
            <p className="text-xs text-slate-400">Application tracker</p>
          </div>
        </div>

        <div className="max-w-lg">
          <p className="text-sm font-medium uppercase tracking-normal text-slate-400">
            Portfolio-grade workflow
          </p>
          <h1 className="mt-4 text-4xl font-semibold tracking-normal">
            Track applications with the same structure used by real product teams.
          </h1>
          <p className="mt-5 text-base leading-7 text-slate-300">
            Authentication, protected data, filters, search and pagination are handled
            by the Spring Boot API behind this interface.
          </p>
        </div>

        <p className="text-sm text-slate-500">Spring Boot + React + TypeScript</p>
      </section>

      <section className="flex min-h-screen flex-1 items-center justify-center px-4 py-8">
        <div className="w-full max-w-md border border-slate-200 bg-white p-6 shadow-sm">
          <div className="mb-8 lg:hidden">
            <div className="flex items-center gap-3">
              <div className="flex size-10 items-center justify-center rounded-lg bg-slate-950 text-white">
                <BriefcaseBusiness size={20} />
              </div>
              <div>
                <p className="text-sm font-semibold text-slate-950">JobTrack</p>
                <p className="text-xs text-slate-500">Application tracker</p>
              </div>
            </div>
          </div>

          <div>
            <h2 className="text-2xl font-semibold tracking-normal text-slate-950">
              Sign in
            </h2>
            <p className="mt-2 text-sm text-slate-500">
              Use your JobTrack account to access your applications.
            </p>
          </div>

          <form className="mt-8 space-y-5" onSubmit={handleSubmit(onSubmit)}>
            <div>
              <label
                className="mb-2 block text-sm font-medium text-slate-700"
                htmlFor="email"
              >
                Email
              </label>
              <input
                id="email"
                className="h-11 w-full rounded-md border border-slate-300 bg-white px-3 text-sm text-slate-950 outline-none focus:border-slate-500"
                placeholder="pedro@example.com"
                type="email"
                {...register('email')}
              />
              {errors.email ? (
                <p className="mt-2 text-sm text-rose-600">{errors.email.message}</p>
              ) : null}
            </div>

            <div>
              <label
                className="mb-2 block text-sm font-medium text-slate-700"
                htmlFor="password"
              >
                Password
              </label>
              <input
                id="password"
                className="h-11 w-full rounded-md border border-slate-300 bg-white px-3 text-sm text-slate-950 outline-none focus:border-slate-500"
                placeholder="Your password"
                type="password"
                {...register('password')}
              />
              {errors.password ? (
                <p className="mt-2 text-sm text-rose-600">
                  {errors.password.message}
                </p>
              ) : null}
            </div>

            {loginError ? (
              <div className="border border-rose-200 bg-rose-50 px-3 py-2 text-sm text-rose-700">
                {loginError}
              </div>
            ) : null}

            <button
              className="inline-flex h-11 w-full items-center justify-center gap-2 rounded-md bg-slate-950 px-4 text-sm font-semibold text-white hover:bg-slate-800 disabled:opacity-60"
              disabled={isSubmitting}
              type="submit"
            >
              {isSubmitting ? <Loader2 className="animate-spin" size={17} /> : <LogIn size={17} />}
              Sign in
            </button>
          </form>
        </div>
      </section>
    </main>
  )
}