<template>
    <ContentField>
          <div class="row justify-content-md-center">
            <div class="col-3">
              <form @submit.prevent="login">
                <div class="mb-3">
                  <label for="username" class="form-label">Username</label>
                  <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                </div>
                <div class="mb-3">
                  <label for="password" class="form-label">Password</label>
                  <input v-model="password" type="password" class="form-control" id="password" placeholder="请输入密码">
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

export default{
   components:{
       ContentField
   },
   setup(){
    //取出全局变量
    const store=useStore();
    //定义三个变量，初始为空
    let username=ref('');
    let password=ref('');
    let error_message=ref('');

    //定义登录触发函数
    const login = () =>{
      store.dispatch("login",{
        username:username.value,
        password:password.value,
        success(resp){
          console.log(resp);
        },
        error(resp){
          console.log(resp);
        }
      })
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
