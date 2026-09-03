import { useAuth } from './auth/AuthContext'
import { LoginPage } from './pages/LoginPage'
import {
  BarChart3,
  BriefcaseBusiness,
  CalendarDays,
  CheckCircle2,
  ClipboardList,
  Clock3,
  Search,
} from 'lucide-react'

const applications = [
  {
    id: 1,
    company: 'Microsoft',
    position: 'Junior Java Developer',
    status: 'APPLIED',
    applicationDate: '2026-08-30',
  },
  {
    id: 2,
    company: 'Google',
    position: 'Backend Developer',
    status: 'INTERVIEW',
    applicationDate: '2026-08-20',
  },
  {
    id: 3,
    company: 'Spotify',
    position: 'Software Engineer Intern',
    status: 'REJECTED',
    applicationDate: '2026-08-10',
  },
]

const statusStyles = {
  APPLIED: 'border-sky-200 bg-sky-50 text-sky-700',
  INTERVIEW: 'border-amber-200 bg-amber-50 text-amber-700',
  OFFER: 'border-emerald-200 bg-emerald-50 text-emerald-700',
  REJECTED: 'border-rose-200 bg-rose-50 text-rose-700',
}

function App() {
  const { isAuthenticated } = useAuth()

  if (!isAuthenticated) {
    return <LoginPage />
  }
  return (
    <main className="min-h-screen bg-[#f5f7fb] text-slate-900">
      <div className="mx-auto flex min-h-screen w-full max-w-7xl">
        <aside className="hidden w-64 border-r border-slate-200 bg-white px-5 py-6 lg:block">
          <div className="flex items-center gap-3">
            <div className="flex size-10 items-center justify-center rounded-lg bg-slate-900 text-white">
              <BriefcaseBusiness size={20} />
            </div>
            <div>
              <p className="text-sm font-semibold text-slate-900">JobTrack</p>
              <p className="text-xs text-slate-500">Application tracker</p>
            </div>
          </div>

          <nav className="mt-8 space-y-1">
            <a
              className="flex items-center gap-3 rounded-md bg-slate-100 px-3 py-2 text-sm font-medium text-slate-900"
              href="/"
            >
              <ClipboardList size={17} />
              Applications
            </a>
            <a
              className="flex items-center gap-3 rounded-md px-3 py-2 text-sm font-medium text-slate-500 hover:bg-slate-50 hover:text-slate-900"
              href="/"
            >
              <BarChart3 size={17} />
              Analytics
            </a>
          </nav>
        </aside>

        <section className="flex min-w-0 flex-1 flex-col">
          <header className="border-b border-slate-200 bg-white px-4 py-4 sm:px-6 lg:px-8">
            <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
              <div>
                <p className="text-sm font-medium text-slate-500">Dashboard</p>
                <h1 className="mt-1 text-2xl font-semibold tracking-normal text-slate-950">
                  Job applications
                </h1>
              </div>

              <button className="inline-flex items-center justify-center rounded-md bg-slate-900 px-4 py-2 text-sm font-semibold text-white shadow-sm hover:bg-slate-800">
                New application
              </button>
            </div>
          </header>

          <div className="grid gap-4 px-4 py-5 sm:grid-cols-2 sm:px-6 lg:grid-cols-4 lg:px-8">
            <MetricCard label="Total" value="3" icon={<BriefcaseBusiness size={18} />} />
            <MetricCard label="Interviews" value="1" icon={<CalendarDays size={18} />} />
            <MetricCard label="Offers" value="0" icon={<CheckCircle2 size={18} />} />
            <MetricCard label="Waiting" value="2" icon={<Clock3 size={18} />} />
          </div>

          <section className="px-4 pb-8 sm:px-6 lg:px-8">
            <div className="border border-slate-200 bg-white">
              <div className="flex flex-col gap-3 border-b border-slate-200 p-4 md:flex-row md:items-center md:justify-between">
                <div className="relative max-w-md flex-1">
                  <Search
                    className="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
                    size={17}
                  />
                  <input
                    className="h-10 w-full rounded-md border border-slate-300 bg-white pl-9 pr-3 text-sm text-slate-900 outline-none focus:border-slate-500"
                    placeholder="Search company or position"
                    type="search"
                  />
                </div>

                <select className="h-10 rounded-md border border-slate-300 bg-white px-3 text-sm text-slate-900 outline-none focus:border-slate-500">
                  <option>All statuses</option>
                  <option>Applied</option>
                  <option>Interview</option>
                  <option>Offer</option>
                  <option>Rejected</option>
                </select>
              </div>

              <div className="overflow-x-auto">
                <table className="w-full min-w-[760px] border-collapse text-left">
                  <thead className="bg-slate-50 text-xs uppercase text-slate-500">
                    <tr>
                      <th className="px-4 py-3 font-semibold">Company</th>
                      <th className="px-4 py-3 font-semibold">Position</th>
                      <th className="px-4 py-3 font-semibold">Status</th>
                      <th className="px-4 py-3 font-semibold">Applied on</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-slate-100">
                    {applications.map((application) => (
                      <tr key={application.id} className="hover:bg-slate-50">
                        <td className="px-4 py-4 text-sm font-medium text-slate-950">
                          {application.company}
                        </td>
                        <td className="px-4 py-4 text-sm text-slate-600">
                          {application.position}
                        </td>
                        <td className="px-4 py-4">
                          <span
                            className={`inline-flex items-center rounded-full border px-2.5 py-1 text-xs font-semibold ${statusStyles[
                              application.status as keyof typeof statusStyles
                            ]
                              }`}
                          >
                            {application.status}
                          </span>
                        </td>
                        <td className="px-4 py-4 text-sm text-slate-600">
                          {application.applicationDate}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>

              <footer className="flex items-center justify-between border-t border-slate-200 px-4 py-3 text-sm text-slate-500">
                <span>Showing 3 of 3 applications</span>
                <div className="flex items-center gap-2">
                  <button
                    className="rounded-md border border-slate-300 px-3 py-1.5 font-medium text-slate-600 disabled:opacity-50"
                    disabled
                  >
                    Previous
                  </button>
                  <button
                    className="rounded-md border border-slate-300 px-3 py-1.5 font-medium text-slate-600 disabled:opacity-50"
                    disabled
                  >
                    Next
                  </button>
                </div>
              </footer>
            </div>
          </section>
        </section>
      </div>
    </main>
  )
}

function MetricCard({
  label,
  value,
  icon,
}: {
  label: string
  value: string
  icon: React.ReactNode
}) {
  return (
    <div className="border border-slate-200 bg-white p-4">
      <div className="flex items-center justify-between">
        <p className="text-sm font-medium text-slate-500">{label}</p>
        <span className="text-slate-400">{icon}</span>
      </div>
      <p className="mt-3 text-2xl font-semibold tracking-normal text-slate-950">
        {value}
      </p>
    </div>
  )
}

export default App