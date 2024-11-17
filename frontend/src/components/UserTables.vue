<template>
    <v-container>
      <v-data-table
        :headers="headers"
        :items="users"
        item-value="id"
        class="elevation-1"
      >
        <template v-slot:top>
          <v-toolbar flat>
            <v-toolbar-title>User List</v-toolbar-title>
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
        users: [],
        headers: [
          { text: "Name", value: "name" },
          { text: "Email", value: "email" },
        ],
      };
    },
    methods: {
      async fetchUsers() {
        try {
          const response = await axios.get("http://localhost:3306/api/user");
          this.users = response.data;
        } catch (error) {
          console.error("Error fetching users:", error);
        }
      },
    },
    mounted() {
      this.fetchUsers();
    },
  };
  </script>
  