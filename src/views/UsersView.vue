<script setup>
// Το component συνδέεται με το κατάλληλο endpoint και επικοινωνεί με το backend για να παραλάβει τα στοιχεία όλων των χρηστών.
import { ref, onMounted, watch } from 'vue';
import { useRemoteData } from '@/composables/useRemoteData.js';
// const backendEnvVar = import.meta.env.VITE_BACKEND;

const urlRef = ref('/api/user');
const authRef = ref(true);
const { data, performRequest } = useRemoteData(urlRef, authRef);

onMounted(() => {
  performRequest();
});

watch(data, (newValue, oldValue) => {
    if (newValue) {
        console.log(newValue);
        console.log(data);
        console.log(data._rawValue);
        console.log(data._rawValue._embedded);
        console.log(data._rawValue._embedded.mUsers);
        console.log(data.value);
    }
});
</script>

<template>
  <!--Δημιουργία ενός table που περιέχει τα στοιχεία όλων των χρηστών-->
  <div class="bg-body-tertiary">
    <div class="container">
      <div class="row py-4 px-3">
        <div class="col-12">
          <div class="mb-4">
            <h1 class="fs-3">Users</h1>
          </div>
          <div>
            <table class="table">
              <thead>
              <tr>
                <th>Id</th>
                <th>Username</th>
<!--                <th>Password</th>-->
                <th>First Name</th>
                <th>Last Name</th>
                <th>Phone</th>
                <th>Email</th>
                <th>Address</th>
                <th>Actions</th>
              </tr>
              </thead>
              <tbody v-if="data && data.length > 0">
              <tr v-for="mUser in data" :key="mUser.id">
                <td>{{ mUser.id }}</td>
                <td>{{ mUser.username }}</td>
<!--                <td>{{ user.password }}</td>-->
                <td>{{ mUser.firstName }}</td>
                <td>{{ mUser.lastName }}</td>
                <td>{{ mUser.phone }}</td>
                <td>{{ mUser.email }}</td>
                <td>{{ mUser.address }}</td>
                <td>
                  <div class="btn-group">
                    <router-link v-if="typeof mUser.id !== 'undefined'" v-bind:to="{ name: 'edit-user', params: { id: mUser.id, phone: mUser.phone, email: mUser.email, address: mUser.address, data: data } }" class="btn btn-primary">Edit</router-link>
                    <router-link v-if="typeof mUser.id !== 'undefined'" v-bind:to="{ name: 'delete-user', params: { id: mUser.id } }" class="btn btn-danger">Delete</router-link>
                  </div>
                  <div class="mt-2">
                    <router-link v-if="typeof mUser.id !== 'undefined'" v-bind:to="{ name: 'role-to-user', params: { id: mUser.id, roleId: 1 } }" class="btn btn-outline-primary btn-sm">Assign Farmer Role</router-link>
                    <router-link v-if="typeof mUser.id !== 'undefined'" v-bind:to="{ name: 'role-to-user', params: { id: mUser.id, roleId: 2 } }" class="btn btn-outline-info btn-sm">Assign Inspector Role</router-link>
                  </div>
                </td>
              </tr>
              </tbody>
              <tbody v-else>
              <tr>
                <td colspan="8">No users available</td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="mt-4">
          <!-- Κουμπί για την πλοήγηση στην προβολή δημιουργίας νέου χρήστη -->
          <router-link :to="{ name: 'new-user' }" class="btn btn-success">Add New User</router-link>
        </div>
      </div>
    </div>
  </div>
</template>
