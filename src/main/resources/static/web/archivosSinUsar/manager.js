const {createApp} = Vue
createApp({
    data(){
        return{
            data: [],
            firstName: "",
            lastName: "",
            email: "",
        }
    },
    created(){
        axios.get('http://localhost:8080/api/clients')
        .then(response =>{
            this.data = response.data
        })
        .catch(err => console.log(err))
    },
    methods: {
//        addClient(){
//            axios.post('http://localhost:8080/api/clients', {
//                firstName: this.firstName,
//                lastName: this.lastName,
//                email: this.email,
//            })
//            .catch(err => console.log(err))
//        }
    }
}).mount("#app")