<template>
  <v-card>
    <v-card-title>Add a new Match</v-card-title>
    <v-card-text>
      <v-text-field v-model="posts_title" :rules="titleRules"
                    :counter="15" label="Title" required variant="outlined"/>
      <v-textarea v-model="posts_text" :rules="bodyRules"
                  :counter="150" label="Body" required variant="outlined"/>
      <v-select v-model="post_author" :items="authors" item-title="author_name" item-value="author_id" label="Author"
                variant="outlined"
                required/>
      <v-select v-model="post_category" :items="categories" item-title="category_name" item-value="id" label="Category" variant="outlined" required/>
      <v-file-input accept="image/*" v-model="image" label="Image" variant="outlined"
                    prepend-icon="mdi-camera"
                    required/>
    </v-card-text>

    <v-card-actions>
      <div class="d-flex w-100">
        <v-btn color="red" variant="tonal" @click="$emit('cancel', null)">Cancel</v-btn>
        <v-btn color="green" variant="tonal" @click="submitArticle" class="flex-grow-1">Post</v-btn>
      </div>
    </v-card-actions>
  </v-card>
</template>

<script>
import axios from 'axios';

export default {
  name: 'newArticle',
  data() {
    return {
      authors: undefined,
      categories: undefined,
      posts_title: '',
      post_author: null,
      post_category: null,
      posts_text: '',
      image: null,
      titleRules: [
        value => {
          if (value) return true

          return 'Title is required.'
        },
        value => {
          if (value?.length <= 15) return true

          return 'Title must be less than 15 characters.'
        },
      ],
      bodyRules: [
        value => {
          if (value) return true

          return 'Body is required.'
        },
        value => {
          if (value?.length <= 150) return true

          return 'Body must be less than 150 characters.'
        },
      ],
      emailRules: [
        value => {
          if (value) return true

          return 'E-mail is required.'
        },
        value => {
          if (/.+@.+\..+/.test(value)) return true

          return 'E-mail must be valid.'
        },
      ],

    };
  },
  mounted() {
    axios.get('http://localhost:8055/items/Authors')
      .then(response => {
        this.authors = response.data.data
        console.log(this.authors)
      });
    axios.get('http://localhost:8055/items/Category')
        .then(response => {
          this.categories = response.data.data
          console.log(this.categories)
        })
  },
  methods: {
    submitArticle() {
      let getImage = new FormData();
      getImage.append('posts_title', this.image[0].name);
      getImage.append('file', this.image[0]);

      axios.post('http://localhost:8055/files', getImage)
        .then(response => {
          let id = JSON.parse(response.request.response).data.id
          let data = `{
        "posts_title": "${this.posts_title}",
        "posts_text": "${this.posts_text}",
        "post_img": "${id}",
        "post_author":${this.post_author},
        "post_category":${this.post_category}
      }`

          console.log(data);
          fetch("http://localhost:8055/items/Posts", {
            method: 'POST',
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            body: data
          }).then(res => {
            res.json()
              .then(data => {
                console.log(data);
                this.$router.push('/');
              })
          }, error => {
            console.log(error)
            alert('Unable to add article.')
          })
        })
    },
  },
}
</script>
