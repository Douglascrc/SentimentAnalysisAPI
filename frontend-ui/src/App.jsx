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
  Clock,
  RefreshCw
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
    recent: 10
  });

  const [apiData, setApiData] = useState({
    statusApi: 'Offline',
    totalAnalises: 0,
    percentualPositivo: 0,
    percentualNegativo: 0,
    tempoMedioRespostaMs: 0
  });

  useEffect(() => {
    fetchHistory();
  }, [filter]);

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
      const response = await fetch('http://localhost:8081/sentiment', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ text: comment, language })
      });

      if (response.ok) {
        const responseBody = await response.json();
        setResult(responseBody);
        setComment('');
        fetchStats();
        fetchHistory();
      } else {
        const errorData = await response.text();
        console.error('Erro na requisição (Status ' + response.status + '):', errorData);
      }
    } catch (error) {
      console.error("Erro na análise");
    } finally {
      setLoading(false);
    }
  };

    /* ===================== HISTÓRICO ===================== */
  const fetchHistory = async () => {
    try {
      const params = new URLSearchParams();

      if (filter.language !== 'all') {
        params.append('language', filter.language);
      }

      if (filter.sentiment !== 'all') {
        params.append('sentiment', filter.sentiment);
      }

      params.append('limit', filter.recent);
      console.log("Buscando histórico com params:", params.toString());

      const response = await fetch(`http://localhost:8081/comments?${params.toString()}`);

      if (response.ok) {
        const data = await response.json();
        if(data && data.comments) {
          setHistory(data.comments);
        }
      }
    } catch (error) {
      console.error("Erro ao buscar histórico", error);
    }
  };
  /* ===================== PIE ===================== */
  const pieData = [
    { name: 'Positivo', value: apiData.percentualPositivo },
    { name: 'Negativo', value: apiData.percentualNegativo }
  ];

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
            {result && (
                <div className={`mt-6 p-4 rounded-xl border animate-in fade-in slide-in-from-top-2 duration-300 ${
                    result.previsao === 'Positivo'
                        ? 'bg-green-50 border-green-200 text-green-900'
                        : 'bg-red-50 border-red-200 text-red-900'
                }`}>
                  <div className="flex items-center justify-between mb-2">
                    <span className="font-bold text-xl">{result.previsao}</span>
                    <span className="text-sm font-medium bg-white/60 px-3 py-1 rounded shadow-sm">
                        {(result.probabilidade * 100).toFixed(1)}% de confiança
                    </span>
                  </div>

                  {result.palavras_chave && result.palavras_chave.length > 0 && (
                      <div className="mt-4 pt-3 border-t border-black/5">
                        <p className="text-xs opacity-70 mb-2 uppercase font-bold tracking-wider">Palavras-chave</p>
                        <div className="flex flex-wrap gap-2">
                          {result.palavras_chave.map((word, i) => (
                              <span key={i} className="bg-white/80 px-2.5 py-1 rounded-md text-xs font-semibold shadow-sm text-slate-700">
                                {word}
                              </span>
                          ))}
                        </div>
                      </div>
                  )}
                </div>
            )}
          </div>

          {/* ===================== HISTÓRICO ===================== */}
          <div className="lg:col-span-2 bg-white rounded-2xl shadow p-6">
            <h3 className="font-bold text-lg mb-4">Histórico de Comentários</h3>

            <div className="flex gap-2 mb-4 flex-wrap items-center">
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
                <option value="Positivo">Positivo</option>
                <option value="Negativo">Negativo</option>
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

              {/* === BOTÃO DE BUSCAR === */}
              <button
                  onClick={fetchHistory}
                  className="p-2 bg-indigo-600 text-white rounded hover:bg-indigo-700 transition-colors flex items-center gap-2 text-sm font-semibold ml-auto sm:ml-0 cursor-pointer"
                  title="Atualizar lista manual"
              >
                <RefreshCw size={18} />
                <span className="hidden sm:inline">Buscar</span>
              </button>
            </div>

            <div className="bg-slate-50 p-3 rounded border h-64 overflow-y-auto">
              {history.length === 0 ? (
                  <div className="flex flex-col items-center justify-center h-full text-slate-400">
                    <MessageSquare className="w-8 h-8 mb-2 opacity-50"/>
                    <p className="text-sm">Nenhum comentário encontrado</p>
                  </div>
              ) : (
                  <ul className="divide-y divide-slate-200">
                    {history.map((c, i) => (
                        <li key={i} className="py-3 flex justify-between items-center group hover:bg-white transition-colors px-2 rounded">
                          <div className="pr-4 flex-1">
                            <p className="text-sm text-slate-700 font-medium truncate">
                              {c.text || "(Sem texto)"}
                            </p>
                            <p className="text-xs text-slate-400 mt-1 font-semibold flex items-center gap-2">
                              <span className="uppercase tracking-wide border px-1 rounded bg-slate-100">
                                {c.language || "NA"}
                              </span>

                              {c.score !== undefined && c.score !== null && (
                                  <span className="font-normal text-slate-500">
                                  Confiança: <span className="font-bold text-slate-700">{(c.score * 100).toFixed(0)}%</span>
                                </span>
                              )}
                            </p>
                          </div>

                          <span className={`text-xs font-bold px-3 py-1 rounded-full border whitespace-nowrap ml-2 ${
                              (c.sentiment && c.sentiment.toUpperCase() === 'POSITIVO')
                                  ? 'bg-green-100 text-green-700 border-green-200'
                                  : 'bg-red-100 text-red-700 border-red-200'
                          }`}>
                            {c.sentiment || 'N/A'}
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