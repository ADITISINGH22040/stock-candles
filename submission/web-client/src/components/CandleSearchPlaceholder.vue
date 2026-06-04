<template>
  <section class="search">
    <label>
      Symbol
      <input v-model="symbol" type="text" placeholder="e.g. AAPL" disabled />
    </label>
    <label>
      Trading date
      <input v-model="tradingDate" type="date" disabled />
    </label>
    <button type="button" disabled>Search (coming soon)</button>
  </section>
</template>

<script setup>
import { ref } from 'vue';

const symbol = ref('');
const tradingDate = ref('');
</script>

<style scoped>
.search {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  margin-top: 1.5rem;
}

label {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}
</style>
