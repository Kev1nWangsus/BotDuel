<template>
    <div ref="parent" class="gamemap">
        <canvas ref="canvas" tabindex="0"></canvas>
    </div>
</template>


<script>
import { GameMap } from '@/assets/scripts/GameMap';
import { ref, onMounted } from 'vue' // onMounted
import { useStore } from 'vuex'

export default {
    setup() {
        const store = useStore();
        let parent = ref(null);
        let canvas = ref(null);
        onMounted(() => {
            // after onMounted, create game object   
            new GameMap(canvas.value.getContext('2d'), parent.value, store);
        });
        return {
            parent,
            canvas
        }
    }
    
}
</script>
<style scoped>
div.gamemap {
    width: 100%;
    height: 100%;
    display: flex;
    align-content: center;
    justify-content: center; 
}
</style>