<template>
    <ContentField>
          <div class="row justify-content-md-center">
            <div class="col-3">
              <form @submit.prevent="login">
                <div class="mb-3">
                  <label for="username" class="form-label">Username</label>
                  <input v-model="username" type="text" class="form-control" id="username" placeholder="Please enter username">
                </div>
                <div class="mb-3">
                  <label for="password" class="form-label">Password</label>
                  <input v-model="password" type="password" class="form-control" id="password" placeholder="Please enter password">
                </div>
            <div class="error_message">{{ error_message }}</div>
                <button type="submit" class="btn btn-primary ">Login</button>
              </form>
            </div>
          </div>
    </ContentField>
</template>

<script>
import ContentField from "../../../components/ContentField.vue";
import { useStore } from 'vuex';
import { ref } from 'vue'
import router from "../../../router/index";

export default {
   components: {
      ContentField
   },
   setup() {
    // global variable
    const store = useStore();
    // initialize as empty
    let username = ref('');
    let password = ref('');
    let error_message = ref('');

    // ogin function
    const login = () => {
      // call function inside store .dispatch
      error_message.value = "";
      store.dispatch("login", {
        username: username.value,
        password: password.value,
        success() {
          store.dispatch("getinfo", {
            success() {
              router.push({name: 'home'});
              console.log(store.state.user);
            }
          })
          
        },
        error() {
          error_message.value = "Password and username mismatch";
        }
      });
    }

    return {
      username,
      password,
      error_message,
      login,
    }
   }
}
</script>

<style scoped>
button{
    width:100%;
}
div.error_message{
    color: red;
}
</style>
