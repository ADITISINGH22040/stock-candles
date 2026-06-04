<template>
  <main class="app">
    <h1>Stock Candles</h1>
    <p>Optional Vue.js frontend — UI to be implemented.</p>
    <CandleSearchPlaceholder />
  </main>
</template>

<script setup>
import CandleSearchPlaceholder from './components/CandleSearchPlaceholder.vue';
</script>

<style scoped>
.app {
  font-family: system-ui, sans-serif;
  max-width: 720px;
  margin: 2rem auto;
  padding: 0 1rem;
}
</style>
