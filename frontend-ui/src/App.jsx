import React, { useState, useEffect } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { Activity, MessageSquare, Zap, AlertCircle, Clock, ShieldCheck } from 'lucide-react';

const App = () => {
  const [comment, setComment] = useState('');
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);
  const [apiData, setApiData] = useState({
    statusApi: "Offline",
    totalAnalises: 0,
    percentualPositivo: 0,
    percentualNegativo: 0,
    tempoMedioRespostaMs: 0
  });

  // Ajustado para bater exatamente no @RequestMapping("/stats") do seu StatsController
  const fetchStats = async () => {
    try {
      const response = await fetch('/stats');
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
      const response = await fetch('/sentiment', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ text: comment })
      });

      if (response.ok) {
        const data = await response.json();
        setResult(data);
        fetchStats();
      }
    } catch (error) {
      console.error("Erro na análise");
    } finally {
      setLoading(false);
    }
  };

  const benchmarkData = [
    { name: 'Olist', acuracia: 92, recall: 89 },
    { name: 'B2W', acuracia: 94, recall: 93 },
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
    <div className="min-h-screen bg-slate-50 font-sans text-slate-900">
      <nav className="bg-white border-b p-4 mb-8">
        <div className="max-w-7xl mx-auto flex justify-between items-center">
          <h1 className="text-xl font-bold flex items-center gap-2">
            <div className="w-8 h-8 bg-indigo-600 rounded flex items-center justify-center text-white text-xs">SA</div>
            SentimentAnalysis<span className="text-indigo-600">API</span>
          </h1>
          <div className="flex items-center gap-2 text-sm font-medium text-slate-500">
             <div className={`w-2 h-2 rounded-full ${apiData.statusApi === 'UP' ? 'bg-green-500 animate-pulse' : 'bg-red-500'}`}></div>
             Sistema {apiData.statusApi === 'UP' ? 'Online' : 'Offline'}
          </div>
        </div>
      </nav>

      <main className="max-w-7xl mx-auto px-4">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
          <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-100 flex items-center justify-between">
            <div>
              <p className="text-xs text-slate-500 uppercase font-bold tracking-wider">Análises Totais</p>
              <p className="text-2xl font-bold text-blue-600">{apiData?.totalAnalises || 0}</p>
            </div>
            <div className="p-3 bg-slate-50 rounded-lg text-slate-400"><MessageSquare size={20}/></div>
          </div>
          <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-100 flex items-center justify-between">
            <div>
              <p className="text-xs text-slate-500 uppercase font-bold tracking-wider">Positivos</p>
              <p className="text-2xl font-bold text-amber-500">{apiData?.percentualPositivo || 0}%</p>
            </div>
            <div className="p-3 bg-slate-50 rounded-lg text-slate-400"><Zap size={20}/></div>
          </div>
          <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-100 flex items-center justify-between">
            <div>
              <p className="text-xs text-slate-500 uppercase font-bold tracking-wider">Latência</p>
              <p className="text-2xl font-bold text-indigo-600">{apiData?.tempoMedioRespostaMs || 0}ms</p>
            </div>
            <div className="p-3 bg-slate-50 rounded-lg text-slate-400"><Clock size={20}/></div>
          </div>
          <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-100 flex items-center justify-between">
            <div>
              <p className="text-xs text-slate-500 uppercase font-bold tracking-wider">Status</p>
              <p className={`text-2xl font-bold ${apiData.statusApi === 'UP' ? 'text-green-600' : 'text-red-600'}`}>{apiData.statusApi}</p>
            </div>
            <div className="p-3 bg-slate-50 rounded-lg text-slate-400"><Activity size={20}/></div>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          <div className="lg:col-span-2 bg-white p-6 rounded-xl shadow-sm border border-slate-100" style={{ minHeight: '400px' }}>
            <h2 className="text-lg font-bold mb-6">Benchmarks</h2>
            <div className="h-64">
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={benchmarkData}>
                  <CartesianGrid strokeDasharray="3 3" vertical={false} />
                  <XAxis dataKey="name" />
                  <YAxis />
                  <Tooltip />
                  <Bar dataKey="acuracia" fill="#6366f1" radius={[4, 4, 0, 0]} />
                  <Bar dataKey="recall" fill="#ef4444" radius={[4, 4, 0, 0]} />
                </BarChart>
              </ResponsiveContainer>
            </div>
          </div>

          <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-100 border-t-4 border-t-indigo-600">
            <h2 className="text-lg font-bold mb-2 flex items-center gap-2">
              <AlertCircle size={20} className="text-indigo-600" /> Simulador
            </h2>
            <textarea
              className="w-full p-3 border border-slate-200 rounded-lg text-sm outline-none"
              rows="4"
              placeholder="Digite seu feedback..."
              value={comment}
              onChange={(e) => setComment(e.target.value)}
            />
            <button
              onClick={handleAnalyze}
              disabled={loading}
              className="mt-4 w-full bg-slate-900 text-white py-2.5 rounded-lg font-bold disabled:opacity-50"
            >
              {loading ? 'Processando...' : 'Analisar Comentário'}
            </button>

            {result && (
              <div className={`mt-6 p-4 rounded-xl border-2 transition-all ${getSentimentBg(result.sentiment)}`}>
                <div className="flex items-center gap-2 mb-1">
                  <ShieldCheck size={18} className={getSentimentColor(result.sentiment)} />
                  <span className="font-bold text-sm text-slate-700">Resultado:</span>
                </div>
                <p className={`text-xl font-black uppercase ${getSentimentColor(result.sentiment)}`}>
                  {result.sentiment}
                </p>
                <p className="text-[10px] mt-1 opacity-70 font-medium text-slate-500">
                  Confiança: {(Number(result.score) * 100).toFixed(2)}%
                </p>
              </div>
            )}
          </div>
        </div>
      </main>
    </div>
  );
};

export default App;