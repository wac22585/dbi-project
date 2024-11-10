<template>
  <v-container>
    <v-card>
      <v-card-title>Benchmark Test</v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="12" sm="6">
            <v-text-field label="Anzahl der Einträge" v-model="count" type="number"></v-text-field>
          </v-col>
          <v-col cols="12" sm="6">
            <v-select :items="databases" label="Datenbank auswählen" v-model="selectedDatabase"></v-select>
          </v-col>
        </v-row>
        <v-btn @click="runBenchmark">Benchmark starten</v-btn>
        <v-divider></v-divider>
        <v-row v-if="results">
          <v-col>
            <v-card>
              <v-card-title>Ergebnisse</v-card-title>
              <v-card-text>{{ results }}</v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import axios from 'axios';

  export default {
    data() {
      return {
        count: 100,
        selectedDatabase: 'mysql',
        databases: ['mysql', 'mongodb'],
        results: null,
      };
    },
    methods: {
      async runBenchmark() {
        try {
          const response = await axios.post('http://localhost:3306/**', {
            count: this.count,
            database: this.selectedDatabase,
          });
          this.results = `Dauer: ${response.data.timeTaken} ms`;
        } catch (error) {
          console.error(error);
        }
      },
    },
  };
</script>
