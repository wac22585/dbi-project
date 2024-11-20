<template>
  <v-app-bar app>
      <v-toolbar-title>Results View</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn text to="/Benchmark">Benchmark</v-btn>
      <v-btn text to="/ItineraryTables">Itinerary Tables</v-btn>
    </v-app-bar>
  <v-container>
    <v-card>
      <v-card-title>Benchmark-Ergebnisse</v-card-title>
      <v-card-text>
        <canvas id="chart"></canvas>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import { Chart } from 'chart.js';

export default {
  props: ['results'],
  mounted() {
    this.renderChart();
  },
  methods: {
    renderChart() {
      new Chart(document.getElementById('chart'), {
        type: 'bar',
        data: {
          labels: ['Write', 'Find', 'Update', 'Delete'],
          datasets: [
            {
              label: 'MySQL',
              data: this.results.mysql,
              backgroundColor: 'blue',
            },
            {
              label: 'MongoDB',
              data: this.results.mongodb,
              backgroundColor: 'green',
            },
          ],
        },
      });
    },
  },
};
</script>
