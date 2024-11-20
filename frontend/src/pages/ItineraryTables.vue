<template>
  <v-app-bar app>
      <v-toolbar-title>Itinerary Tables</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn text to="/Benchmark">Benchmark</v-btn>
      <v-btn text to="/ResultsView">Results View</v-btn>
    </v-app-bar>
    <v-container>
      <v-data-table
        :headers="headers"
        :items="flattenedItineraries"
        :sort-by="['uuid', 'name', 'startDate', 'endDate']"
        :sort-desc="[sortDesc]"
        multi-sort
        item-value="id"
        class="elevation-1"
      >
        <template v-slot:top>
          <v-toolbar flat>
            <v-toolbar-title>Itinerary List</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-select
            v-model="sortBy"
            :items="headers.map(header => header.value)"
            label="Sort by"
            class="mr-4"
          ></v-select>
          <v-btn @click="toggleSortOrder">
            {{ sortDesc ? 'Descending' : 'Ascending' }}
          </v-btn>
          </v-toolbar>
        </template>
      </v-data-table>
    </v-container>
  </template>
  
  <script>
  import axios from "axios";
  
  export default {
    data() {
      return {
        itineraries: [],
        flattenedItineraries: [],
        headers: [
          { text: "Uuid", value: "uuid" },
          { text: "Name", value: "name" },
          { text: "Start Date", value: "startDate" },
          { text: "End Date", value: "endDate" },
          { text: "Itinerary Step", value: "itineraryStepName" },
          { text: "Itinerary Step Date", value: "itineraryStepDate" },
          { text: "Current City", value: "currentCity" },
          { text: "Current Country", value: "currentCountry" },
          { text: "Next City", value: "nextCity" },
          { text: "Next Country", value: "nextCountry" },
      ],
      sortBy: 'startDate',
      sortDesc: false,
      sortOptions: [
        { text: "Uuid", value: "uuid" },
        { text: "Name", value: "name" },
        { text: "Start Date", value: "startDate" },
        { text: "End Date", value: "endDate" },
        { text: "Itinerary Step", value: "itineraryStepName" },
        { text: "Itinerary Step Date", value: "itineraryStepDate" },
        { text: "Current City", value: "currentCity" },
        { text: "Current Country", value: "currentCountry" },
        { text: "Next City", value: "nextCity" },
        { text: "Next Country", value: "nextCountry" },
      ],
      };
    },
    methods: {
      async fetchItineraries() {
        try {
          const response = await axios.get('http://localhost:8000/api/mongodb/itinerary/all');
          this.itineraries = response.data;
          this.flattenItineraries();
          console.log(this.itineraries)
        } catch (error) {
          console.error("Error fetching itineraries:", error);
        }
      },
      flattenItineraries() {
        this.flattenedItineraries = this.itineraries.flatMap(itinerary => 
          itinerary.itinerarySteps.map(step => ({
            uuid: itinerary.uuid,
            name: itinerary.name,
            startDate: itinerary.startDate,
            endDate: itinerary.endDate,
            itineraryStepName: step.name,
            itineraryStepDate: step.stepDate,
            currentCity: step.routeStops[0]?.currentCity.city || '',
            currentCountry: step.routeStops[0]?.currentCity.country || '',
            nextCity: step.routeStops[0]?.nextCity.city || '',
            nextCountry: step.routeStops[0]?.nextCity.country || '',
          }))
        );
      },
      toggleSortOrder() {
        this.sortDesc = !this.sortDesc;
      }
    },
    mounted() {
      this.fetchItineraries();
    },
  };
  </script>
  