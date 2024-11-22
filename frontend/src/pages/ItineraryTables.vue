<template>
  <v-app>
    <v-app-bar app>
      <v-toolbar-title>Itinerary Tables</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn text to="/Benchmark">Benchmark</v-btn>
      <v-btn text to="/ResultsView">Results View</v-btn>
    </v-app-bar>
    <v-container fluid class="mt-15 mt-15">
      <v-data-table
       
        :headers="headers"
        :items="filteredItineraries"
        :sort-by="sortBy"
        :sort-desc="sortDesc"
        multi-sort
        show-select
        item-key="uuid"
        class="elevation-1"
        :items-per-page="itemsPerPage"
        :items-per-page-options="itemsPerPageOptions"
        v-model="selected"
      >
        <template v-slot:top>
            <v-toolbar flat class="pt-4 pl-4">
              <v-select
                v-model="filterField"
                :items="headers.map(header => header.value)"
                label="Filter Field"
                class="mr-4"
                outlined
              ></v-select>
              <v-text-field
                v-model="filterValue"
                label="Filter Value"
                class="mr-4"
                outlined
                @input="applyFilter"
              ></v-text-field>

              <v-btn @click="clearFilter">Clear Filter</v-btn>
            <v-btn @click="toggleSortOrder" class="mr-3">
              {{ sortDesc ? 'Descending' : 'Ascending' }}
            </v-btn>
            <v-btn
              color="green"
              variant="tonal"
              class="mr-3"
              @click="showCreatePostDialog = !showCreatePostDialog"
            >
              Create
            </v-btn>
            <v-btn 
              color="orange"
              variant="tonal"
              class="mr-3"
              @click="editDialog = !editDialog" 
              >Update</v-btn>
            <v-btn
              color="red"
              variant="tonal"
              class="mr-3"
              @click="openDeleteDialog" :disabled="!selected.length">Delete</v-btn>
            </v-toolbar>
          </template>
    
          <template v-slot:item.startDate="{ item }">
            {{ formatDate(item.startDate) }}
          </template>
          <template v-slot:item.endDate="{ item }">
            {{ formatDate(item.endDate) }}
          </template>
          <template v-slot:item.itineraryStepDate="{ item }">
            {{ formatDate(item.itineraryStepDate) }}
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

    <!-- Edit Dialog -->
    <v-dialog v-model="editDialog" max-width="900px">
      <v-card>
        <v-card-title>Edit Existing Itinerary</v-card-title>
        <v-card-text>
          <v-select
            v-model="selectedItinerary"
            :items="itineraries"
            item-title="name"
            item-value="uuid"
            label="Select Itinerary to Edit"
            variant="outlined"
            @update:modelValue="populateEditForm()"
            required
          ></v-select>

          <v-form ref="form">
          <div v-if="selectedItinerary">
            <v-text-field v-model="editedItem.name" label="Name" required></v-text-field>
            <v-date-picker v-model="editedItem.startDate" label="Start Date" required></v-date-picker>
            <v-date-picker v-model="editedItem.endDate" label="End Date" required></v-date-picker>
            <v-text-field v-model="editedItem.itinerarySteps[0].name" label="Itinerary Step Name" required></v-text-field>
            <v-date-picker v-model="editedItem.itinerarySteps[0].stepDate" label="Itinerary Step Date" required></v-date-picker>
            <v-select
              v-model="editedItem.itinerarySteps[0].routeStops[0].currentCity.country"
              :items="countries"
              label="Current Country"
              @update:modelValue="fetchCitiesEdit('currentCity')"
              required
            ></v-select>
            <v-select
              v-model="editedItem.itinerarySteps[0].routeStops[0].currentCity.city"
              :items="currentCityCities"
              label="Current City"
              required
            ></v-select>
            <v-select
              v-model="editedItem.itinerarySteps[0].routeStops[0].nextCity.country"
              :items="countries"
              label="Next Country"
              @update:modelValue="fetchCitiesEdit('nextCity')"
              required
            ></v-select>
            <v-select
              v-model="editedItem.itinerarySteps[0].routeStops[0].nextCity.city"
              :items="nextCityCities"
              label="Next City"
              required
            ></v-select>
          </div>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red" text @click="cancelEdit">Cancel</v-btn>
          <v-btn color="green" text @click="saveEditedItem">Save</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Delete Confirmation Dialog -->
    <v-dialog v-model="showDeleteDialog" max-width="500px">
      <v-card>
        <v-card-title class="text-h5">Are you sure you want to delete this item?</v-card-title>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue-darken-1" variant="text" @click="closeDeleteDialog">Cancel</v-btn>
          <v-btn color="blue-darken-1" variant="text" @click="deleteItem">OK</v-btn>
          <v-spacer></v-spacer>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-snackbar
      v-model="snackbar.show"
      :timeout="1800"
    >
      {{ snackbar.message }}
      <template v-slot:actions>
        <v-btn color="primary" variant="text" @click="snackbar.show = false">
          Close
        </v-btn>
      </template>
    </v-snackbar>
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
        editedItem: {
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
        selectedItinerary: null,
        snackbar: {
          show: false,
          message: "",
        },
        headers: [
          { text: '', value: 'select', sortable: false },
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
        filterField: null,
        filterValue: '',
        sortBy: ['startDate'],
        sortDesc: [false],
        itemsPerPage: 10,
        page: 1,
        itemsPerPageOptions: [5, 10, 25, 50],
        showCreatePostDialog: false,
        showDeleteDialog: false,
        editDialog: false,
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
    computed: {
      filteredItineraries() {
        if (!this.filterField || !this.filterValue) {
          return this.flattenedItineraries;
        }

        return this.flattenedItineraries.filter((itinerary) => {
          const fieldValue = itinerary[this.filterField];
          return fieldValue && fieldValue.toString().toLowerCase().includes(this.filterValue.toLowerCase());
        });
      },
    },
    methods: {
      formatDate(date) {
        if (!date) return '';
        return new Date(date).toLocaleDateString();
      },
      toggleSortOrder() {
        this.sortDesc[0] = !this.sortDesc[0];
      },
      toggleSelection(item) {
        const index = this.selectedItems.findIndex(selected => selected.id === item.id);
        if (index === -1) {
          this.selectedItems.push(item);
        } else {
          this.selectedItems.splice(index, 1);
        }
      },
      isSelected(item) {
        return this.selectedItems.some(selected => selected.id === item.id);
      },
      async fetchItineraries() {
        try {
          const response = await axios.get('http://localhost:8000/api/mongodb/itinerary/all');
          console.log(response.data);
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
            id:itinerary.uuid,
            uuid: itinerary.uuid,
            name: itinerary.name,
            startDate: new Date(itinerary.startDate),
            endDate: new Date(itinerary.endDate),
            itineraryStepName: step.name,
            itineraryStepDate: new Date(step.stepDate),
            currentCity: step.routeStops[0]?.currentCity.city || '',
            currentCountry: step.routeStops[0]?.currentCity.country || '',
            nextCity: step.routeStops[0]?.nextCity.city || '',
            nextCountry: step.routeStops[0]?.nextCity.country || '',
          }))
        );
      },
      populateEditForm() {
        if (this.selectedItinerary == null) return;
        console.log("Selected Itinerary:", this.selectedItinerary);
        axios.get(`http://localhost:8000/api/mongodb/itinerary/${this.selectedItinerary}`)
          .then((response) => {
            console.log(response);
            this.editedItem.name = response.data.name;
            this.editedItem.startDate = new Date(response.data.startDate);
            this.editedItem.endDate = new Date(response.data.endDate);
            this.editedItem.itinerarySteps[0].name = response.data.itinerarySteps[0].name;
            this.editedItem.itinerarySteps[0].stepDate = new Date(response.data.itinerarySteps[0].stepDate);
            this.editedItem.itinerarySteps[0].routeStops[0].currentCity.country = response.data.itinerarySteps[0].routeStops[0].currentCity.country;
            this.editedItem.itinerarySteps[0].routeStops[0].currentCity.city = response.data.itinerarySteps[0].routeStops[0].currentCity.city;
            this.editedItem.itinerarySteps[0].routeStops[0].nextCity.country = response.data.itinerarySteps[0].routeStops[0].nextCity.country;
            this.editedItem.itinerarySteps[0].routeStops[0].nextCity.city = response.data.itinerarySteps[0].routeStops[0].nextCity.city;
          })
          .catch(error => {
            console.error("Error fetching itinerary:", error);
          });
        // const itinerary = this.itineraries.find(item => item.uuid === this.selectedItinerary);
        // if (itinerary) {
        //   this.editedItem = { ...itinerary }; // Clone to avoid mutation
        // }
      },
      cancelEdit() {
        this.editDialog = false;
        this.selectedItinerary = null;
        this.editedItem = null;
      },
      openCreateDialog() {
        this.createDialog = true;
      },
      closeCreateDialog() {
        this.createDialog = false;
      },
      openDeleteDialog() {
      this.showDeleteDialog = true;
      },
      closeDeleteDialog() {
        this.showDeleteDialog = false;
      },
      clearFilter() {
        this.filterField = null;
        this.filterValue = '';
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
        const country = type === 'currentCity' ? this.newItem.itinerarySteps[0].routeStops[0].currentCity.country : this.newItem.itinerarySteps[0].routeStops[0].nextCity.country;

        if (!country) {
          console.error("No country selected for", type);
          return;
        }
        try {
          const response = await axios.post('https://countriesnow.space/api/v0.1/countries/cities', { country });
          if (type === 'currentCity') {
            this.currentCityCities = response.data.data;
          } else {
            this.nextCityCities = response.data.data;
          }
        } catch (error) {
          console.error("Error fetching cities:", error);
        }
      },
      async fetchCitiesEdit(type) {
        const country = type === 'currentCity' ? this.editedItem.itinerarySteps[0].routeStops[0].currentCity.country : this.editedItem.itinerarySteps[0].routeStops[0].nextCity.country;

        if (!country) {
          console.error("No country selected for", type);
          return;
        }
        try {
          const response = await axios.post('https://countriesnow.space/api/v0.1/countries/cities', { country });
          if (type === 'currentCity') {
            this.currentCityCities = response.data.data;
          } else {
            this.nextCityCities = response.data.data;
          }
        } catch (error) {
          console.error("Error fetching cities:", error);
        }
      },
      async saveEditedItem() {
        const itemToUpdate = this.selectedItinerary;
        console.log("Item to update:", itemToUpdate);
        try {
          const dto = await axios.get(`http://localhost:8000/api/mongodb/itinerary/${itemToUpdate}`);
          console.log("DTO:", dto);
          dto.data.name = this.editedItem.name;
          dto.data.startDate = this.editedItem.startDate;
          dto.data.endDate = this.editedItem.endDate;
          dto.data.itinerarySteps[0].name = this.editedItem.itinerarySteps[0].name;
          dto.data.itinerarySteps[0].stepDate = this.editedItem.itinerarySteps[0].stepDate;
          dto.data.itinerarySteps[0].routeStops[0].currentCity.country = this.editedItem.itinerarySteps[0].routeStops[0].currentCity.country;
          dto.data.itinerarySteps[0].routeStops[0].currentCity.city = this.editedItem.itinerarySteps[0].routeStops[0].currentCity.city;
          dto.data.itinerarySteps[0].routeStops[0].nextCity.country = this.editedItem.itinerarySteps[0].routeStops[0].nextCity.country;
          dto.data.itinerarySteps[0].routeStops[0].nextCity.city = this.editedItem.itinerarySteps[0].routeStops[0].nextCity.city;
          const response = await axios.patch(`http://localhost:8000/api/mongodb/itinerary/update`, dto.data);
          this.itineraries = this.itineraries.map(itinerary => itinerary.uuid === itemToUpdate ? response.data : itinerary);
          const index = this.itineraries.findIndex(itinerary => itinerary.uuid === itemToUpdate.uuid);
          if (index !== -1) {
          this.itineraries.splice(index, 1, response.data);
        }
          this.flattenItineraries();
          this.snackbar.message = "Successfully updated itinerary.";
          this.snackbar.show = true;
        } catch (error) {
          console.error("Error updating itinerary:", error);
        }
      },
      async deleteItem() {
        if (this.selected.length === 0) {
          console.error('No item selected for deletion');
          return;
        }
        const selectedUuid = this.selected[0];
        try {
          const response = await axios.delete(
            `http://localhost:8000/api/mongodb/itinerary/delete/${selectedUuid}`
          );
          if (response.status === 200) {
            const index = this.itineraries.findIndex(
              (itinerary) => itinerary.uuid === selectedUuid
            );
            if (index !== -1) {
              this.itineraries.splice(index, 1);
              this.flattenItineraries();
              this.selected = [];
            }
          } else {
            console.error('Failed to delete item:', response.statusText);
          }
        } catch (error) {
          console.error('Error deleting itinerary:', error);
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