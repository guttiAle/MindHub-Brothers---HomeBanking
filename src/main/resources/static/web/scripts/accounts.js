const {createApp} = Vue
createApp({
    data(){
        return{
            data: [],
        }
    },
    created(){
        axios.get('http://localhost:8080/api/clients/current')
        .then(response =>{
            this.data = response.data
            console.log(response.data)
        })
        .catch(err => console.log(err))
    },
    methods: {
        logout() {
            axios
                .post('/api/logout')
                .then(response => {
                window.location.replace('./index.html');
            })
            .catch(error => {
                console.error(error);
            })},
        create(){
            axios.post('/api/clients/current/accounts')
            .then(response => console.log('account created'))
            .catch(error => {console.error(error)})
        }
    }
}).mount("#app")