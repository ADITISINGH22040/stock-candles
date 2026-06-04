/**
 * Placeholder API module for stock candle endpoints.
 * Implementation will call GET /api/v1/candles via the Vite dev proxy.
 */

export async function fetchCandles(symbol, tradingDate) {
  const params = new URLSearchParams({ symbol, tradingDate });
  const response = await fetch(`/api/v1/candles?${params}`);
  if (!response.ok) {
    throw new Error(`API request failed: ${response.status}`);
  }
  return response.json();
}
