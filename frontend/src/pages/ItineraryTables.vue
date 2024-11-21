<template>
  <v-app>
    <v-app-bar app>
      <v-toolbar-title>Itinerary Tables</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn text to="/Benchmark">Benchmark</v-btn>
      <v-btn text to="/ResultsView">Results View</v-btn>
    </v-app-bar>
    <v-container class="mt-15 mt-15">
      <v-data-table
        :headers="headers"
        :items="flattenedItineraries"
        :sort-by="[{ key: 'startDate', order: 'asc'}]" 
        multi-sort
        item-value="uuid"
        class="elevation-1"
        @click:row="onRowClick"
        show-select
        v-model:selected="selected"
        v-model:items-per-page="itemsPerPage"
        v-model:page="page"
        v-model:items-per-page-options="itemsPerPageOptions"
      >
        <template v-slot:top>
          <v-toolbar flat>
            <v-toolbar-title>Itinerary CRUD</v-toolbar-title>
            <v-divider class="mx-4" inset vertical></v-divider>
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
          <v-btn
            color="green"
            variant="tonal"
            class="mb-2"
            @click="showCreatePostDialog = !showCreatePostDialog"
          >
            Create
          </v-btn>
          <v-btn 
            color="orange"
            variant="tonal"
            class="mb-2"
            @click="updateItem" :disabled="!selected.length">Update</v-btn>
          <v-btn
            color="red"
            variant="tonal"
            class="mb-2"
            @click="deleteItem" :disabled="!selected.length">Delete</v-btn>
          </v-toolbar>
        </template>
      </v-data-table>
    </v-container>

    <!-- Create Item Dialog -->
    <v-dialog v-model="showCreatePostDialog" max-width="900px">
      <v-card>
        <v-card-title>
          <span class="headline">Create New Itinerary</span>
        </v-card-title>
        <v-card-text>
          <v-form ref="form">
            <v-text-field v-model="newItem.name" label="Name" required></v-text-field>
            <v-date-picker v-model="newItem.startDate" label="Start Date" required></v-date-picker>
            <v-date-picker v-model="newItem.endDate" label="End Date" required></v-date-picker>
            <v-text-field v-model="newItem.itinerarySteps[0].name" label="Itinerary Step Name" required></v-text-field>
            <v-date-picker v-model="newItem.itinerarySteps[0].stepDate" label="Itinerary Step Date" required></v-date-picker>
            <!-- <v-select
              v-model="newItem.itinerarySteps[0].routeStops[0].currentCity.country"
              :items="countries"
              label="Current Country"
              @change="fetchCities('currentCity')"
              required
            ></v-select> -->
            <v-select
              v-model="newItem.itinerarySteps[0].routeStops[0].currentCity.country"
              :items="countries"
              label="Current Country"
              @update:modelValue="(value) => fetchCities('currentCity')"
              required
            ></v-select>
            <v-select
              v-model="newItem.itinerarySteps[0].routeStops[0].currentCity.city"
              :items="currentCityCities"
              label="Current City"
              required
            ></v-select>
            <v-select
              v-model="newItem.itinerarySteps[0].routeStops[0].nextCity.country"
              :items="countries"
              label="Next Country"
              @update:modelValue="fetchCities('nextCity')"
              required
            ></v-select>
            <v-select
              v-model="newItem.itinerarySteps[0].routeStops[0].nextCity.city"
              :items="nextCityCities"
              label="Next City"
              required
            ></v-select>
          </v-form>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="closeCreateDialog">Cancel</v-btn>
          <v-btn color="blue darken-1" text @click="submitCreateItem">Save</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    </v-app>
  </template>
  
  <script>
  import axios from "axios";
  
  export default {
    data() {
      return {
        itineraries: [],
        flattenedItineraries: [],
        selected: [],
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
        itemsPerPage: 10,
        page: 1,
        itemsPerPageOptions: [5, 10, 25, 50],
        showCreatePostDialog: false,
        newItem: {
          uuid: null, 
          name: '', 
          startDate: new Date(), 
          endDate: new Date(),
          itinerarySteps: [
            {
              name: '', 
              stepDate: new Date(),
              routeStops: [
                {
                  currentCity: { 
                    city: '', 
                    country: '' 
                  },
                  nextCity: { 
                    city: '', 
                    country: '' 
                  },
                },
              ],
            },
          ],
        },
        countries: [],
        selectedCountry: '', // Flat property for testing
        currentCityCities: [],
        nextCityCities: [],
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
      },
      openCreateDialog() {
        this.createDialog = true;
      },
      closeCreateDialog() {
        this.createDialog = false;
      },
      onRowClick(item) {
      this.selected = [item.uuid]; // or [item.id] depending on your unique identifier
      },
      async submitCreateItem() {
      if (this.$refs.form.validate()) {
        try {
            const response = await axios.post('http://localhost:8000/api/mongodb/itinerary/add', this.newItem);
            this.itineraries.push(response.data);
            this.flattenItineraries();
            this.closeCreateDialog();
            console.log(response.data);
          } catch (error) {
            console.error("Error creating itinerary:", error);
          }
        }
      },
      async fetchCountries() {
        try {
          const response = await axios.get('https://restcountries.com/v3.1/all');
          this.countries = response.data.map(country => country.name.common);
        } catch (error) {
          console.error("Error fetching countries:", error);
        }
      },
      async fetchCities(type) {
        console.log('fetchCities triggered with:', type);

        const country = type === 'currentCity' ? this.newItem.itinerarySteps[0].routeStops[0].currentCity.country : this.newItem.itinerarySteps[0].routeStops[0].nextCity.country;

        console.log(`Fetching cities for ${type} in country:`, country);

        if (!country) {
          console.error("No country selected for", type);
          return;
        }
        try {
          const response = await axios.post('https://countriesnow.space/api/v0.1/countries/cities', { country });
          console.log(response);
          console.log(this.newItem.itinerarySteps[0].routeStops[0]);
          if (type === 'currentCity') {
            this.currentCityCities = response.data.data;
          } else {
            this.nextCityCities = response.data.data;
          }
        } catch (error) {
          console.error("Error fetching cities:", error);
        }
      },
      async updateItem() {
        const itemToUpdate = this.selected[0];
        itemToUpdate.name = 'Updated Itinerary';
        try {
          const response = await axios.put(`http://localhost:8000/api/mongodb/itinerary/${itemToUpdate.uuid}`, itemToUpdate);
          const index = this.itineraries.findIndex(itinerary => itinerary.uuid === itemToUpdate.uuid);
          this.itineraries.splice(index, 1, response.data);
          this.flattenItineraries();
        } catch (error) {
          console.error("Error updating itinerary:", error);
        }
      },
      async deleteItem() {
        if (this.selected.length === 0) {
        console.error("No item selected for deletion");
        return;
        }
        const itemToDelete = this.selected[0];
        try {
          const response = await axios.get(`http://localhost:8000/api/mongodb/itinerary/delete/${itemToDelete.uuid}`);
          if (response.status === 200) {
            const index = this.itineraries.findIndex(itinerary => itinerary.uuid === itemToDelete.uuid);
            if (index !== -1) {
              this.itineraries.splice(index, 1);
              this.flattenItineraries();
            }
          }
        } catch (error) {
          console.error("Error deleting itinerary:", error);
        }
      }
    },
    // watch: {
    //   selected(newSelected) {
    //     console.log("Selected items:", newSelected); // Check what is inside 'selected'
    //   } 
    // },
    mounted() {
      this.fetchItineraries();
      this.fetchCountries();
      // this.$nextTick(() => {
      //   const selectElement = this.$refs.nextCountrySelect;
      //   if (selectElement) {
      //     selectElement.addEventListener('change', (event) => {
      //       console.log('DOM change event:', event.target.value);
      //       this.fetchCities('nextCity');
      //     });
      //   }
      // });
    }
  };
</script>