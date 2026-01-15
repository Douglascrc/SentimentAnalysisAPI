import React, { useState, useEffect } from 'react';
import {
  PieChart,
  Pie,
  Cell,
  ResponsiveContainer
} from 'recharts';
import {
  Activity,
  MessageSquare,
  Zap,
  Clock
} from 'lucide-react';

const App = () => {
  const [comment, setComment] = useState('');
  const [language, setLanguage] = useState('pt');
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);
  const [history, setHistory] = useState([]);

  const [filter, setFilter] = useState({
    language: 'all',
    sentiment: 'all',
    recent: 5
  });

  const [apiData, setApiData] = useState({
    statusApi: 'Offline',
    totalAnalises: 0,
    percentualPositivo: 0,
    percentualNegativo: 0,
    tempoMedioRespostaMs: 0
  });

  /* ===================== STATS ===================== */
  const fetchStats = async () => {
    try {
      const response = await fetch('http://localhost:8081/stats');
      if (response.ok) {
        const data = await response.json();
        setApiData(data);
      }
    } catch (error) {
      console.error("API Offline");
    }
  };

  useEffect(() => {
    fetchStats();
    const interval = setInterval(fetchStats, 5000);
    return () => clearInterval(interval);
  }, []);

  const handleAnalyze = async () => {
    if (!comment.trim()) return;
    setLoading(true);
    try {
      // Ajustado para bater exatamente no @RequestMapping("/sentiment") do seu SentimentController
      const response = await fetch('http://localhost:8081/sentiment', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ text: comment, language })
      });

      if (res.ok) {
        setResult(await res.json());
        setComment('');
        fetchStats();
        fetchHistory();
      }
    } catch (error) {
      console.error("Erro na análise");
    } finally {
      setLoading(false);
    }
  };

  /* ===================== PIE ===================== */
  const pieData = [
    { name: 'Positivo', value: apiData.percentualPositivo },
    { name: 'Negativo', value: apiData.percentualNegativo }
  ];

  // Helper para definir cores baseadas no sentimento recebido
  const getSentimentColor = (sentiment) => {
    const s = sentiment?.toUpperCase();
    if (s === 'POSITIVO') return 'text-green-600';
    if (s === 'NEGATIVO') return 'text-red-600';
    return 'text-indigo-600';
  };

  const getSentimentBg = (sentiment) => {
    const s = sentiment?.toUpperCase();
    if (s === 'POSITIVO') return 'border-green-200 bg-green-50';
    if (s === 'NEGATIVO') return 'border-red-200 bg-red-50';
    return 'border-indigo-200 bg-slate-50';
  };

  return (
    <div className="min-h-screen bg-slate-50 text-slate-900">

      {/* ===================== HEADER ===================== */}
      <header className="bg-gradient-to-r from-indigo-600 to-purple-600 text-white p-6 rounded-b-3xl mb-10">
        <div className="max-w-7xl mx-auto">
          <div className="flex items-center gap-3 mb-6">
            <div className="bg-white/20 p-3 rounded-xl">
              <MessageSquare />
            </div>
            <div>
              <h1 className="text-2xl font-bold">Análise de Sentimentos</h1>
              <p className="text-sm opacity-80">
                API de predição sentimentos para Insights de Marketing
              </p>
            </div>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
            <Stat title="Total analisados" value={apiData.totalAnalises} />
            <Stat
              title="Taxa de satisfação"
              value={
                apiData.totalAnalises === 0
                  ? '—'
                  : `${apiData.percentualPositivo}%`
              }
            />

            <Stat
              title="Sentimento dominante"
              value={
                apiData.totalAnalises === 0
                  ? '—'
                  : apiData.percentualPositivo >= 50
                    ? 'Positivo 😄'
                    : 'Negativo 😐'
              }
            />

          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4">

        {/* ===================== INFO CARDS ===================== */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
          <InfoCard title="Positivos" value={`${apiData.percentualPositivo}%`} icon={<Zap />} />
          <InfoCard title="Latência" value={`${apiData.tempoMedioRespostaMs}ms`} icon={<Clock />} />
          <InfoCard
            title="Status"
            value={apiData.statusApi}
            icon={<Activity />}
            highlight={apiData.statusApi === 'UP'}
          />
        </div>

        {/* ===================== GRID PRINCIPAL ===================== */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">

          {/* GRÁFICO */}          <div className="bg-white rounded-2xl shadow p-6">
            <h2 className="font-bold text-lg mb-4">Distribuição de Sentimentos</h2>

            {apiData.totalAnalises === 0 ? (
              <div className="h-[260px] flex flex-col items-center justify-center text-slate-400 text-sm">
                <MessageSquare className="mb-2" />
                Ainda não há dados suficientes para exibir o gráfico
              </div>
            ) : (
              <>
                <ResponsiveContainer width="100%" height={260}>
                  <PieChart>
                    <Pie
                      data={pieData}
                      dataKey="value"
                      innerRadius={60}
                      outerRadius={90}
                      paddingAngle={3}
                    >
                      <Cell fill="#22c55e" />
                      <Cell fill="#ef4444" />
                    </Pie>
                  </PieChart>
                </ResponsiveContainer>

                <div className="flex justify-between mt-6">
                  <Badge color="green">Positivos: {apiData.percentualPositivo}%</Badge>
                  <Badge color="red">Negativos: {apiData.percentualNegativo}%</Badge>
                </div>
              </>
            )}
          </div>


          {/* ANALISADOR */}
          <div className="bg-white rounded-2xl shadow p-6">
            <h2 className="font-bold text-lg mb-4">Analisador de Sentimentos</h2>

            <select
              value={language}
              onChange={e => setLanguage(e.target.value)}
              className="w-full mb-3 p-2 border rounded-lg text-sm"
            >
              <option value="pt">🇧🇷 Português</option>
              <option value="en">🇺🇸 Inglês</option>
              <option value="es">🇪🇸 Espanhol</option>
            </select>

            <textarea
              className="w-full p-3 border rounded-lg text-sm mb-4"
              rows={4}
              placeholder="Ex: Eu adorei o serviço, foi excelente!"
              value={comment}
              onChange={e => setComment(e.target.value)}
            />

            <button
              onClick={handleAnalyze}
              disabled={loading}
              className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-3 rounded-lg"
            >
              {loading ? 'Analisando...' : 'Analisar Texto'}
            </button>
          </div>

          {/* ===================== HISTÓRICO ===================== */}
          <div className="lg:col-span-2 bg-white rounded-2xl shadow p-6">
            <h3 className="font-bold text-lg mb-4">Histórico de Comentários</h3>

            <div className="flex gap-2 mb-4 flex-wrap">
              <select
                value={filter.language}
                onChange={e => setFilter({ ...filter, language: e.target.value })}
                className="p-2 border rounded text-sm"
              >
                <option value="all">Todos idiomas</option>
                <option value="pt">🇧🇷 Português</option>
                <option value="en">🇺🇸 Inglês</option>
                <option value="es">🇪🇸 Espanhol</option>
              </select>

              <select
                value={filter.sentiment}
                onChange={e => setFilter({ ...filter, sentiment: e.target.value })}
                className="p-2 border rounded text-sm"
              >
                <option value="all">Todos sentimentos</option>
                <option value="POSITIVO">Positivo</option>
                <option value="NEGATIVO">Negativo</option>
              </select>

              <select
                value={filter.recent}
                onChange={e => setFilter({ ...filter, recent: Number(e.target.value) })}
                className="p-2 border rounded text-sm"
              >
                <option value={5}>Últimos 5</option>
                <option value={10}>Últimos 10</option>
                <option value={20}>Últimos 20</option>
              </select>
            </div>

            <div className="bg-slate-50 p-3 rounded border h-64 overflow-y-auto">
              {history.length === 0 ? (
                <p className="text-center text-sm text-slate-500">
                  Nenhum comentário encontrado
                </p>
              ) : (
                <ul className="divide-y">
                  {history.map((c, i) => (
                   <li key={i} className="py-2 flex justify-between">
                      <div>
                        <p className="text-sm">{c.text}</p>
                        <p className="text-xs text-slate-400">
                          {c.language.toUpperCase()} • {new Date(c.created_at).toLocaleString()}
                        </p>
                      </div>
                      <span className={`text-xs font-bold px-2 py-1 rounded ${
                        c.sentiment === 'POSITIVO'
                          ? 'bg-green-100 text-green-700'
                          : 'bg-re d-100 text-red-700'
                      }`}>
                        {c.sentiment}
                      </span>
                    </li>
                  ))}
                </ul>
              )}
            </div>

          </div>

        </div>

      </main>
      {/* ===================== FOOTER ===================== */}
<footer className="mt-16 border-t bg-white">
  <div className="max-w-7xl mx-auto px-4 py-6 flex flex-col sm:flex-row items-center justify-between gap-4 text-sm text-slate-500">

    <div className="flex items-center gap-2">
      <MessageSquare className="w-4 h-4 text-indigo-600" />
      <span className="font-semibold text-slate-700">
        Análise de Sentimentos
      </span>
      <span className="text-slate-400">• Dashboard</span>
    </div>

    <div className="flex items-center gap-4">
      <span>
        Status da API:{' '}
        <span
          className={`font-semibold ${
            apiData.statusApi === 'UP'
              ? 'text-green-600'
              : 'text-red-500'
          }`}
        >
          {apiData.statusApi}
        </span>
      </span>

      <span className="hidden sm:inline">|</span>

      <span>
        © {new Date().getFullYear()} • Hackatoon ONE
      </span>
    </div>

  </div>
</footer>

    </div>
  );
};

/* ===================== COMPONENTES ===================== */

const Stat = ({ title, value }) => (
  <div className="bg-white/15 rounded-xl p-4">
    <p className="text-sm opacity-80">{title}</p>
    <p className="text-2xl font-bold">{value}</p>
  </div>
);

const InfoCard = ({ title, value, icon, highlight }) => (
  <div className="bg-white rounded-xl shadow p-5 flex justify-between items-center">
    <div>
      <p className="text-xs text-slate-500 font-bold uppercase">{title}</p>
      <p className={`text-2xl font-bold ${highlight ? 'text-green-600' : ''}`}>{value}</p>
    </div>
    {icon}
  </div>
);

const Badge = ({ color, children }) => (
  <div className={`px-4 py-2 rounded-lg text-sm font-semibold ${
    color === 'green'
      ? 'bg-green-50 text-green-700'
      : 'bg-red-50 text-red-700'
  }`}>
    {children}
  </div>
);

export default App;