export async function fetchCandles(symbol, timeframe, startDate, endDate, page = 0, size = 200) {
  const params = new URLSearchParams({
    symbol,
    timeframe,
    start_date: startDate,
    end_date: endDate,
    page: String(page),
    size: String(size)
  });

  const response = await fetch(`/api/v1/candles?${params}`);
  if (!response.ok) {
    const body = await response.text();
    throw new Error(`API request failed: ${response.status} ${body}`);
  }
  return response.json();
}
