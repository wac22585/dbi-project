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
            <v-select :items="tests" label="Test auswählen" v-model="selectedTest"></v-select>
          </v-col>
        </v-row>
        <v-btn class="mb-4" @click="runBenchmark" :loading="loading" :disabled="loading">Benchmark starten</v-btn>
        <v-divider></v-divider>
        <v-row v-if="results">
          <v-col>
            <v-card>
              <v-card-title>Ergebnisse</v-card-title>
                <v-card-text>
                  <v-table>
                    <template v-slot:default>
                      <thead>
                        <tr>
                          <th class="table-header">{{ columnHeaders[0] }}</th>
                          <th class="table-header">{{ columnHeaders[1] }}</th>
                          <th class="table-header">{{ columnHeaders[2] }}</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr v-for="(result, description) in formattedResults" :key="description">
                          <td class="table-cell">{{ description }}</td>
                          <td class="table-cell">{{ formatTime(result[0]) }}</td>
                          <td class="table-cell">{{ formatTime(result[1]) }}</td>
                        </tr>
                      </tbody>
                    </template>
                  </v-table>
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
import { mapActions } from 'vuex';

  export default {
    data() {
      return {
        selectedTest: 'MongoDb vs MySQL',
        tests: ['MongoDb vs MySQL', 'Embedding vs Referencing'],
        results: {},
        error: null,
        loading: false,
        orderedDescriptions: [
          "Create 100",
          "Create 1000",
          "Create 10000",
          "Find all",
          "Find with filter",
          "Find with filter and projection",
          "Find with filter and projection and sort",
          "Update",
          "Delete",
        ],
      };
    },
    computed: {
      formattedResults() {
        const sortedResults = {};
        this.orderedDescriptions.forEach((description) => {
          if (this.results[description]) {
            sortedResults[description] = this.results[description];
          }
        });
        return sortedResults;
      },
      columnHeaders() {
        if (this.selectedTest === 'MongoDb vs MySQL') {
          return ['Description', 'MongoDB', 'MySQL'];
        } else if (this.selectedTest === 'Embedding vs Referencing') {
          return ['Description', 'Referencing', 'Embedding'];
        }
        return ['Description', 'Column 1', 'Column 2']; // Default headers
      },
    },
    methods: {
      ...mapActions(['updateBenchmarkResults']),
      async runBenchmark() {
        this.loading = true;
        this.results = [];
        this.error = null;

        console.log("hello:",  this.selectedTest)

        // this.updateBenchmarkResults(this.results);

          // const timestamp = new Date().toISOString(); // Generate timestamp

          // // Navigate to ResultsView with results and timestamp
          // this.$router.push({
          //   path: '/ResultsView',
          //   state: { results: this.results, timestamp },
          // });

        try {
          let response;
          if (this.selectedTest === 'MongoDb vs MySQL') {
            response = await axios.get('http://localhost:8000/api/performance/benchmarks-mongo-sql');
          } else {
            response = await axios.get('http://localhost:8000/api/performance/benchmarks-embedding-referencing');
          }
          this.results = response.data;
          console.log('API Response:', response.data);
          // Update Vuex store with benchmark results
        } catch(error) {
          this.error = 'Failed to run benchmark';
          console.error(error);
        }

      this.loading = false;
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
