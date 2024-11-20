<template>
  <v-app-bar app>
      <v-toolbar-title>Benchmark</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn text to="/ItineraryTables">Itinerary Tables</v-btn>
      <v-btn text to="/ResultsView">Results View</v-btn>
    </v-app-bar>

  <v-container>
    <v-card>
      <v-card-title>Benchmark Test</v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="12" sm="6">
            <v-select :items="databases" label="Datenbank auswählen" v-model="selectedDatabase"></v-select>
          </v-col>
        </v-row>
        <v-btn @click="runBenchmark" :loading="loading" :disabled="loading">Benchmark starten</v-btn>
        <v-divider></v-divider>
        <v-row v-if="results">
          <v-col>
            <v-card>
              <v-card-title>Ergebnisse</v-card-title>
                <v-card-text>
                  <v-simple-table>
                    <template v-slot:default>
                      <thead>
                        <tr>
                          <th class="table-header">Operation</th>
                          <th class="table-header">Count</th>
                          <th class="table-header">Duration (ms)</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr v-for="(result, index) in results" :key="index">
                          <td class="table-cell">{{ result.operation }}</td>
                          <td class="table-cell">{{ result.count }}</td>
                          <td class="table-cell">{{ result.duration }}</td>
                        </tr>
                      </tbody>
                    </template>
                  </v-simple-table>
                </v-card-text>
            </v-card>
          </v-col>
        </v-row>
        <!-- Error Section -->
        <v-alert v-if="error" type="error" dismissible>
          {{ error }}
        </v-alert>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import axios from 'axios';

  export default {
    data() {
      return {
        selectedDatabase: 'mysql',
        databases: ['mysql', 'mongodb'],
        results: [],
        error: null,
        loading: false,      
        };
    },
    methods: {
      async runBenchmark() {
        this.loading = true;
        this.results = [];
        this.error = null;

      const counts = [100, 1000, 10000];
      for (const count of counts) {
        try {
          const response = await axios.get(`http://localhost:8000/api/performance/crud?size=${count}`);

          console.log('API Response:', response.data); // Debug API response

          Object.entries(response.data).forEach(([operation, duration]) => {
          this.results.push({
            operation,
            count,
            duration,
            });
          });
        } catch (error) {
          this.error = 'Failed to run benchmark';
        console.error(error);
        }
      }
      
      this.loading = false;
      },
    },
  };
</script>

<!-- <style>
.benchmark-table .table-header {
  padding: 10px 20px;
}
-->

<style scoped>
.table-cell {
  padding: 10px 20px; /* Adjust spacing between text and cell borders */
}
</style>