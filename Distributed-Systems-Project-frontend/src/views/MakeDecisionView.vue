<script setup>
/* Το component συνδέεται με το κατάλληλο endpoint στο backend για να προσπελάσει το application που επιλέχτηκε και να
   ολοκληρώσει τον έλεγχό του. Αφού ολοκληρωθεί ο έλεγχος, το application αφαιρείται από την λίστα των applications
   προς έγκριση ("Applications" view) και ο χρήστης ενημερώνεται. */
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useRemoteData } from '@/composables/useRemoteData.js';

const route = useRoute();
const applicationId = ref(route.params.id);
const urlRef = ref(`http://localhost:8080/api/application/makeDecision/${applicationId.value}`);
const authRef = ref(true);
const methodRef = ref("POST");

console.log('applicationId:', applicationId.value);

const { data, performRequest } = useRemoteData(urlRef, authRef, methodRef);

onMounted(() => {
    performRequest();
});
</script>

<template>
  <!--Δημιουργία ενός alert που ενημερώνει τον χρήστη ότι το application ελέχθηκε και ένα κουμπί για μετάβαση πίσω στο "Applications" view.-->
    <div>
        <div class="alert alert-success" role="alert">
            <h4 class="alert-heading">Success!</h4>
            <p class="mb-0">{{ data }}!</p>
        </div>
        <router-link :to="{ name: 'applications' }" class="btn btn-primary">Back to Applications</router-link>
    </div>
</template>