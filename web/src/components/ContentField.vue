<template>
    <div class="container content-field">
        <div class="card content-color">
            <div class="card-body">
                   <slot></slot>
            </div>
        </div>
    </div>
    
</template>
<script>

</script>

<style scoped>
    div.content-field{
        margin:10px;
    }
    .content-color{
        background-color:lightblue;
    }
    
</style>