<template>
  <main class="app">
    <header>
      <h1>Stock Candles</h1>
      <p>Query symbol, timeframe and date range, then view aggregated candlestick data.</p>
    </header>

    <section class="search-card">
      <form @submit.prevent="searchCandles" class="search-form">
        <label>
          Symbol
          <input v-model="symbol" type="text" placeholder="AAPL" required />
        </label>

        <label>
          Timeframe
          <select v-model="timeframe">
            <option value="1m">1m</option>
            <option value="5m">5m</option>
            <option value="15m">15m</option>
            <option value="30m">30m</option>
            <option value="1h">1h</option>
            <option value="1d">1d</option>
          </select>
        </label>

        <label>
          Start date
          <input v-model="startDate" type="datetime-local" required />
        </label>

        <label>
          End date
          <input v-model="endDate" type="datetime-local" required />
        </label>

        <button type="submit" :disabled="loading">Search</button>
      </form>
    </section>

    <section class="status-card" v-if="error">
      <p class="error">{{ error }}</p>
    </section>

    <section class="results-card" v-if="candles.length || message">
      <div class="results-header">
        <div>
          <p>Symbol: <strong>{{ metadata.symbol || '-' }}</strong></p>
          <p>Timeframe: <strong>{{ metadata.timeframe || '-' }}</strong></p>
          <p>Returned: <strong>{{ metadata.count || 0 }}</strong></p>
        </div>
      </div>

      <div id="chart-container" class="chart-container" aria-label="Candlestick chart"></div>

      <div class="table-container" v-if="candles.length">
        <table>
          <thead>
            <tr>
              <th>#</th>
              <th>Date / Time</th>
              <th>Open</th>
              <th>High</th>
              <th>Low</th>
              <th>Close</th>
              <th>Volume</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(candle, index) in candles" :key="candle.datetime">
              <td>{{ index + 1 }}</td>
              <td>{{ candle.datetime }}</td>
              <td>{{ candle.open }}</td>
              <td>{{ candle.high }}</td>
              <td>{{ candle.low }}</td>
              <td>{{ candle.close }}</td>
              <td>{{ candle.volume }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <p v-if="message" class="message">{{ message }}</p>
    </section>
  </main>
</template>

<script setup>
import { onMounted, reactive, ref, watch } from 'vue';
import Highcharts from 'highcharts/highstock';
import { fetchCandles } from './api/candleApi';

const symbol = ref('');
const timeframe = ref('15m');
const startDate = ref('');
const endDate = ref('');
const loading = ref(false);
const error = ref('');
const message = ref('');
const candles = ref([]);
const metadata = reactive({ symbol: '', timeframe: '', count: 0 });

const loadChart = () => {
  const container = document.getElementById('chart-container');
  if (!candles.value.length) {
    if (container) {
      container.innerHTML = '<p class="empty-state">No candle data to display.</p>';
    }
    return;
  }

  const chartData = candles.value.map((candle) => [
    new Date(candle.datetime).getTime(),
    Number(candle.open),
    Number(candle.high),
    Number(candle.low),
    Number(candle.close)
  ]);

  Highcharts.stockChart('chart-container', {
    chart: {
      backgroundColor: '#ffffff',
      height: '60%'
    },
    rangeSelector: {
      selected: 1
    },
    title: {
      text: `${metadata.symbol} ${metadata.timeframe} Candlestick Chart`
    },
    xAxis: {
      type: 'datetime'
    },
    yAxis: [{
      title: {
        text: 'Price'
      }
    }],
    series: [{
      type: 'candlestick',
      name: `${metadata.symbol} price`,
      data: chartData,
      tooltip: {
        valueDecimals: 2
      }
    }]
  });
};

const searchCandles = async () => {
  error.value = '';
  message.value = '';
  candles.value = [];
  metadata.symbol = '';
  metadata.timeframe = '';
  metadata.count = 0;

  if (!symbol.value || !startDate.value || !endDate.value) {
    error.value = 'Symbol, start date and end date are required.';
    return;
  }

  const normalizeDateTime = (value) => {
    const normalized = value.replace('T', ' ');
    return normalized.length === 16 ? `${normalized}:00` : normalized;
  };

  const start = normalizeDateTime(startDate.value);
  const end = normalizeDateTime(endDate.value);
  loading.value = true;

  try {
    const response = await fetchCandles(symbol.value.trim(), timeframe.value, start, end);
    metadata.symbol = response.symbol;
    metadata.timeframe = response.timeframe;
    metadata.count = response.count;
    candles.value = response.candles || [];
    if (!candles.value.length) {
      message.value = 'No candles were found for the selected range.';
    }
    loadChart();
  } catch (e) {
    error.value = e.message || 'Unable to fetch candles.';
    const container = document.getElementById('chart-container');
    if (container) {
      container.innerHTML = '';
    }
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  const container = document.getElementById('chart-container');
  if (container) {
    container.innerHTML = '<p class="empty-state">Enter query parameters and click Search.</p>';
  }
});

watch(candles, (newCandles) => {
  if (newCandles.length) {
    loadChart();
  }
});
</script>

<style scoped>
.app {
  font-family: system-ui, sans-serif;
  max-width: 920px;
  margin: 2rem auto;
  padding: 0 1rem;
  color: #0f172a;
}

header h1 {
  margin: 0;
}

.search-card,
.results-card,
.status-card {
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 14px;
  box-shadow: 0 16px 40px rgba(15, 23, 42, 0.06);
  margin-top: 1.5rem;
  padding: 1.25rem;
}

.search-form {
  display: grid;
  gap: 1rem;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
}

.search-form label {
  display: grid;
  gap: 0.5rem;
}

.search-form input,
.search-form select,
.search-form button {
  border: 1px solid #cbd5e1;
  border-radius: 10px;
  padding: 0.85rem 1rem;
  font: inherit;
}

.search-form button {
  background: #2563eb;
  color: white;
  cursor: pointer;
  transition: background 0.2s ease;
}

.search-form button:disabled {
  opacity: 0.65;
  cursor: not-allowed;
}

.error {
  color: #b91c1c;
  margin: 0;
}

.chart-container {
  min-height: 360px;
  margin-top: 1rem;
}

.table-container {
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

thead {
  background: #f8fafc;
}

th,
td {
  text-align: left;
  padding: 0.9rem 0.65rem;
  border-bottom: 1px solid #e2e8f0;
}

.message,
.empty-state {
  margin-top: 1rem;
  color: #334155;
}
</style>
