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
          <v-row>
            <v-col cols="12">
              <p><strong>Timestamp:</strong>  {{ formattedTimestamp }} </p>
            </v-col>
          </v-row>

          <v-row>
            <v-table>
              <thead>
                <tr>
                  <th class="table-header">Beschreibung</th>
                  <th class="table-header">MongoDB</th>
                  <th class="table-header">MySQL</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(result, operation) in getBenchmarkResults" :key="operation">
                  <td class="table-cell">{{ operation }}</td>
                  <td class="table-cell">{{ formatTime(result[0]) }}</td>
                  <td class="table-cell">{{ formatTime(result[1]) }}</td>
                </tr>
              </tbody>
            </v-table>  
          </v-row>  
          <v-row>
          <v-col>
            <canvas id="chart"></canvas>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import { mapGetters } from 'vuex';
import { Chart } from 'chart.js';

export default {
  computed: {
    ...mapGetters(['getBanchmarkResults']),
  },
  props: ['results', 'timestamp'],
  data() {
    return {
      formattedTimestamp: '',
    };
  },
  mounted() {
    this.formatTimestamp();
    console.log(this.getBenchmarkResults);
    if (this.getBenchmarkResults && Object.keys(this.getBenchmarkResults).length > 0) {
      this.renderChart();
    }
  },
  methods: {
    formatTimestamp() {
      // Format timestamp into readable format
      const date = new Date(this.timestamp);
      this.formattedTimestamp = date.toLocaleString(); // e.g., "17/11/2024, 3:45 PM"
    },
    renderChart() {
      new Chart(document.getElementById('chart'), {
        type: 'bar',
        data: {
          labels: Object.keys(this.getBenchmarkResults),
          datasets: [
            {
              label: 'MySQL',
              data: Object.values(this.getBenchmarkResults).map((result) => result[0]),
              backgroundColor: 'blue',
            },
            {
              label: 'MongoDB',
              data: Object.values(this.getBenchmarkResults).map((result) => result[0]),
              backgroundColor: 'green',
            },
          ],
        },
      });
    },
    formatTime(timeInMs) {
      if (timeInMs >= 1000) {
          const seconds = Math.floor(timeInMs / 1000);
          const milliseconds = timeInMs % 1000;
          return `${seconds}s${milliseconds}ms`;
        }
        return `${timeInMs}ms`;
      },
  },
};
</script>
