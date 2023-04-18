const {createApp} = Vue
createApp({
    data(){
        return{
            data: [],
        }
    },
    created(){
        axios.get('http://localhost:8080/api/clients/1')
        .then(response =>{
            this.data = response.data
            console.log(response.data)
        })
        .catch(err => console.log(err))
    },
    methods: {
    }
}).mount("#app")